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
				addComponent(createLogoComponent(group.getName(), content,
						logo.getMimetype()));
				setHeight("175px");
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

	static com.vaadin.ui.Image createLogoComponent(String groupName,
			final byte[] content, String mime) {
		com.vaadin.ui.Image image = new com.vaadin.ui.Image();
		image.addStyleName("user-group-logo");
		StreamSource source = new StreamSource() {

			private static final long serialVersionUID = 158820412989991373L;

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(content);
			}
		};
		String filename = groupName;
		if (mime.contains("png")) {
			filename += ".png";
		} else {
			filename += ".jpg";
		}
		StreamResource resource = new StreamResource(source, filename);
		resource.setMIMEType(mime);
		resource.setCacheTime(1000 * 60 * 60 * 24 * 7);// 7 days
		image.setSource(resource);
		return image;
	}

}
