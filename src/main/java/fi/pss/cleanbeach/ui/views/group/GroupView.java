/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.mvp.AbstractView;

/**
 * @author denis
 * 
 */
@UIScoped
public class GroupView extends AbstractView<GroupPresenter> {

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
        CssLayout mainLayout = new CssLayout();
        mainLayout.setWidth(100, Unit.PERCENTAGE);
        mainLayout.addStyleName("groups");

        Label title = new Label(
                "Groups allow you to find more events and create events yourself. You can join any group, don't be shy!");
        mainLayout.addComponent(title);
        title.addStyleName("groups-title");

        HorizontalLayout groupActionsLayout = new HorizontalLayout();
        groupActionsLayout.setWidth(100, Unit.PERCENTAGE);
        groupActionsLayout.addStyleName("groups-buttons");
        mainLayout.addComponent(groupActionsLayout);

        groupActionsLayout.addComponent(createSearchButton());
        groupActionsLayout.addComponent(createGroupButton());

        Collection<? extends Component> adminComponents = getAdminComponents();
        if (!adminComponents.isEmpty()) {
            Label adminTitle = new Label(
                    "You are an admin of the following groups:");
            adminTitle.addStyleName("admin-title");
            mainLayout.addComponent(adminTitle);
            for (Component component : adminComponents) {
                mainLayout.addComponent(component);
            }
        }

        Collection<? extends Component> memberComponents = getMemberComponents();
        if (!memberComponents.isEmpty()) {
            Label memberTitle = new Label(
                    "You are a member of the following groups:");
            memberTitle.addStyleName("admin-title");
            mainLayout.addComponent(memberTitle);

            for (Component component : memberComponents) {
                mainLayout.addComponent(component);
            }
        }

        return mainLayout;
    }

    private Collection<? extends Component> getMemberComponents() {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }

    private Collection<? extends Component> getAdminComponents() {
        // TODO Auto-generated method stub
        return Collections.emptyList();
    }

    private Component createGroupComponent(UsersGroup group) {
        // TODO : create component for group
        return null;
    }

    private Button createSearchButton() {
        Button button = new Button("Search for Groups");
        button.addStyleName("search-button");
        // TOOD : add listener and forward event to presenter
        return button;
    }

    private Button createGroupButton() {
        Button button = new Button("Create a Group");
        button.addStyleName("create-button");
        // TOOD : add listener and forward event to presenter
        return button;
    }
}
