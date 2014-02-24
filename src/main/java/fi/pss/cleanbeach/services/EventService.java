package fi.pss.cleanbeach.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

@Stateless
public class EventService {

	private final Logger log = Logger.getLogger(getClass().getSimpleName());

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

	/**
	 * Create a new event.
	 * 
	 * @param time
	 * @param where
	 * @param owner
	 * @param description
	 *            Maximum length is 248 characters.
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

		return e;
	}

	/**
	 * Gets all events from:<br/>
	 * - any groups the user is part of<br/>
	 * - any other events the user has registered for<br/>
	 * - any events that are near the user<br/>
	 * - that are max. 1 month old<br/>
	 * TODO: positioning, time
	 * 
	 * @param u
	 * @return
	 */
	public List<Event> getEventsForUser(User u, Double latitude,
			Double longitude) {

		u = em.merge(u);

		String q = "SELECT e FROM Event e WHERE ";
		if (!u.getMemberIn().isEmpty()) {
			q += "e.organizer IN (:orgs) OR ";
		}
		q += "e IN (SELECT s.event FROM Signup s WHERE s.user=:user)";

		Query query = em.createQuery(q);
		if (!u.getMemberIn().isEmpty()) {
			query.setParameter("orgs", u.getMemberIn());
		}
		query.setParameter("user", u);

		@SuppressWarnings("unchecked")
		List<Event> l = query.getResultList();
		fillWithThrashDetails(l);

		return l;
	}

	private void fillWithThrashDetails(List<Event> l) {

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

		String q = "SELECT e FROM Event e WHERE e IN ("
				+ "Select s FROM Signup s WHERE s.user=:user AND s.accepted=true"
				+ ")";

		Query query = em.createQuery(q);
		query.setParameter("user", u);

		@SuppressWarnings("unchecked")
		List<Event> l = query.getResultList();
		fillWithThrashDetails(l);

		return l;
	}

	/**
	 * Returns all events that match the query string. TODO: Check organizer
	 * name, desc, location name
	 * 
	 * @param u
	 * @param searchText
	 * @return
	 */
	public List<Event> searchForEvents(User u, String searchText) {

		String q = "SELECT e FROM Event e "
				+ "WHERE e.description LIKE :param "
				+ "OR e.organizer.name LIKE :param "
				+ "OR e.organizer.description LIKE :param "
				+ "OR e.location.name LIKE :param";

		Query query = em.createQuery(q);
		query.setParameter("param", "%" + searchText + "%");

		@SuppressWarnings("unchecked")
		List<Event> l = query.getResultList();
		log.info("Search with string '" + searchText + "' returned " + l.size()
				+ " results");

		fillWithThrashDetails(l);

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
			e.getJoinedUsers().add(s.getUser());
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
			img.setUploaded(new Date());
		}

		e = em.find(e.getClass(), e.getId());

		// make sure the collection is loaded
		e.getComments().size();

		e.getComments().add(c);

		e.setNumComments(e.getNumComments() + 1);
		if (img != null) {
			e.setNumCommentsWithImage(e.getNumCommentsWithImage() + 1);
		}

		em.merge(e);

		return c;
	}

	public void addThrashType(String name) {
		ThrashType t = new ThrashType();
		t.setName(name);
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

		if (join) {
			e.getJoinedUsers().add(currentUser);
		} else {
			e.getJoinedUsers().remove(currentUser);
		}

		return e;
	}

	public List<Event> getEvents(UsersGroup group) {
		TypedQuery<Event> query = em.createQuery(
				"SELECT e from Event e WHERE e.organizer=:group", Event.class);
		query.setParameter("group", group);

		List<Event> l = query.getResultList();
		fillWithThrashDetails(l);
		return l;
	}
}
