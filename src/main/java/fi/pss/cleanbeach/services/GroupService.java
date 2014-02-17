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
        return Collections.unmodifiableSet(user.getMemberIn());

    }

    public Set<UsersGroup> getAdminGroups(User user) {
        Set<UsersGroup> result = new HashSet<>();
        Set<UsersGroup> groups = user.getMemberIn();
        for (UsersGroup group : groups) {
            if (group.getAdmins().contains(user)) {
                result.add(group);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    public void addMember(UsersGroup group, User user) {
        doAddMember(group, user);
        entityManager.persist(group);
        entityManager.persist(user);
    }

    public void addAdmin(UsersGroup group, User user) {
        doAddMember(group, user);
        group.getAdmins().add(user);

        entityManager.persist(group);
        entityManager.persist(user);
    }

    public void save(UsersGroup group) {
        entityManager.persist(group);
    }

    private void doAddMember(UsersGroup group, User user) {
        group.getMembers().add(user);
        user.getMemberIn().add(group);
    }
}
