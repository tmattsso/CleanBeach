package fi.pss.cleanbeach.ui.views.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.util.Lang;

public class WallLayout extends VerticalLayout {

	private static final long serialVersionUID = -6558108321816615836L;

	private final EventsPresenter presenter;

	private final Map<fi.pss.cleanbeach.data.Event, EventPanel> eventToPanel = new HashMap<>();

	public WallLayout(EventsPresenter presenter) {
		this.presenter = presenter;
		setMargin(false);
		setSpacing(true);
	}

	public void update(List<fi.pss.cleanbeach.data.Event> events) {

		removeAllComponents();
		eventToPanel.clear();

		if (events.isEmpty()) {

			Label empty = new Label(Lang.get("events.wall.nonefound"));
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

	public void remove(long eventId) {
		for (fi.pss.cleanbeach.data.Event e : eventToPanel.keySet()) {
			if (e.getId() == eventId) {
				removeComponent(eventToPanel.get(e));
				break;
			}
		}
	}
}
