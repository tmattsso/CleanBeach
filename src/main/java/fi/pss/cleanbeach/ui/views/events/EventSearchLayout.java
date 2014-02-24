package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.MyTouchKitUI;

public class EventSearchLayout extends VerticalLayout {

	private static final long serialVersionUID = -7941395172864864360L;

	private final VerticalLayout resultContainer;

	private final EventsPresenter presenter;

	public EventSearchLayout(final EventsPresenter presenter) {

		this.presenter = presenter;

		setMargin(false);
		setSpacing(true);

		final TextField tf = new TextField();
		tf.setWidth("100%");
		tf.setInputPrompt("search here");
		addComponent(tf);

		Button search = new Button("Search");
		search.setClickShortcut(KeyCode.ENTER);
		search.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2109171941826669484L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.searchForEvents(tf.getValue(),
						MyTouchKitUI.getCurrentUser());
			}
		});
		addComponent(search);

		resultContainer = new VerticalLayout();
		addComponent(resultContainer);

	}

	public void populate(List<fi.pss.cleanbeach.data.Event> l) {
		resultContainer.removeAllComponents();

		if (l.isEmpty()) {

			Label empty = new Label("No events found.");
			empty.addStyleName("noevents");
			resultContainer.addComponent(empty);

			return;
		}

		for (fi.pss.cleanbeach.data.Event e : l) {
			resultContainer.addComponent(new EventPanel(e, presenter));
		}
	}

}
