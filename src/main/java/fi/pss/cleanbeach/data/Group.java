package fi.pss.cleanbeach.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Group extends AbstractEntity {

	private String name;
	private String description;

	@OneToOne(cascade = CascadeType.ALL)
	private Image logo;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<User> members = new HashSet<>();

	@ManyToOne
	private User creator;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<User> admins = new HashSet<>();

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "organizer")
	private List<Event> events = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getLogo() {
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Set<User> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<User> admins) {
		this.admins = admins;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
}
