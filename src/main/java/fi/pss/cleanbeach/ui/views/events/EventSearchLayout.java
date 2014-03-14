package fi.pss.cleanbeach.ui.views.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.util.Lang;

public class EventSearchLayout extends VerticalLayout {

	private static final long serialVersionUID = -7941395172864864360L;

	private final VerticalLayout resultContainer;
	private final Map<fi.pss.cleanbeach.data.Event, EventPanel> eventToPanel = new HashMap<>();

	private final EventsPresenter presenter;

	public EventSearchLayout(final EventsPresenter presenter) {

		this.presenter = presenter;

		setMargin(false);
		setSpacing(true);

		final TextField tf = new TextField();
		tf.setWidth("100%");
		tf.setInputPrompt(Lang.get("events.search.searchprompt"));
		addComponent(tf);

		Button search = new Button(Lang.get("events.search.searchbutton"));
		search.setClickShortcut(KeyCode.ENTER);
		search.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2109171941826669484L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.searchForEvents(tf.getValue());
			}
		});
		addComponent(search);

		resultContainer = new VerticalLayout();
		addComponent(resultContainer);

	}

	public void populate(List<fi.pss.cleanbeach.data.Event> l) {
		resultContainer.removeAllComponents();

		if (l.isEmpty()) {

			Label empty = new Label(Lang.get("events.search.nothingfound"));
			empty.addStyleName("noevents");
			resultContainer.addComponent(empty);

			return;
		}

		for (fi.pss.cleanbeach.data.Event e : l) {
			EventPanel panel = new EventPanel(e, presenter);
			resultContainer.addComponent(panel);
			eventToPanel.put(e, panel);
		}
	}

	public void update(fi.pss.cleanbeach.data.Event e) {
		if (eventToPanel.containsKey(e)) {
			eventToPanel.get(e).update(e);
		}
	}

}
