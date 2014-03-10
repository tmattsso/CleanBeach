package fi.pss.cleanbeach.ui.views.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class EventPanel extends AbstractEventPanel<EventsPresenter> {

	private static final long serialVersionUID = -6402313439815158102L;

	public EventPanel(fi.pss.cleanbeach.data.Event e, EventsPresenter presenter) {
		super(e, presenter);
	}

	@Override
	protected Collection<? extends Component> createContent(
			fi.pss.cleanbeach.data.Event e) {
		List<Component> result = new ArrayList<>(2);
		// group logo
		HorizontalLayout hl = new HorizontalLayout();
		result.add(hl);

		result.add(createCollectedComponent(e));
		return result;
	}

}
