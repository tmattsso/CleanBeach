package fi.pss.cleanbeach.data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id",
		"event_id" }) })
public class Signup extends AbstractEntity {

	private boolean accepted = false;

	@ManyToOne(optional = false)
	private User user;

	@ManyToOne(optional = false)
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
