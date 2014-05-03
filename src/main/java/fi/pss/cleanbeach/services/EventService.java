package fi.pss.cleanbeach.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fi.pss.cleanbeach.data.Comment;
import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.Signup;
import fi.pss.cleanbeach.data.Thrash;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.util.ImageUtil;

@Stateless
public class EventService {

	private final Logger log = Logger.getLogger(getClass().getSimpleName());

	/**
	 * Should be about 30km in all directions
	 */
	private final static double EVENT_SEARCH_COORDINATE_THRESHOLD = 0.27;

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

	/**
	 * Create a new event.
	 * 
	 * @param time
	 * @param where
	 * @param owner
	 * @param description
	 *            Maximum length is 2048 characters.
	 * @return the persisted {@link Event} object.
	 */
	public Event createEvent(Date time, Location where, UsersGroup owner,
			String description) {

		Event e = new Event();
		e.setLocation(where);
		e.setStart(time);
		e.setOrganizer(owner);
		e.setDescription(description);

		em.persist(e);

		e = loadDetails(e);

		return e;
	}

	/**
	 * Gets all events from:<br/>
	 * - any groups the user is part of<br/>
	 * - any other events the user has registered for<br/>
	 * - any events that are near the user<br/>
	 * - any events that a user group has endorsed<br/>
	 * - that are max. 1 month old<br/>
	 * 
	 * @param u
	 * @return
	 */
	public List<Event> getEventsForUser(User u, Double latitude,
			Double longitude) {

		u = em.merge(u);

		String q = "SELECT e FROM Event e WHERE (";
		if (!u.getMemberIn().isEmpty()) {
			// users' group is organizing
			q += "e.organizer IN (:orgs) OR ";
			// users group has endorsed
			q += "e IN (SELECT i.event FROM Invite i WHERE i.accepted=:accepted AND i.invitee IN (:usergroups)) OR ";
		}
		// the user has signed up for
		q += "e IN (SELECT s.event FROM Signup s WHERE s.user=:user)";

		if (latitude != null && longitude != null) {

			q += " OR (";
			q += " e.location.latitude < :latMax";
			q += " AND e.location.latitude > :latMin";

			q += " AND e.location.longitude < :longMax";
			q += " AND e.location.longitude > :longMin";
			q += ")";
		}

		q += ") AND e.start > :date";

		Query query = em.createQuery(q);
		if (!u.getMemberIn().isEmpty()) {
			query.setParameter("orgs", u.getMemberIn());
			query.setParameter("accepted", true);
			query.setParameter("usergroups", u.getMemberIn());
		}
		if (latitude != null && longitude != null) {
			query.setParameter("latMax", latitude
					+ EVENT_SEARCH_COORDINATE_THRESHOLD);
			query.setParameter("latMin", latitude
					- EVENT_SEARCH_COORDINATE_THRESHOLD);

			query.setParameter("longMax", longitude
					+ EVENT_SEARCH_COORDINATE_THRESHOLD);
			query.setParameter("longMin", longitude
					- EVENT_SEARCH_COORDINATE_THRESHOLD);
		}
		query.setParameter("user", u);
		query.setParameter("date", getMinStartTime());

		@SuppressWarnings("unchecked")
		List<Event> l = query.getResultList();
		fillWithThrashDetails(l);
		fillWithUserDetails(l);
		Collections.sort(l);

		return l;
	}

	private void fillWithThrashDetails(List<Event> l) {

		if (l == null || l.isEmpty()) {
			return;
		}

		// fetch trash numbers
		String q = "SELECT t FROM Thrash t WHERE t.event IN (:events)";
		Query query = em.createQuery(q);
		query.setParameter("events", l);
		@SuppressWarnings("unchecked")
		List<Thrash> thrash = query.getResultList();

		Map<Event, List<Thrash>> map = new HashMap<>();
		for (Thrash t : thrash) {
			List<Thrash> list = map.get(t.getEvent());
			if (list == null) {
				list = new ArrayList<>();
				map.put(t.getEvent(), list);
			}
			list.add(t);
		}

		for (Event e : l) {
			if (map.get(e) == null) {
				e.setThrash(new ThrashDAO(new ArrayList<Thrash>()));
			} else {
				e.setThrash(new ThrashDAO(new ArrayList<Thrash>(map.get(e))));
			}
		}
	}

	/**
	 * Gets all events the user has registered for (and not de-registered)
	 * 
	 * @param u
	 * @return
	 */
	public List<Event> getJoinedEventsForUser(User u) {
		u = em.merge(u);

		String q = "SELECT s.event FROM Signup s WHERE s.user=:user AND s.accepted=true AND s.event.start > :date";

		Query query = em.createQuery(q);
		query.setParameter("user", u);
		query.setParameter("date", getMinStartTime());

		@SuppressWarnings("unchecked")
		List<Event> l = query.getResultList();
		fillWithThrashDetails(l);
		fillWithUserDetails(l);
		Collections.sort(l);

		return l;
	}

