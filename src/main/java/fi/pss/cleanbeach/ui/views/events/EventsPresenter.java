package fi.pss.cleanbeach.ui.views.events;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class EventsPresenter extends AbstractPresenter<IEvents> implements
		Serializable, IEventPresenter {

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

	@Override
	public void openSingleEvent(Event event) {
		event = service.loadDetails(event);
		view.showDetails(event);
	}

	public void searchForEvents(String value, User currentUser) {
		List<Event> l = service.searchForEvents(currentUser, value);
		view.populateSearchResults(l);
	}

	public void joinEvent(fi.pss.cleanbeach.data.Event e, User currentUser) {
		e = service.setUserJoined(e, currentUser, true);
		e = service.loadDetails(e);
		view.updateEventDetails(e);

	}

	public void leaveEvent(fi.pss.cleanbeach.data.Event e, User currentUser) {
		e = service.setUserJoined(e, currentUser, false);
		e = service.loadDetails(e);
		view.updateEventDetails(e);
	}

	public List<ThrashType> getThrashTypes() {
		return service.getThrashTypes();
	}

	public void setNumThrash(Integer value, ThrashType t, Event e,
			User currentUser) {
		service.setThrash(value, t, e, currentUser);
	}

	public void openAddThrash(Event e) {
		view.openThrashDetails(e);
	}
}
