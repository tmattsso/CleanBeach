package fi.pss.cleanbeach.ui.views.group;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.util.ImageUtil;

public class GroupComponent extends CssLayout {

	private static final long serialVersionUID = -1618342523005670175L;

	public GroupComponent(final UsersGroup group,
			final GroupPresenter presenter, boolean isAdmin) {
		build(group, presenter, isAdmin);
	}

	public void build(final UsersGroup group, final GroupPresenter presenter,
			boolean isAdmin) {
		removeAllComponents();

		addStyleName("user-group");
		addLayoutClickListener(new LayoutClickListener() {

			private static final long serialVersionUID = -1380510126070557343L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				presenter.showGroup(group);
			}
		});

		Image logo = group.getLogo();
		boolean hasLogo = false;
		if (logo != null) {
			final byte[] content = logo.getContent();
			hasLogo = content != null && content.length > 0;
			if (hasLogo) {
				com.vaadin.ui.Image groupLogo = ImageUtil.getGroupLogo(group);
				groupLogo.addStyleName("logosmall");
				groupLogo.addStyleName("user-group-logo");
				addComponent(groupLogo);
				setHeight("125px");
			}
		}

		Label name = new Label(group.getName());
		name.addStyleName("user-group-name");
		addComponent(name);

		Label members = createMembersCount(group, presenter);
		members.addStyleName("user-group-members-count");
		addComponent(members);

		String eventInvitations = presenter.getEventInvitations(group);
		if (isAdmin && eventInvitations != null) {
			Label invitations = new Label(eventInvitations);
			if (hasLogo) {
				invitations.addStyleName("user-group-event-invitations");
			}
			addComponent(invitations);
		}
	}

	private Label createMembersCount(UsersGroup group, GroupPresenter presenter) {
		Label members = new Label(presenter.getMembers(group));
		return members;
	}

}
