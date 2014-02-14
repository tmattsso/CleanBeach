package fi.pss.cleanbeach.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Thrash extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY)
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