	private Date getMinStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}

	/**
	 * Returns all events that match the query string.
	 * 
	 * <p>
	 * TODO what if user wants to search for old events?
	 * 
	 * @param u
	 * @param searchText
	 * @return
	 */
	public List<Event> searchForEvents(User u, String searchText) {

		if (searchText == null || searchText.isEmpty()) {
			// special case: return all events in users groups instead

			if (u.getMemberIn().isEmpty()) {
				// no groups, we can't even make query
				return new ArrayList<>();
			}

			String q = "SELECT e FROM Event e "
					+ "WHERE e.organizer IN (:usergroups)";

			Query query = em.createQuery(q);
			query.setParameter("usergroups", u.getMemberIn());

			@SuppressWarnings("unchecked")
			List<Event> l = query.getResultList();
			fillWithThrashDetails(l);
			fillWithUserDetails(l);

			Collections.sort(l);

			return l;
		}

		String q = "SELECT e FROM Event e "
				+ "WHERE UPPER(e.description) LIKE :param "
				+ "OR UPPER(e.organizer.name) LIKE :param "
				+ "OR UPPER(e.organizer.description) LIKE :param "
				+ "OR UPPER(e.location.name) LIKE :param "
				+ "AND e.start > :start";

		Query query = em.createQuery(q);
		query.setParameter("param", "%" + searchText.toUpperCase() + "%");

		Calendar cal = Calendar.getInstance();
		// all events from a week ago forward
		cal.add(Calendar.WEEK_OF_YEAR, -1);
		query.setParameter("start", cal.getTime());

		@SuppressWarnings("unchecked")
		List<Event> l = query.getResultList();
		log.info("Search with string '" + searchText + "' returned " + l.size()
				+ " results");

		fillWithThrashDetails(l);
		fillWithUserDetails(l);

		Collections.sort(l);

		return l;
	}

	public Event loadDetails(Event e) {

		e = em.find(e.getClass(), e.getId());

		// init comments
		e.getComments().size();

		// get users
		String q = "SELECT s FROM Signup s WHERE s.event=:event";
		Query query = em.createQuery(q);
		query.setParameter("event", e);
		@SuppressWarnings("unchecked")
		List<Signup> l = query.getResultList();
		for (Signup s : l) {
			if (s.isAccepted()) {
				e.getJoinedUsers().add(s.getUser());
			}
		}

		e.setThrash(getCollectedThrash(e));

		return e;
	}

	/**
	 * 
	 * @param e
	 * @param text
	 *            Maximum length is 248 characters.
	 * @param img
	 * @param u
	 * @return
	 */
	public Comment addComment(Event e, String text, Image img, User u) {

		Comment c = new Comment();
		c.setAuthor(u);
		c.setContent(text);
		c.setImage(img);
		c.setWritetime(new Date());

		if (img != null) {

			img.setContent(ImageUtil.resizeIfBigger(img.getContent(), 1024,
					img.getMimetype()));

			img.setUploaded(new Date());
		}

		e = em.find(e.getClass(), e.getId());

		// make sure the collection is loaded
		e.getComments().size();

		e.getComments().add(0, c);

		e.setNumComments(e.getNumComments() + 1);
		if (img != null) {
			e.setNumCommentsWithImage(e.getNumCommentsWithImage() + 1);
		}

		em.merge(e);

		return c;
	}

	public void addThrashType(String name, boolean other) {
		ThrashType t = new ThrashType();
		t.setName(name);
		t.setOther(other);
		em.persist(t);
	}

	public List<ThrashType> getThrashTypes() {
		String q = "SELECT s FROM ThrashType s";
		Query query = em.createQuery(q);
		@SuppressWarnings("unchecked")
		List<ThrashType> l = query.getResultList();

		return l;
	}

	public ThrashDAO getCollectedThrash(Event e) {

		String q = "SELECT t FROM Thrash t WHERE t.event=:e";
		Query query = em.createQuery(q);
		query.setParameter("e", e);
		@SuppressWarnings("unchecked")
		List<Thrash> l = query.getResultList();

		return new ThrashDAO(l);
	}

	public Event setUserJoined(Event e, User currentUser, boolean join) {
		String q = "SELECT s FROM Signup s WHERE s.event=:event AND s.user=:user";
		Query query = em.createQuery(q);
		query.setParameter("event", e);
		query.setParameter("user", currentUser);

		Signup s = null;
		try {
			s = (Signup) query.getSingleResult();
		} catch (NoResultException ex) {
			// fine
		}

		if (s != null) {
			s.setAccepted(join);
			s = em.merge(s);
		} else {
			s = new Signup();
			s.setUser(currentUser);
			s.setEvent(e);
			s.setAccepted(join);
			em.persist(s);
		}

		e = em.find(e.getClass(), e.getId());
		e = loadDetails(e);
		if (join) {
			e.getJoinedUsers().add(currentUser);
		} else {
			e.getJoinedUsers().remove(currentUser);
		}

		return e;
	}

	/**
	 * TODO restrict time?
	 * 
	 * @param group
	 * @return
	 */
	public List<Event> getEvents(UsersGroup group) {
		TypedQuery<Event> query = em
				.createQuery(
						"SELECT e from Event e WHERE e.organizer=:group OR e IN "
								+ "(SELECT i.event FROM Invite i WHERE i.invitee=:group AND accepted=:acc)",
						Event.class);
		query.setParameter("group", group);
		query.setParameter("acc", true);

		List<Event> l = query.getResultList();
		fillWithThrashDetails(l);
		fillWithUserDetails(l);
		Collections.sort(l);
		return l;
	}

	/**
	 * TODO restrict time?
	 * 
	 * @param group
	 * @return
	 */
	public List<Event> getEvents(Location loc) {
		TypedQuery<Event> query = em.createQuery(
				"SELECT e from Event e WHERE e.location=:loc", Event.class);
		query.setParameter("loc", loc);

		List<Event> l = query.getResultList();
		fillWithThrashDetails(l);
		fillWithUserDetails(l);
		Collections.sort(l);
		return l;
	}

	private void fillWithUserDetails(List<Event> l) {
		if (l == null || l.isEmpty()) {
			return;
		}

		String q = "SELECT s FROM Signup s WHERE s.accepted=:acepted AND s.event IN (:events)";
		Query query = em.createQuery(q);
		query.setParameter("acepted", true);
		query.setParameter("events", l);

		@SuppressWarnings("unchecked")
		List<Signup> list = query.getResultList();

		for (Event e : l) {
			for (Signup s : list) {
				if (s.getEvent().equals(e)) {
					e.getJoinedUsers().add(s.getUser());
				}
			}
		}
	}

	public void setThrash(int value, ThrashType type, Event event,
			User currentUser) {
		TypedQuery<Thrash> query = em
				.createQuery(
						"SELECT t from Thrash t WHERE "
								+ "t.type=:type AND t.reporter=:user AND t.event=:event",
						Thrash.class);
		query.setParameter("type", type);
		query.setParameter("user", currentUser);
		query.setParameter("event", event);

		Thrash t;
		try {
			t = query.getSingleResult();
		} catch (NoResultException e) {
			// no input yet, create
			t = new Thrash();
			t.setEvent(event);
			t.setLocation(null);
			t.setPickupTime(new Date());
			t.setReporter(currentUser);
			t.setType(type);
			em.persist(t);
		}

		t.setNum(value);
		em.merge(t);
	}

	public void delete(Event e) {

		// invitations
		String q = "DELETE FROM Invite i WHERE i.event=:event";
		Query query = em.createQuery(q);
		query.setParameter("event", e);
		query.executeUpdate();
		em.flush();

		// signups
		q = "DELETE FROM Signup i WHERE i.event=:event";
		query = em.createQuery(q);
		query.setParameter("event", e);
		query.executeUpdate();
		em.flush();

		// thrash
		q = "DELETE FROM Thrash i WHERE i.event=:event";
		query = em.createQuery(q);
		query.setParameter("event", e);
		query.executeUpdate();
		em.flush();

		// event itself
		e = em.find(Event.class, e.getId());
		em.remove(e);

		em.flush();
	}

	public fi.pss.cleanbeach.data.Event loadDetails(long eventId) {
		Event e = em.find(Event.class, eventId);
		if (e != null) {
			e = loadDetails(e);
		}
		return e;
	}

	public Event saveEvent(Event event, String desc, Date start) {
		event = em.find(Event.class, event.getId());
		event.setDescription(desc);
		event.setStart(start);
		event = em.merge(event);
		event = loadDetails(event);
		return event;
	}

	public Collection<Event> getEventsNear(Double latitude, Double longitude,
			Integer zoomLevel) {

		Query q = em
				.createQuery("SELECT e FROM Event e WHERE e.start>:now AND "
						+ "(e.location.latitude<:latMax AND e.location.latitude>:latMin) "
						+ "AND (e.location.longitude<:longMax AND e.location.longitude>:longMin)");

		q.setParameter("now", new Date());

		// TODO base threshold on zoom level
		q.setParameter("latMax", latitude + 1);
		q.setParameter("latMin", latitude - 1);
		q.setParameter("longMax", longitude + 1);
		q.setParameter("longMin", longitude - 1);

		@SuppressWarnings("unchecked")
		java.util.List<Event> list = q.getResultList();
		if (list == null) {
			return new HashSet<Event>();
		}

		// filter duplicate events for same location
		Map<Location, Event> nextEvents = new HashMap<Location, Event>();
		for (Event event : list) {
			if (!nextEvents.containsKey(event.getLocation())) {
				nextEvents.put(event.getLocation(), event);
				continue;
			}
			Event other = nextEvents.get(event.getLocation());
			if (other.getStart().getTime() > event.getStart().getTime()) {
				nextEvents.put(event.getLocation(), other);
			}
		}

		return nextEvents.values();
	}
}
