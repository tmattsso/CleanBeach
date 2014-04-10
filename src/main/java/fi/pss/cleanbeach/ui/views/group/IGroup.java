/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.Collection;
import java.util.Set;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.views.eventdetails.IEventDetails;

/**
 * @author denis
 * 
 */
public interface IGroup extends IEventDetails {

	void showAdminGroups(Set<UsersGroup> groups);

	void showMemberGroups(Set<UsersGroup> groups);

	void showGroupDetails(UsersGroup group);

	void showLeaveConfirmation();

	void showJoinConfirmation();

	void updateMembershipState(UsersGroup userGroup);

	void showManageAdmins(UsersGroup group);

	void showMembersInGroup(UsersGroup group);

	void showInvitations(UsersGroup group, Collection<Invite> pendingInvitations);

	void updateGroupDetails(UsersGroup usersGroup);

	void showEditGroup(UsersGroup usersGroup);

	void showGroupSaveConfirmation(UsersGroup group);

	void backToMainAndReset();

	void showCreateEvent(UsersGroup group);

}
