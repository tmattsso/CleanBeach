package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;

import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.mvp.AbstractView;

@UIScoped
public class EventsView extends AbstractView<EventsPresenter> implements
		IEvents {

	private static final long serialVersionUID = -259521650823470699L;

	private MainEventsLayout events;
	private EventDetailLayout details;

	public EventsView() {
		setCaption("Events");
		addStyleName("events");
	}

	@Override
	protected ComponentContainer getMainContent() {

		events = new MainEventsLayout(presenter);

		presenter.loadAllEvents(MyTouchKitUI.getCurrentUser());

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
	public void showDetails(fi.pss.cleanbeach.data.Event e) {
		navigateTo(details = new EventDetailLayout(e, presenter));
	}

	@Override
	public void populateSearchResults(List<fi.pss.cleanbeach.data.Event> l) {
		events.populateSearchResults(l);
	}

	@Override
	public void updateEventDetails(fi.pss.cleanbeach.data.Event e) {
		details.update(e);
	}

}
