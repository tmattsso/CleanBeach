/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.services.GroupService;
import fi.pss.cleanbeach.services.InviteService;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.events.IEventPresenter;

/**
 * @author denis
 * 
 */
public class GroupPresenter extends AbstractPresenter<IGroup> implements
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
			view.showAdminGroups(groupService.getAdminGroups(user));
			view.showMemberGroups(groupService.getMemberGroups(user));
		}
	}

	public String getEventInvitations(UsersGroup group) {
		int size = inviteService.getPendingInvitations(group).size();
		if (size == 1) {
			return Lang.get("Groups.view.events.amount.singular", size);
		}
		return Lang.get("Groups.view.events.amount.plural", size);
	}

	public String getPendingEventInvitations(UsersGroup group) {
		int size = inviteService.getPendingInvitations(group).size();
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
		// TODO Auto-generated method stub

	}

	public void requestLeaveGroup(UsersGroup group) {
		view.showLeaveConfirmation();
	}

	public void requestJoinGroup(UsersGroup group) {
		view.showJoinConfirmation();
	}

	public void leaveGroup(UsersGroup group) {
		UsersGroup userGroup = groupService.removeMember(group, view.getUser());
		view.updateMembershipState(userGroup);
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
		// TODO Auto-generated method stub

	}

	public boolean canJoin(UsersGroup group) {
		return !group.getMembers().contains(view.getUser());
	}

	public boolean canLeave(UsersGroup group) {
		if (group.getMembers().contains(view.getUser())) {
			if (group.getAdmins().contains(view.getUser())) {
				return group.getAdmins().size() != 1;
			}
			return true;
		}
		return false;
	}

	public boolean canManage(UsersGroup group) {
		return group.getAdmins().contains(view.getUser());
	}

	@Override
	public void openSingleEvent(Event event) {
		// TODO Auto-generated method stub

	}

	public List<fi.pss.cleanbeach.data.Event> getEvents(UsersGroup group) {
		return eventService.getEvents(group);
	}

	public void setAdmin(UsersGroup group, User u, Boolean value) {
		groupService.setAdminStatus(group, u, value);
	}

}
