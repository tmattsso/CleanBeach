package fi.pss.cleanbeach.ui.views.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WallLayout extends VerticalLayout {

	private static final long serialVersionUID = -6558108321816615836L;

	private final EventsPresenter presenter;

	private final Map<fi.pss.cleanbeach.data.Event, EventPanel> eventToPanel = new HashMap<>();

	public WallLayout(EventsPresenter presenter) {
		this.presenter = presenter;
		setMargin(false);
	}

	public void update(List<fi.pss.cleanbeach.data.Event> events) {

		removeAllComponents();
		eventToPanel.clear();

		if (events.isEmpty()) {

			Label empty = new Label("No events found.");
			empty.addStyleName("noevents");
			addComponent(empty);

			return;
		}

		for (fi.pss.cleanbeach.data.Event e : events) {
			EventPanel p = new EventPanel(e, presenter);
			addComponent(p);
			eventToPanel.put(e, p);
		}

	}

	public void update(fi.pss.cleanbeach.data.Event e) {
		if (eventToPanel.containsKey(e)) {
			eventToPanel.get(e).update(e);
		}
	}
}
