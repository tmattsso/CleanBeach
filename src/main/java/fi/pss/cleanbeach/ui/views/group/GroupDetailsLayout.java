package fi.pss.cleanbeach.ui.views.group;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.BaseTheme;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.UsersGroup;

/**
 * @author denis
 * 
 */
class GroupDetailsLayout extends NavigationView {

    private final GroupPresenter presenter;

    private UsersGroup group;

    private Component adminComponent;

    GroupDetailsLayout(final GroupPresenter presenter, final UsersGroup group) {
        this.presenter = presenter;
        this.group = group;

        setCaption(getMessage("Group.details.caption"));

        setRightButton(presenter, group);

        CssLayout mainLayout = new CssLayout();
        mainLayout.addStyleName("groupview-details");
        mainLayout.addComponent(createGroupInfoComponent());
        mainLayout.addComponent(createInvitationsInfo());
        if (presenter.canManage(group)) {
            adminComponent = createButtonsComponent();
            mainLayout.addComponent(adminComponent);
        }
        mainLayout.addComponent(createEventsComponent());

        setContent(mainLayout);
    }

    @Override
    public CssLayout getContent() {
        return (CssLayout) super.getContent();
    }

    public void showLeaveConfirmation() {
        ConfirmDialog.show(UI.getCurrent(),
                getMessage("Group.details.leave.message", group.getName()),
                new ConfirmDialog.Listener() {

                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            presenter.leaveGroup(group);
                        }
                    }
                });
    }

    public void showJoinConfirmation() {
        ConfirmDialog.show(UI.getCurrent(),
                getMessage("Group.details.join.message", group.getName()),
                new ConfirmDialog.Listener() {

                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            presenter.joinGroup(group);
                        }
                    }
                });
    }

    public void updateMembershipState(UsersGroup group) {
        this.group = group;
        if (!presenter.canManage(group) && adminComponent != null) {
            getContent().removeComponent(adminComponent);
        }
        setRightButton(presenter, group);
    }

    private Component createInvitationsInfo() {
        CssLayout layout = new CssLayout();
        layout.addStyleName("groupview-details-invitations");

        Label prefix = new Label(getMessage("Group.details.invitations.prefix"));
        prefix.setSizeUndefined();
        prefix.addStyleName("invitations-prefix");
        layout.addComponent(prefix);

        String link = presenter.getPendingEventInvitations(group);
        Button invitations = new Button(link);
        invitations.setStyleName(BaseTheme.BUTTON_LINK);
        invitations.addStyleName("invitations-link");
        layout.addComponent(invitations);

        return layout;
    }

    private Component createEventsComponent() {
        CssLayout layout = new CssLayout();
        layout.addStyleName("groupview-details-events");

        Label header = new Label(getMessage("Group.details.events.caption"));
        header.addStyleName("groupview-details-events-caption");
        layout.addComponent(header);

        List<fi.pss.cleanbeach.data.Event> events = group.getEvents();
        for (fi.pss.cleanbeach.data.Event event : events) {
            layout.addComponent(createEventComponent(event));
        }

        return layout;
    }

    private Component createEventComponent(fi.pss.cleanbeach.data.Event event) {
        CssLayout layout = new CssLayout();
        layout.addStyleName("groupview-details-event");
        // TODO
        return layout;
    }

    private Component createButtonsComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.addStyleName("groupview-details-admin-buttons");

        Button createButton = new Button(
                getMessage("Group.details.create.event"));
        createButton.addStyleName("group-details-create-event");
        createButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.createEvent();
            }
        });
        layout.addComponent(createButton);

        Button manage = new Button(getMessage("Group.details.manage.admin"));
        manage.addStyleName("group-details-manage-admins");
        manage.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.showManageAdmins();
            }
        });
        layout.addComponent(manage);
        return layout;
    }

    private void setRightButton(final GroupPresenter presenter,
            final UsersGroup group) {
        if (presenter.canJoin(group)) {
            getNavigationBar().setRightComponent(
                    createJoinButton(presenter, group));
        } else if (presenter.canLeave(group)) {
            getNavigationBar().setRightComponent(
                    createLeaveButton(presenter, group));
        } else {
            getNavigationBar().setRightComponent(null);
        }
    }

    private Component createGroupInfoComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.addStyleName("group-details-info");
        CssLayout details = new CssLayout();
        details.addStyleName("group-details-text-info");
        layout.addComponent(details);
        layout.setExpandRatio(details, 1);

        Label name = new Label(group.getName());
        name.addStyleName("group-details-name");
        details.addComponent(name);

        Label description = new Label(group.getDescription());
        description.addStyleName("group-details-description");
        details.addComponent(description);

        CssLayout rightLayout = new CssLayout();
        rightLayout.addStyleName("group-details-right-panel");
        layout.addComponent(rightLayout);
        Image logo = group.getLogo();
        if (logo != null && logo.getContent() != null
                && logo.getContent().length > 0) {
            rightLayout.addComponent(GroupsLayout.createLogoComponent(logo
                    .getContent()));
        }

        String members = presenter.getMembers(group);
        Button membersButton = new Button(members);
        membersButton.setStyleName(BaseTheme.BUTTON_LINK);
        membersButton.addStyleName("group-details-members");
        membersButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.showMembers(group);
            }
        });
        rightLayout.addComponent(membersButton);

        return layout;
    }

    private Button createLeaveButton(final GroupPresenter presenter,
            final UsersGroup group) {
        Button button = new Button(getMessage("Group.details.leave.group"));
        button.setStyleName(BaseTheme.BUTTON_LINK);
        button.addStyleName("groupview-details-leave");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.requestLeaveGroup(group);
            }
        });
        return button;
    }

    private Component createJoinButton(final GroupPresenter presenter,
            final UsersGroup group) {
        Button button = new Button(getMessage("Group.details.join.group"));
        button.setStyleName(BaseTheme.BUTTON_LINK);
        button.addStyleName("groupview-details-join");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.requestJoinGroup(group);
            }
        });
        return button;
    }

    private String getMessage(String key, Object... params) {
        return presenter.getMessage(key, params);
    }

}
