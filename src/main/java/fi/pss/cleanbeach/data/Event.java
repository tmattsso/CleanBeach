package fi.pss.cleanbeach.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Event extends AbstractEntity {

	@Column(length = 2048)
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date start;

	@ManyToOne
	private Location location;

	@ManyToOne
	private UsersGroup organizer;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderColumn(name = "eventorder")
	private List<Comment> comments = new ArrayList<>();

	private int numComments = 0;

	private int numCommentsWithImage = 0;

	@Transient
	private Collection<User> joinedUsers = new HashSet<>();

	@Transient
	private ThrashDAO thrash;

	public Collection<User> getJoinedUsers() {
		return joinedUsers;
	}

	public void setJoinedUsers(Collection<User> joinedUsers) {
		this.joinedUsers = joinedUsers;
	}

	public String getDescription() {
		return description;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
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

	public UsersGroup getOrganizer() {
		return organizer;
	}

	public void setOrganizer(UsersGroup organizer) {
		this.organizer = organizer;
	}

	public int getNumComments() {
		return numComments;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

	public int getNumCommentsWithImage() {
		return numCommentsWithImage;
	}

	public void setNumCommentsWithImage(int numCommentsWithImage) {
		this.numCommentsWithImage = numCommentsWithImage;
	}

	public ThrashDAO getThrash() {
		return thrash;
	}

	public void setThrash(ThrashDAO thrash) {
		this.thrash = thrash;
	}
}
