/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.Collection;
import java.util.Collections;

import com.vaadin.ui.Component;

import fi.pss.cleanbeach.ui.views.events.AbstractEventPanel;

/**
 * @author denis
 * 
 */
public class EventComponent extends AbstractEventPanel<GroupPresenter> {

    public EventComponent(fi.pss.cleanbeach.data.Event e,
            GroupPresenter presenter) {
        super(e, presenter);
    }

    @Override
    protected Collection<? extends Component> createContent(
            fi.pss.cleanbeach.data.Event e) {
        return Collections.singleton(createCollectedComponent(e));
    }

}
