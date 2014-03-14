package fi.pss.cleanbeach.services;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;

/**
 * @author denis
 * 
 */
@Stateless
public class InviteService {

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager entityManager;

	/**
	 * Send an invite from <code>inviter</code> to <code>group</code> with
	 * <code>event</code> attached.
	 * 
	 * @param inviter
	 *            Inviter user
	 * @param group
	 *            Invitee group
	 * @param event
	 *            Event
	 * @return Invitation
	 */
	public Invite invite(User inviter, UsersGroup group, Event event) {

		// check for existing invite
		String q = "SELECT i FROM Invite i WHERE i.invitee=:group AND i.inviter=:user AND i.event=:event";
		Query query = entityManager.createQuery(q);
		query.setParameter("group", group);
		query.setParameter("user", inviter);
		query.setParameter("event", event);

		Invite i = null;
		try {
			i = (Invite) query.getSingleResult();
		} catch (NoResultException e) {
			// fine
		}

		if (i != null) {
			// there already is an invitefrom this user; ignore this
			return null;
		}

		Invite invite = new Invite();
		invite.setInviter(inviter);
		invite.setInvitee(group);
		invite.setEvent(event);
		entityManager.persist(invite);
		return invite;
	}

	/**
	 * Returns all invitations for <code>group</code>.
	 * 
	 * @param group
	 *            Users group
	 * @return list of invitations
	 */
	public Collection<Invite> getInvitations(UsersGroup group) {
		TypedQuery<Invite> query = entityManager.createQuery(
				"SELECT i from Invite i WHERE i.invitee=:group", Invite.class);
		query.setParameter("group", group);
		return query.getResultList();
	}

	/**
	 * Returns pending invitations for <code>group</code>.
	 * 
	 * @param group
	 *            Users group
	 * @return list of invitations
	 */
	public Collection<Invite> getPendingInvitations(UsersGroup group) {
		TypedQuery<Invite> query = entityManager.createQuery(
				"SELECT i from Invite i WHERE i.invitee=:group", Invite.class);
		query.setParameter("group", group);
		return query.getResultList();
	}

	public Collection<Invite> getPendingInvitations(Event e) {
		TypedQuery<Invite> query = entityManager.createQuery(
				"SELECT i from Invite i WHERE i.event=:event", Invite.class);
		query.setParameter("event", e);
		return query.getResultList();
	}
}
