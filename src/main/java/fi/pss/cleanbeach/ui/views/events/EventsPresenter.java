package fi.pss.cleanbeach.ui.views.events;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.views.eventdetails.EventDetailsPresenter;

@UIScoped
public class EventsPresenter extends EventDetailsPresenter<IEvents> implements
		Serializable {

	private static final long serialVersionUID = 3951507517016979359L;

	protected EventsPresenter() {
	}

	@Override
	public void init(User currentUser) {

	}

	public void loadJoinedEvents() {
		List<Event> l = service.getJoinedEventsForUser(view.getUser());
		view.showJoinedEvents(l);

	}

	public void loadAllEvents() {
		List<Event> l = service.getEventsForUser(view.getUser(), null, null);
		view.showAllEvents(l);
	}

	public void searchForEvents(String value) {
		List<Event> l = service.searchForEvents(view.getUser(), value);
		view.populateSearchResults(l);
	}

	@Override
	public void createEvent(UsersGroup creator, String desc, Date start,
			Location loc) {
		// TODO Auto-generated method stub

	}

}
