/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.io.Serializable;

import javax.inject.Inject;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.GroupService;
import fi.pss.cleanbeach.services.InviteService;
import fi.pss.cleanbeach.services.ResourceService;
import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

/**
 * @author denis
 * 
 */
public class GroupPresenter extends AbstractPresenter<IGroup> implements
        Serializable {

    @Inject
    private GroupService groupService;

    @Inject
    private ResourceService resourceService;

    @Inject
    private InviteService inviteService;

    @Override
    public void init(User currentUser) {
        // TODO Auto-generated method stub

    }

    public void loadGroups() {
        User user = MyTouchKitUI.getCurrentUser();
        if (user != null) {
            view.showAdminGroups(groupService.getAdminGroups(user));
            view.showMemberGroups(groupService.getMemberGroups(user));
        }
    }

    public String getEventInvitations(UsersGroup group) {
        int size = inviteService.getPendingInvitations(group).size();
        if (size == 1) {
            return getMessage("Groups.view.events.amount.singular", size);
        }
        return getMessage("Groups.view.events.amount.plural", size);
    }

    public String getPendingEventInvitations(UsersGroup group) {
        int size = inviteService.getPendingInvitations(group).size();
        if (size == 1) {
            return getMessage("Groups.view.pending.events.amount.singular",
                    size);
        }
        return getMessage("Groups.view.pending.events.amount.plural", size);
    }

    public String getMembers(UsersGroup group) {
        if (group.getMembers().size() == 1) {
            return getMessage("Groups.view.members.amount.singular", group
                    .getMembers().size());
        }
        return getMessage("Groups.view.members.amount.plural", group
                .getMembers().size());
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
        UsersGroup userGroup = groupService.removeMember(group,
                MyTouchKitUI.getCurrentUser());
        view.updateMembershipState(userGroup);
    }

    public void joinGroup(UsersGroup group) {
        UsersGroup userGroup = groupService.addMember(group,
                MyTouchKitUI.getCurrentUser());
        view.updateMembershipState(userGroup);
    }

    public String getMessage(String key, Object... params) {
        return resourceService.getMessage(view.getLocale(), key, params);
    }

    public void createEvent() {
        // TODO Auto-generated method stub

    }

    public void showManageAdmins() {
        // TODO Auto-generated method stub

    }

    public void showMembers(UsersGroup group) {
        // TODO Auto-generated method stub

    }

    public boolean canJoin(UsersGroup group) {
        return !group.getMembers().contains(MyTouchKitUI.getCurrentUser());
    }

    public boolean canLeave(UsersGroup group) {
        if (group.getMembers().contains(MyTouchKitUI.getCurrentUser())) {
            if (group.getAdmins().contains(MyTouchKitUI.getCurrentUser())) {
                return group.getAdmins().size() != 1;
            }
            return true;
        }
        return false;
    }

    public boolean canManage(UsersGroup group) {
        return group.getAdmins().contains(MyTouchKitUI.getCurrentUser());
    }

}
