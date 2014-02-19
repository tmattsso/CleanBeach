/**
 *
 */
package fi.pss.cleanbeach.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;

/**
 * @author denis
 * 
 */
@Stateless
public class GroupService {

    @PersistenceContext(unitName = "cleanbeach")
    private EntityManager entityManager;

    public Set<UsersGroup> getMemberGroups(User user) {
        User u = entityManager.find(User.class, user.getId());
        return Collections.unmodifiableSet(u.getMemberIn());

    }

    public Set<UsersGroup> getAdminGroups(User user) {
        User u = entityManager.find(User.class, user.getId());
        Set<UsersGroup> result = new HashSet<>();
        Set<UsersGroup> groups = u.getMemberIn();
        for (UsersGroup group : groups) {
            if (group.getAdmins().contains(u)) {
                result.add(group);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    public UsersGroup addMember(UsersGroup group, User user) {
        doAddMember(group, user);
        UsersGroup userGroup = entityManager.merge(group);
        entityManager.merge(user);

        return userGroup;
    }

    public UsersGroup addAdmin(UsersGroup group, User user) {
        doAddMember(group, user);
        group.getAdmins().add(user);

        UsersGroup userGroup = entityManager.merge(group);
        entityManager.merge(user);

        return userGroup;
    }

    public UsersGroup removeMember(UsersGroup group, User user) {
        group.getMembers().remove(user);
        group.getAdmins().remove(user);
        user.getMemberIn().remove(group);

        UsersGroup userGroup = entityManager.merge(group);
        entityManager.merge(user);

        return userGroup;
    }

    public UsersGroup createGroup(User user, String name, String description) {
        UsersGroup group = new UsersGroup();
        User creator = entityManager.merge(user);
        group.setCreator(creator);
        group.setName(name);
        group.setDescription(description);

        entityManager.persist(group);

        return group;
    }

    public void save(UsersGroup group) {
        entityManager.persist(group);
    }

    private void doAddMember(UsersGroup group, User user) {
        group.getMembers().add(user);
        user.getMemberIn().add(group);
    }

}
