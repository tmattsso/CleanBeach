/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;

import fi.pss.cleanbeach.ui.mvp.AbstractView;

/**
 * @author denis
 * 
 */
@UIScoped
public class GroupView extends AbstractView<GroupPresenter> {

    public GroupView() {
    }

    @Inject
    @Override
    public void injectPresenter(GroupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected ComponentContainer getMainContent() {
        // TODO Auto-generated method stub
        return null;
    }

}
