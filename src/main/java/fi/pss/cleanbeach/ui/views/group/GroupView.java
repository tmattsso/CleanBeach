/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.ResourceService;
import fi.pss.cleanbeach.ui.mvp.AbstractView;

/**
 * @author denis
 * 
 */
@UIScoped
public class GroupView extends AbstractView<GroupPresenter> implements IGroup {

    @Inject
    private ResourceService resourceService;

    private CssLayout mainLayout;

    public GroupView() {
        setCaption("Groups");
    }

    @Inject
    @Override
    public void injectPresenter(GroupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected ComponentContainer getMainContent() {

        mainLayout = new CssLayout();
        mainLayout.setWidth(100, Unit.PERCENTAGE);
        mainLayout.addStyleName("groups");

        Label title = new Label(getMessage("Groups.view.title"));
        mainLayout.addComponent(title);
        title.addStyleName("groups-title");

        HorizontalLayout groupActionsLayout = new HorizontalLayout();
        groupActionsLayout.setWidth(100, Unit.PERCENTAGE);
        groupActionsLayout.addStyleName("groups-buttons");
        mainLayout.addComponent(groupActionsLayout);

        groupActionsLayout.addComponent(createSearchButton());
        groupActionsLayout.addComponent(createGroupButton());

        presenter.loadGroups();

        return mainLayout;
    }

    @Override
    public void showAdminGroups(Set<UsersGroup> groups) {
        if (!groups.isEmpty()) {
            Label adminTitle = new Label(getMessage("Groups.view.admin.label"));
            adminTitle.addStyleName("admin-title");
            mainLayout.addComponent(adminTitle);
            for (UsersGroup group : groups) {
                mainLayout.addComponent(createGroupComponent(group, true));
            }
        }
    }

    @Override
    public void showMemberGroups(Set<UsersGroup> groups) {
        if (!groups.isEmpty()) {
            Label memberTitle = new Label(
                    getMessage("Groups.view.member.label"));
            memberTitle.addStyleName("member-title");
            mainLayout.addComponent(memberTitle);
            for (UsersGroup group : groups) {
                mainLayout.addComponent(createGroupComponent(group, false));
            }
        }
    }

    private Component createGroupComponent(final UsersGroup group,
            boolean isAdmin) {
        CssLayout component = new CssLayout();
        component.addStyleName("user-group");
        component.addLayoutClickListener(new LayoutClickListener() {

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
            com.vaadin.ui.Image image = new com.vaadin.ui.Image();
            image.addStyleName("user-group-logo");
            StreamSource source = new StreamSource() {

                @Override
                public InputStream getStream() {
                    return new ByteArrayInputStream(content);
                }
            };
            StreamResource resource = new StreamResource(source, null);
            image.setSource(resource);
            component.addComponent(image);
        }

        if (isAdmin) {
            String eventInvitations = presenter.getEventsInvitations(group);
            if (eventInvitations != null) {
                Label invitations = new Label(eventInvitations);
                invitations.setSizeUndefined();
                invitations.addStyleName("user-group-event-invitations");
                component.addComponent(invitations);
            }
        }

        Label name = new Label(group.getName());
        name.setSizeUndefined();
        name.addStyleName("user-group-name");
        component.addComponent(name);

        Label members = createMembersCount(group);
        members.setSizeUndefined();
        members.addStyleName("user-group-members-count");
        if (hasLogo) {
            members.addStyleName("align-bottom");
        }
        component.addComponent(members);

        return component;
    }

    private Button createSearchButton() {
        Button button = new Button(getMessage("Groups.view.search.button"));
        button.addStyleName("search-button");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.searchGroups();
            }
        });
        return button;
    }

    private Button createGroupButton() {
        Button button = new Button(getMessage("Groups.view.create.button"));
        button.addStyleName("create-button");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.createGroup();
            }
        });
        return button;
    }

    private Label createMembersCount(UsersGroup group) {
        Label members = new Label(presenter.getMembers(group));
        return members;
    }

    private String getMessage(String key) {
        return resourceService.getMessage(getLocale(), key);
    }

}
