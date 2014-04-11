/**
 *
 */
package fi.pss.cleanbeach.services;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

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
		return u.getMemberIn();

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
		return result;
	}

	public UsersGroup addMember(UsersGroup group, User user) {
		UsersGroup userGroup = doAddMember(group, user);
		userGroup = entityManager.merge(userGroup);
		entityManager.merge(user);

		return userGroup;
	}

	public UsersGroup removeMember(UsersGroup group, User user)
			throws CannotDeleteException {
		UsersGroup userGroup = entityManager.find(UsersGroup.class,
				group.getId());

		// can't remove creator
		if (group.getCreator().equals(user)) {
			throw new CannotDeleteException();
		}

		userGroup.getMembers().remove(user);
		userGroup.getAdmins().remove(user);
		user.getMemberIn().remove(group);

		userGroup = entityManager.merge(userGroup);
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

	public UsersGroup save(UsersGroup group) {
		return entityManager.merge(group);
	}

	private UsersGroup doAddMember(UsersGroup group, User user) {
		UsersGroup userGroup = entityManager.find(UsersGroup.class,
				group.getId());
		userGroup.getMembers().add(user);
		user.getMemberIn().add(group);
		return userGroup;
	}

	public void setAdminStatus(UsersGroup group, User u, boolean value)
			throws CannotDeleteException {

		group = entityManager.find(group.getClass(), group.getId());
		if (value) {
			group.getAdmins().add(u);
		} else {

			// can't remove creator
			if (group.getCreator().equals(u)) {
				throw new CannotDeleteException();
			}

			group.getAdmins().remove(u);
		}

		entityManager.merge(group);
	}

	public void delete(UsersGroup group) throws CannotDeleteException {
		try {
			group = entityManager.find(UsersGroup.class, group.getId());
			entityManager.remove(group);
			entityManager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new CannotDeleteException();
		}
	}

	public static class CannotDeleteException extends Exception {

		private static final long serialVersionUID = -8820995400069653254L;

	}
}
