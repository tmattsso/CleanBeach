/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.Set;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;

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

    private GroupsLayout groupsComponent;

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
    public void showGroupDetails(UsersGroup group, boolean adminView) {
        navigateTo(new GroupDetailsLayout(presenter, group, adminView));
    }

    @Override
    protected ComponentContainer getMainContent() {
        groupsComponent = new GroupsLayout(presenter);
        presenter.loadGroups();
        return groupsComponent;
    }

}
