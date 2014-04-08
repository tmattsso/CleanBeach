/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.services.GroupService;
import fi.pss.cleanbeach.services.GroupService.CannotDeleteException;
import fi.pss.cleanbeach.services.InviteService;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.eventdetails.EventDetailsPresenter;
import fi.pss.cleanbeach.ui.views.events.IEventPresenter;

/**
 * @author denis
 * 
 */
public class GroupPresenter extends EventDetailsPresenter<IGroup> implements
		Serializable, IEventPresenter {

	private static final long serialVersionUID = 8624452511940560036L;

	@Inject
	private GroupService groupService;

	@Inject
	private InviteService inviteService;

	@Inject
	private EventService eventService;

	@Override
	public void init(User currentUser) {

	}

	public void loadGroups() {
		User user = view.getUser();
		if (user != null) {
			view.backToMainAndReset();
			view.showAdminGroups(groupService.getAdminGroups(user));
			view.showMemberGroups(groupService.getMemberGroups(user));
		}
	}

	public String getEventInvitations(UsersGroup group) {
		int size = inviteService.getPendingInvitations(group, true).size();
		if (size == 1) {
			return Lang.get("Groups.view.events.amount.singular", size);
		}
		return Lang.get("Groups.view.events.amount.plural", size);
	}

	public String getPendingEventInvitations(UsersGroup group) {
		int size = inviteService.getPendingInvitations(group, true).size();
		if (size == 1) {
			return Lang.get("Groups.view.pending.events.amount.singular", size);
		}
		return Lang.get("Groups.view.pending.events.amount.plural", size);
	}

	public String getMembers(UsersGroup group) {
		if (group.getMembers().size() == 1) {
			return Lang.get("Groups.view.members.amount.singular", group
					.getMembers().size());
		}
		return Lang.get("Groups.view.members.amount.plural", group.getMembers()
				.size());
	}

	public void showGroup(UsersGroup group) {
		view.showGroupDetails(group);
	}

	public void searchGroups() {
		// TODO Auto-generated method stub

	}

	public void createGroup() {
		UsersGroup usersGroup = new UsersGroup();
		view.showEditGroup(usersGroup);
	}

	public void requestLeaveGroup(UsersGroup group) {
		view.showLeaveConfirmation();
	}

	public void requestJoinGroup(UsersGroup group) {
		view.showJoinConfirmation();
	}

	public void leaveGroup(UsersGroup group) {
		try {
			UsersGroup userGroup;
			userGroup = groupService.removeMember(group, view.getUser());
			view.updateMembershipState(userGroup);
		} catch (CannotDeleteException e) {
			view.showErrorNotification(Lang.get("Group.errors.leavegroup"));
		}
	}

	public void joinGroup(UsersGroup group) {
		UsersGroup userGroup = groupService.addMember(group, view.getUser());
		view.updateMembershipState(userGroup);
	}

	public void createEvent() {
		// TODO Auto-generated method stub

	}

	public void showManageAdmins(UsersGroup group) {
		view.showManageAdmins(group);
	}

	public void showMembers(UsersGroup group) {
		view.showMembersInGroup(group);
	}

	public boolean canJoin(UsersGroup group) {
		return !group.getMembers().contains(view.getUser());
	}

	public boolean canLeave(UsersGroup group) {
		if (group.getMembers().contains(view.getUser())) {
			if (group.getAdmins().contains(view.getUser())) {
				return !group.getCreator().equals(view.getUser());
			}
			return true;
		}
		return false;
	}

	public boolean canManage(UsersGroup group) {
		return group.getAdmins().contains(view.getUser());
	}

	public List<fi.pss.cleanbeach.data.Event> getEvents(UsersGroup group) {
		return eventService.getEvents(group);
	}

	public void setAdmin(UsersGroup group, User u, Boolean value) {
		try {
			groupService.setAdminStatus(group, u, value);
		} catch (CannotDeleteException e) {
			view.showErrorNotification(Lang.get("Group.errors.admin"));
		}
	}

	public void showInvitations(UsersGroup group) {
		Collection<Invite> pendingInvitations = inviteService
				.getPendingInvitations(group, false);
		view.showInvitations(group, pendingInvitations);
	}

	public void changeInvite(Invite i, Boolean value) {
		i.setAccepted(value);
		inviteService.update(i);
		view.updateGroupDetails(i.getInvitee());
	}

	public void saveGroup(UsersGroup group, User currentUser) {
		if (group.getId() == null) {
			group.setCreator(currentUser);
			group.getMembers().add(currentUser);
			group.getAdmins().add(currentUser);
		}

		group = groupService.save(group);

		loadGroups();
		view.showGroupDetails(group);
		view.showGroupSaveConfirmation(group);
	}

	public void showEdit(UsersGroup group) {
		view.showEditGroup(group);
	}

	public boolean canDelete(UsersGroup group) {
		return group.getCreator().equals(view.getUser());
	}

	public void delete(UsersGroup group) {
		try {
			groupService.delete(group);
			loadGroups();
		} catch (CannotDeleteException e) {
			view.showErrorNotification(Lang.get("Group.errors.delete.caption"),
					Lang.get("Group.errors.delete.msg"));
		}
	}

}
