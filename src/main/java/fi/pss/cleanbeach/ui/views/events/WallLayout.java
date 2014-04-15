package fi.pss.cleanbeach.ui.views.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.addon.touchkit.extensions.Geolocator;
import com.vaadin.addon.touchkit.extensions.PositionCallback;
import com.vaadin.addon.touchkit.gwt.client.vcom.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.util.Lang;

public class WallLayout extends VerticalLayout implements PositionCallback {

	private static final long serialVersionUID = -6558108321816615836L;

	private final EventsPresenter presenter;

	private final Map<fi.pss.cleanbeach.data.Event, EventPanel> eventToPanel = new HashMap<>();

	private boolean positioningHasBeenRun;

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

	@Override
	public void onSuccess(Position position) {
		presenter
				.loadAllEvents(position.getLatitude(), position.getLongitude());
	}

	@Override
	public void onFailure(int errorCode) {
		presenter.loadAllEvents(null, null);
	}

	public void runPositioning() {
		if (!positioningHasBeenRun) {

			removeAllComponents();
			Label running = new Label("[updating list...");
			addComponent(running);
			setComponentAlignment(running, Alignment.MIDDLE_CENTER);

			Geolocator.detect(this);
			positioningHasBeenRun = true;
		}
	}
}
