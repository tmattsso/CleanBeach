package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import com.vaadin.ui.VerticalLayout;

public class WallLayout extends VerticalLayout {

	private static final long serialVersionUID = -6558108321816615836L;

	private final EventsPresenter presenter;

	public WallLayout(EventsPresenter presenter) {
		this.presenter = presenter;
		setMargin(true);
	}

	public void update(List<fi.pss.cleanbeach.data.Event> events) {

		removeAllComponents();
		for (fi.pss.cleanbeach.data.Event e : events) {
			EventPanel p = new EventPanel(e, presenter);
			addComponent(p);
		}

	}
}
