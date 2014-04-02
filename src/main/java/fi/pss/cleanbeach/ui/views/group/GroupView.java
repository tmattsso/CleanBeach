/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.cdi.UIScoped;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.mvp.AbstractView;
import fi.pss.cleanbeach.ui.util.Lang;

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

	@Override
	public void showManageAdmins(UsersGroup group) {
		navigateTo(new ManageAdminsLayout(group, presenter));
	}

	@Override
	public void showMembersInGroup(UsersGroup group) {
		Popover pop = new Popover();
		pop.center();
		pop.setWidth("80%");
		pop.setHeight("80%");
		pop.addStyleName("groupmembers");

		VerticalLayout content = new VerticalLayout();
		pop.setContent(content);

		content.setSpacing(true);
		content.setMargin(true);

		Label caption = new Label(
				Lang.get("Groups.view.members.membersingroup"));
		caption.addStyleName("caption");
		content.addComponent(caption);

		List<User> users = new ArrayList<>(group.getMembers());
		Collections.sort(users);
		boolean isAdmin = group.getAdmins().contains(
				MyTouchKitUI.getCurrentUser());
		for (User u : users) {

			Label user = new Label();
			if (isAdmin) {
				user.setContentMode(ContentMode.HTML);
				user.setValue(u.getName() + " <span>(" + u.getEmail()
						+ ")</span>");
			} else {
				user.setValue(u.getName());
			}
			user.addStyleName("user");
			content.addComponent(user);
		}

		pop.showRelativeTo(this);
	}

}
