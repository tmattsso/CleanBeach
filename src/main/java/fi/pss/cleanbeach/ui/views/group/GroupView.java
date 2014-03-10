/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.Set;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;

import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.mvp.AbstractView;

/**
 * @author denis
 * 
 */
@UIScoped
public class GroupView extends AbstractView<GroupPresenter> implements IGroup {

	private static final long serialVersionUID = 4837511726547892725L;

	private GroupsLayout groupsComponent;

	private GroupDetailsLayout detailsComponent;

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

}
