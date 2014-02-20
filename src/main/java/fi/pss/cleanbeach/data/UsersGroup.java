package fi.pss.cleanbeach.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class UsersGroup extends AbstractEntity {

    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Image logo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "members", joinColumns = { @JoinColumn(name = "member_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<User> members = new HashSet<>();

    @ManyToOne
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "admins", joinColumns = { @JoinColumn(name = "admin_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<User> admins = new HashSet<>();

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

}
