package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.MyTouchKitUI;

public class MainEventsLayout extends NavigationView implements ClickListener {

	private static final long serialVersionUID = -5316615188726137687L;

	private final Button allEvents;
	private final Button joinedEvents;
	private final Button search;
	private final CssLayout content;

	private WallLayout allEventsLayout;
	private WallLayout joinedEventsLayout;
	private EventSearchLayout searchLayout;

	private final EventsPresenter presenter;

	public MainEventsLayout(EventsPresenter presenter) {

		this.presenter = presenter;

		setCaption("Events");

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(false);

		Toolbar tabs = new Toolbar();

		allEvents = new Button("All events");
		allEvents.addClickListener(this);
		allEvents.setData(allEventsLayout = new WallLayout(presenter));
		allEvents.addStyleName("selected");
		tabs.addComponent(allEvents);

		joinedEvents = new Button("Joined events");
		joinedEvents.addClickListener(this);
		joinedEvents.setData(joinedEventsLayout = new WallLayout(presenter));
		tabs.addComponent(joinedEvents);

		search = new Button("Search");
		search.addClickListener(this);
		search.setData(searchLayout = new EventSearchLayout(presenter));
		tabs.addComponent(search);

		content = new CssLayout((Component) allEvents.getData());
		content.setSizeFull();

		vl.addComponent(tabs);
		vl.addComponent(content);
		vl.setExpandRatio(content, 1);

		setContent(vl);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		allEvents.removeStyleName("selected");
		joinedEvents.removeStyleName("selected");
		search.removeStyleName("selected");

		content.removeAllComponents();
		content.addComponent((Component) event.getButton().getData());
		event.getButton().addStyleName("selected");

		if (event.getButton() == allEvents) {
			presenter.loadAllEvents(MyTouchKitUI.getCurrentUser());
		}
		if (event.getButton() == joinedEvents) {
			presenter.loadJoinedEvents(MyTouchKitUI.getCurrentUser());
		}

	}

	@Override
	protected void onBecomingVisible() {
		super.onBecomingVisible();

		// clear navigation data
		getNavigationManager().setPreviousComponent(null);
	}

	public void showJoinedEvents(List<fi.pss.cleanbeach.data.Event> l) {
		joinedEventsLayout.update(l);
	}

	public void showAllEvents(List<fi.pss.cleanbeach.data.Event> l) {
		allEventsLayout.update(l);
	}

	public void populateSearchResults(List<fi.pss.cleanbeach.data.Event> l) {
		searchLayout.populate(l);
	}
}