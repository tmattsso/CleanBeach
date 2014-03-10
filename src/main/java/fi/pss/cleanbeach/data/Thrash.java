package fi.pss.cleanbeach.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Thrash extends AbstractEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	private User reporter;

	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;

	@ManyToOne(fetch = FetchType.LAZY)
	private Event event;

	@ManyToOne
	private ThrashType type;

	@Temporal(TemporalType.DATE)
	private Date pickupTime;

	private int num;

	/**
	 * Used for 'other' category
	 */
	@Column(length = 256)
	private String description;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public User getReporter() {
		return reporter;
	}

	public void setReporter(User reporter) {
		this.reporter = reporter;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public ThrashType getType() {
		return type;
	}

	public void setType(ThrashType type) {
		this.type = type;
	}

	public Date getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(Date pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
