/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.cdi.UIScoped;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.MyTouchKitUI;
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
		setPreviousComponent(null);
		setNextComponent(null);
		setCurrentComponent(groupsComponent);
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

	@Override
	public void showInvitations(UsersGroup group,
			Collection<Invite> pendingInvitations) {

		Popover pop = new Popover();
		pop.center();
		pop.setWidth("80%");
		pop.setHeight("80%");
		pop.addStyleName("groupinvitations");

		GridLayout content = new GridLayout(2, 1);
		pop.setContent(content);

		content.setSpacing(true);
		content.setMargin(true);
		content.setWidth("100%");
		content.setColumnExpandRatio(0, 1);

		Label caption = new Label(Lang.get("Groups.view.invitations.caption"));
		caption.addStyleName("caption");
		content.addComponent(caption, 0, 0, 1, 0);

		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		for (final Invite i : pendingInvitations) {

			Label invite = new Label();
			invite.setContentMode(ContentMode.HTML);
			invite.setValue(i.getEvent().getLocation().getName() + " "
					+ df.format(i.getEvent().getStart()));
			invite.addStyleName("user");
			content.addComponent(invite);
			content.setComponentAlignment(invite, Alignment.MIDDLE_LEFT);

			final Switch s = new Switch();
			s.setSizeUndefined();
			s.setValue(i.isAccepted());
			s.setImmediate(true);
			s.addValueChangeListener(new ValueChangeListener() {

				private static final long serialVersionUID = 2319743848498697115L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					presenter.changeInvite(i, s.getValue());
				}
			});
			content.addComponent(s);
		}

		pop.showRelativeTo(this);
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

}
