package fi.pss.cleanbeach.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.Location.STATUS;
import fi.pss.cleanbeach.data.Thrash;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.data.User;

@Stateless
public class LocationService {

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

	public Set<Location> getLocationsNear(double latitude, double longitude) {
		Query q = em.createQuery("SELECT l FROM Location l WHERE "
				+ "(latitude<:latMax AND latitude>:latMin) "
				+ "AND (longitude<:longMax AND longitude>:longMin)");

		q.setParameter("latMax", latitude + 1);
		q.setParameter("latMin", latitude - 1);
		q.setParameter("longMax", longitude + 1);
		q.setParameter("longMin", longitude - 1);

		@SuppressWarnings("unchecked")
		java.util.List<Location> list = q.getResultList();

		return list == null ? new HashSet<Location>() : new HashSet<Location>(
				list);
	}

	public Location createLocation(double latitude, double longitude,
			String name) {

		Location l = new Location();
		l.setLongitude(longitude);
		l.setLatitude(latitude);
		l.setName(name);
		l.setStatus(STATUS.NO_DATA);
		em.persist(l);
		return l;
	}

	public Location save(Location selected) {
		selected = em.merge(selected);
		return selected;
	}

	public void setNumThrash(Integer value, ThrashType type, Location l,
			User currentUser) {

		// TODO should this have additional date filtering? if not, we'll
		// combine all thrash ever picked up by this user...

		// e.g. pickupTime > a day ago (or preferably this day)

		TypedQuery<Thrash> query = em
				.createQuery(
						"SELECT t from Thrash t WHERE "
								+ "t.type=:type AND t.reporter=:user AND t.location=:loc",
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

		String q = "SELECT t FROM Thrash t WHERE t.location=:loc AND t.reporter=:user";
		Query query = em.createQuery(q);
		query.setParameter("loc", selected);
		query.setParameter("user", user);
		@SuppressWarnings("unchecked")
		List<Thrash> l = query.getResultList();

		return new ThrashDAO(l);
	}
}
