package fi.pss.cleanbeach.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;

@Stateless
public class EventService {

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

	/**
	 * Create a new event.
	 * 
	 * @param time
	 * @param where
	 * @param owner
	 * @param description
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

		String q = "SELECT e FROM Event e WHERE " + "e.organizer IN (:orgs) "
				+ "OR e IN (SELECT s.event FROM Signup s WHERE s.user=:user)";

		Query query = em.createQuery(q);
		query.setParameter("orgs", u.getMemberIn());
		query.setParameter("user", u);

		@SuppressWarnings("unchecked")
		List<Event> l = query.getResultList();

		return l;
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

		String q = "SELECT e FROM Event e WHERE e.description LIKE :param";

		Query query = em.createQuery(q);
		query.setParameter("param", "%" + searchText + "%");

		@SuppressWarnings("unchecked")
		List<Event> l = query.getResultList();

		return l;
	}

}
