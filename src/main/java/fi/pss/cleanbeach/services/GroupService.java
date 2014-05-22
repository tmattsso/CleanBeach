/**
 *
 */
package fi.pss.cleanbeach.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.util.LoggingInterceptor;
import fi.pss.cleanbeach.ui.util.ImageUtil;

/**
 * @author denis
 * 
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
public class GroupService {

	private final Logger log = Logger.getLogger(getClass().getSimpleName());

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

		if (group.getLogo() != null) {

			Image uploadedImage = group.getLogo();

			byte[] image = ImageUtil.resizeIfBigger(uploadedImage.getContent(),
					150, uploadedImage.getMimetype());
			uploadedImage.setContent(image);
		}

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

	public List<UsersGroup> searchForGroups(User u, String searchText) {

		if (searchText == null || searchText.isEmpty()) {
			// special case: return all users groups instead

			if (u.getMemberIn().isEmpty()) {
				// no groups, we can't even make query
				return new ArrayList<>();
			}

			return new ArrayList<>(u.getMemberIn());
		}

		String q = "SELECT g FROM UsersGroup g "
				+ "WHERE UPPER(g.description) LIKE :param "
				+ "OR UPPER(g.name) LIKE :param ";

		Query query = entityManager.createQuery(q);
		query.setParameter("param", "%" + searchText.toUpperCase() + "%");

		@SuppressWarnings("unchecked")
		List<UsersGroup> l = query.getResultList();
		log.info("Search with string '" + searchText + "' returned " + l.size()
				+ " results");

		return l;
	}

	@SuppressWarnings("unchecked")
	public List<UsersGroup> getGroups() {
		return entityManager.createQuery("Select g from UsersGroup g")
				.getResultList();
	}
}
