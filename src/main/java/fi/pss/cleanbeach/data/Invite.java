package fi.pss.cleanbeach.data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Invite extends AbstractEntity {

	private boolean accepted = false;

	@ManyToOne
	private User inviter;

	@ManyToOne
	private UsersGroup invitee;

	@ManyToOne
	private Event event;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public User getInviter() {
		return inviter;
	}

	public void setInviter(User inviter) {
		this.inviter = inviter;
	}

	public UsersGroup getInvitee() {
		return invitee;
	}

	public void setInvitee(UsersGroup invitee) {
		this.invitee = invitee;
	}
}
