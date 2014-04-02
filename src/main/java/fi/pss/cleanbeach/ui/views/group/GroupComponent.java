package fi.pss.cleanbeach.ui.views.group;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.UsersGroup;

public class GroupComponent extends CssLayout {

	public GroupComponent(final UsersGroup group,
			final GroupPresenter presenter, boolean isAdmin) {
		build(group, presenter, isAdmin);
		;
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
			addComponent(createLogoComponent(content));
		}

		if (isAdmin) {
			String eventInvitations = presenter.getEventInvitations(group);
			if (eventInvitations != null) {
				Label invitations = new Label(eventInvitations);
				invitations.setSizeUndefined();
				invitations.addStyleName("user-group-event-invitations");
				addComponent(invitations);
			}
		}

		Label name = new Label(group.getName());
		name.setSizeUndefined();
		name.addStyleName("user-group-name");
		addComponent(name);

		Label members = createMembersCount(group, presenter);
		members.setSizeUndefined();
		members.addStyleName("user-group-members-count");
		if (hasLogo) {
			members.addStyleName("align-bottom");
		}
		addComponent(members);
	}

	private Label createMembersCount(UsersGroup group, GroupPresenter presenter) {
		Label members = new Label(presenter.getMembers(group));
		return members;
	}

	static com.vaadin.ui.Image createLogoComponent(final byte[] content) {
		com.vaadin.ui.Image image = new com.vaadin.ui.Image();
		image.addStyleName("user-group-logo");
		StreamSource source = new StreamSource() {

			private static final long serialVersionUID = 158820412989991373L;

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(content);
			}
		};
		StreamResource resource = new StreamResource(source, null);
		image.setSource(resource);
		return image;
	}

}
