package fi.pss.cleanbeach.data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Signup extends AbstractEntity {

	private boolean accepted = false;

	@ManyToOne
	private User user;

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

	public User getUser() {
		return user;
	}

	public void setUser(User inviter) {
		user = inviter;
	}
}
