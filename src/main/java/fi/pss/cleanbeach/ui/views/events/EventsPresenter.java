package fi.pss.cleanbeach.ui.views.events;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class EventsPresenter extends AbstractPresenter<IEvents> implements
		Serializable {

	private static final long serialVersionUID = 3951507517016979359L;

	@Inject
	private EventService service;

	protected EventsPresenter() {
	}

	@Override
	public void init(User currentUser) {

	}

	public void loadJoinedEvents(User currentUser) {
		List<Event> l = service.getJoinedEventsForUser(currentUser);
		view.showJoinedEvents(l);

	}

	public void loadAllEvents(User currentUser) {
		List<Event> l = service.getEventsForUser(currentUser, null, null);
		view.showAllEvents(l);
	}

	public void openSingleEvent(Event event) {
		event = service.loadDetails(event);
		view.showDetails(event);
	}
}
