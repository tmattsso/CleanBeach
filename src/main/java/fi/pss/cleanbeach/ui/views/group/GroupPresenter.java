/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.io.Serializable;

import javax.inject.Inject;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.GroupService;
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

    public String getEventsInvitations(UsersGroup group) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMembers(UsersGroup group) {
        // TODO Auto-generated method stub
        return group.getMembers().size() + " members";
    }

    public void showGroup(UsersGroup group) {
        // TODO Auto-generated method stub

    }

    public void searchGroups() {
        // TODO Auto-generated method stub

    }

    public void createGroup() {
        // TODO Auto-generated method stub

    }

}
