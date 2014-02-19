package fi.pss.cleanbeach.services;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
                "SELECT i from Invite i WHERE i.invitee=:group "
                        + "AND i.accepted=0", Invite.class);
        query.setParameter("group", group);
        return query.getResultList();
    }
}
