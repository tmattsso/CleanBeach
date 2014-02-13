package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WallLayout extends VerticalLayout {

	private static final long serialVersionUID = -6558108321816615836L;

	public WallLayout(EventsPresenter presenter) {
	}

	public void update(List<fi.pss.cleanbeach.data.Event> events) {

		removeAllComponents();
		for (fi.pss.cleanbeach.data.Event e : events) {
			Label l = new Label(e.getLocation().getName() + ": "
					+ e.getDescription());
			addComponent(l);
		}

	}
}
