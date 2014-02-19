/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.Locale;
import java.util.Set;

import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.mvp.IView;

/**
 * @author denis
 * 
 */
public interface IGroup extends IView {

    void showAdminGroups(Set<UsersGroup> groups);

    void showMemberGroups(Set<UsersGroup> groups);

    Locale getLocale();

    void showGroupDetails(UsersGroup group);

    void showLeaveConfirmation();

    void showJoinConfirmation();

    void updateMembershipState(UsersGroup userGroup);

}
