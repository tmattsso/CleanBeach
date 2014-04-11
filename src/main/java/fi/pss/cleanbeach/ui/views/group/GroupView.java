/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Notification;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.eventdetails.EventDetailsCapableView;

/**
 * @author denis
 * 
 */
@UIScoped
public class GroupView extends EventDetailsCapableView<GroupPresenter>
		implements IGroup {

	private static final long serialVersionUID = 4837511726547892725L;

	private GroupsLayout groupsComponent;
	protected GroupDetailsLayout detailsComponent;

	public GroupView() {
		setCaption("Groups");
		addStyleName("groups");
	}

	@Inject
	@Override
	public void injectPresenter(GroupPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showAdminGroups(Set<UsersGroup> groups) {
		groupsComponent.showAdminGroups(groups);
	}

	@Override
	public void showMemberGroups(Set<UsersGroup> groups) {
		groupsComponent.showMemberGroups(groups);
	}

	@Override
	public void showGroupDetails(UsersGroup group) {

		Component current = getCurrentComponent();
		if (current instanceof SearchGroupsLayout) {
			// keep history
		} else {
			setPreviousComponent(null);
			setNextComponent(null);
			setCurrentComponent(groupsComponent);
		}
		detailsComponent = new GroupDetailsLayout(presenter, group);
		navigateTo(detailsComponent);
	}

	@Override
	protected ComponentContainer getMainContent() {
		groupsComponent = new GroupsLayout(presenter);
		presenter.loadGroups();
		return groupsComponent;
	}

	@Override
	public void showLeaveConfirmation() {
		detailsComponent.showLeaveConfirmation();
	}

	@Override
	public void showJoinConfirmation() {
		detailsComponent.showJoinConfirmation();
	}

	@Override
	public void updateMembershipState(UsersGroup group) {
		detailsComponent.updateMembershipState(group);
	}

	@Override
	public void showManageAdmins(UsersGroup group) {
		navigateTo(new ManageAdminsLayout(group, presenter));
	}

	@Override
	public void showMembersInGroup(UsersGroup group) {
		navigateTo(new GroupMembersLayout(group, presenter));
	}

	@Override
	public void showInvitations(UsersGroup group,
			Collection<Invite> pendingInvitations) {

		navigateTo(new InvitationsLayout(pendingInvitations, presenter));
	}

	@Override
	public void updateGroupDetails(UsersGroup group) {
		groupsComponent.update(group);
		detailsComponent.build();
	}

	@Override
	public void showEditGroup(UsersGroup usersGroup) {
		navigateTo(new EditGroupLayout(usersGroup, presenter));
	}

	@Override
	public void showGroupSaveConfirmation(UsersGroup group) {
		Notification.show(Lang.get("Group.edit.created", group.getName()));
	}

	@Override
	public void backToMainAndReset() {
		setPreviousComponent(null);
		setNextComponent(null);
		setCurrentComponent(groupsComponent);
		groupsComponent.build();
	}

	@Override
	public void showGroupSearch() {
		navigateTo(new SearchGroupsLayout(presenter));
	}

	@Override
	public void navigateBackAfterDelete(long eventId) {
		if (detailsComponent != null) {
			navigateBack();
			detailsComponent.remove(eventId);
		}
	}

}
