package fi.pss.cleanbeach.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Event extends AbstractEntity {

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date start;

	@ManyToOne
	private Location location;

	@ManyToOne
	private Group organizer;

	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "order")
	private final List<Comment> comments = new ArrayList<>();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Group getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Group organizer) {
		this.organizer = organizer;
	}
}
