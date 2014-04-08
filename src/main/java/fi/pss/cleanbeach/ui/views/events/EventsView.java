package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;

import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.eventdetails.EventDetailsCapableView;

@UIScoped
public class EventsView extends EventDetailsCapableView<EventsPresenter>
		implements IEvents {

	private static final long serialVersionUID = -259521650823470699L;

	private MainEventsLayout events;

	public EventsView() {
		addStyleName("events");
	}

	@Override
	protected ComponentContainer getMainContent() {

		setCaption(Lang.get("events.caption"));
		events = new MainEventsLayout(presenter);

		presenter.loadAllEvents();

		return events;
	}

	@Override
	@Inject
	public void injectPresenter(EventsPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showJoinedEvents(List<fi.pss.cleanbeach.data.Event> l) {
		events.showJoinedEvents(l);
	}

	@Override
	public void showAllEvents(List<fi.pss.cleanbeach.data.Event> l) {
		events.showAllEvents(l);
	}

	@Override
	public void populateSearchResults(List<fi.pss.cleanbeach.data.Event> l) {
		events.populateSearchResults(l);
	}

}
