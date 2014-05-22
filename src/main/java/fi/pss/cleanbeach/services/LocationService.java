package fi.pss.cleanbeach.services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.Location.STATUS;
import fi.pss.cleanbeach.data.Thrash;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.util.LoggingInterceptor;

@Stateless
@Interceptors(LoggingInterceptor.class)
public class LocationService {

	/**
	 * Coordinates closer than this to an existing location are not allowed.
	 * Should be roughly 50 meter radius.
	 */
	private final static double LOCATION_CREATE_COORDINATE_THRESHOLD = 0.0009;

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

	public Set<Location> getLocationsNear(double latitude, double longitude) {
		Query q = em.createQuery("SELECT l FROM Location l WHERE "
				+ "(latitude<:latMax AND latitude>:latMin) "
				+ "AND (longitude<:longMax AND longitude>:longMin)");

		// TODO base threshold on zoom level
		q.setParameter("latMax", latitude + 1);
		q.setParameter("latMin", latitude - 1);
		q.setParameter("longMax", longitude + 1);
		q.setParameter("longMin", longitude - 1);

		@SuppressWarnings("unchecked")
		java.util.List<Location> list = q.getResultList();

		return list == null ? new HashSet<Location>() : new HashSet<Location>(
				list);
	}

	/**
	 * Creates location if no location can be found right beside it.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param name
	 * @return
	 */
	public Location createLocation(double latitude, double longitude,
			String name) {

		// check for existing
		String qs = "SELECT l FROM Location l WHERE "
				+ "(latitude<:latMax AND latitude>:latMin) "
				+ "AND (longitude<:longMax AND longitude>:longMin)";

		Query q = em.createQuery(qs);

		q.setParameter("latMax", latitude
				+ LOCATION_CREATE_COORDINATE_THRESHOLD);
		q.setParameter("latMin", latitude
				- LOCATION_CREATE_COORDINATE_THRESHOLD);
		q.setParameter("longMax", longitude
				+ LOCATION_CREATE_COORDINATE_THRESHOLD);
		q.setParameter("longMin", longitude
				- LOCATION_CREATE_COORDINATE_THRESHOLD);

		List<?> existing = q.getResultList();
		if (!existing.isEmpty()) {
			return null;
		}

		Location l = new Location();
		l.setLongitude(longitude);
		l.setLatitude(latitude);
		l.setName(name);
		l.setStatus(STATUS.NO_DATA);
		em.persist(l);
		return l;
	}

	public void setNumThrash(Integer value, ThrashType type, Location l,
			User currentUser) {

		TypedQuery<Thrash> query = em
				.createQuery(
						"SELECT t from Thrash t WHERE "
								+ "t.type=:type AND t.reporter=:user AND t.location=:loc AND t.pickupTime=CURRENT_DATE",
						Thrash.class);
		query.setParameter("type", type);
		query.setParameter("user", currentUser);
		query.setParameter("loc", l);

		Thrash t;
		try {
			t = query.getSingleResult();
		} catch (NoResultException e) {
			// no input yet, create
			t = new Thrash();
			t.setEvent(null);
			t.setLocation(l);
			t.setPickupTime(new Date());
			t.setReporter(currentUser);
			t.setType(type);
			em.persist(t);
		}

		t.setNum(value);
		em.merge(t);
	}

	public ThrashDAO getThrash(Location selected, User user) {

		String q = "SELECT t FROM Thrash t WHERE "
				+ "t.location=:loc AND t.reporter=:user AND t.pickupTime=CURRENT_DATE";
		Query query = em.createQuery(q);
		query.setParameter("loc", selected);
		query.setParameter("user", user);
		@SuppressWarnings("unchecked")
		List<Thrash> l = query.getResultList();

		return new ThrashDAO(l);
	}

	public Location setDirty(Location selected, String desc) {
		selected = em.find(selected.getClass(), selected.getId());
		selected.setStatus(STATUS.DIRTY);
		selected.setStatusMsg(desc);
		selected = em.merge(selected);
		return selected;
	}

	public void setDescription(ThrashType t, User currentUser, String value,
			Event event, Location l) {
		String q = "SELECT t FROM Thrash t WHERE "
				+ "t.type=:t AND t.reporter=:user AND t.pickupTime=CURRENT_DATE ";

		if (event != null) {
			q += "AND t.event=:event ";
		}
		if (l != null) {
			q += "AND t.location=:loc ";
		}

		Query query = em.createQuery(q);
		query.setParameter("t", t);
		query.setParameter("user", currentUser);

		if (event != null) {
			query.setParameter("event", event);
		}
		if (l != null) {
			query.setParameter("loc", l);
		}

		Thrash thrash = null;
		try {
			thrash = (Thrash) query.getSingleResult();
		} catch (NoResultException e) {
			// create
			thrash = new Thrash();
			thrash.setReporter(currentUser);
			thrash.setPickupTime(new Date());
			thrash.setType(t);
			thrash.setEvent(event);
			thrash.setLocation(l);
			em.persist(thrash);
		}
		thrash.setDescription(value);
		em.merge(thrash);
	}

	public Collection<Location> getLocationsForCreate() {
		Query q = em.createQuery("SELECT l FROM Location l");

		@SuppressWarnings("unchecked")
		java.util.List<Location> list = q.getResultList();

		return list == null ? new HashSet<Location>() : new HashSet<Location>(
				list);
	}

	public HashSet<Location> getLocationsNearForCreate(Double lat, Double lon) {
		Query q = em.createQuery("SELECT l FROM Location l WHERE "
				+ "(latitude<:latMax AND latitude>:latMin) "
				+ "AND (longitude<:longMax AND longitude>:longMin)");

		q.setParameter("latMax", lat + LOCATION_CREATE_COORDINATE_THRESHOLD);
		q.setParameter("latMin", lat - LOCATION_CREATE_COORDINATE_THRESHOLD);
		q.setParameter("longMax", lon + LOCATION_CREATE_COORDINATE_THRESHOLD);
		q.setParameter("longMin", lon - LOCATION_CREATE_COORDINATE_THRESHOLD);

		@SuppressWarnings("unchecked")
		java.util.List<Location> list = q.getResultList();

		return list == null ? new HashSet<Location>() : new HashSet<Location>(
				list);
	}

	@SuppressWarnings("unchecked")
	public List<Location> getLocations() {
		return em.createQuery("SELECT l FROM Location l").getResultList();
	}

	public ThrashDAO getThrash(Location loc) {
		String q = "SELECT t FROM Thrash t WHERE " + "t.location=:loc";
		Query query = em.createQuery(q);
		query.setParameter("loc", loc);
		@SuppressWarnings("unchecked")
		List<Thrash> l = query.getResultList();

		return new ThrashDAO(l);
	}

	public ThrashDAO getThrash() {
		String q = "SELECT t FROM Thrash t ";
		Query query = em.createQuery(q);
		@SuppressWarnings("unchecked")
		List<Thrash> l = query.getResultList();

		return new ThrashDAO(l);
	}
}
