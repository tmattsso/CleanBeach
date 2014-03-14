package fi.pss.cleanbeach.ui.views.events;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.services.InviteService;
import fi.pss.cleanbeach.services.LocationService;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class EventsPresenter extends AbstractPresenter<IEvents> implements
		Serializable, IEventPresenter {

	private static final long serialVersionUID = 3951507517016979359L;

	@Inject
	private EventService service;

	@Inject
	private LocationService locService;

	@Inject
	private InviteService inviteService;

	@Inject
	private AuthenticationService authService;

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

	@Override
	public void openSingleEvent(Event event) {
		event = service.loadDetails(event);
		view.showDetails(event);
	}

	public void searchForEvents(String value) {
		List<Event> l = service.searchForEvents(view.getUser(), value);
		view.populateSearchResults(l);
	}

	public void joinEvent(Event e) {
		e = service.setUserJoined(e, view.getUser(), true);
		e = service.loadDetails(e);
		view.updateEventDetails(e);

	}

	public void leaveEvent(Event e) {
		e = service.setUserJoined(e, view.getUser(), false);
		e = service.loadDetails(e);
		view.updateEventDetails(e);
	}

	public List<ThrashType> getThrashTypes() {
		return service.getThrashTypes();
	}

	public void setNumThrash(Integer value, ThrashType t, Event e) {
		service.setThrash(value, t, e, view.getUser());
	}

	public void openAddThrash(Event e) {
		view.openThrashDetails(e);
	}

	public void openAddComment(boolean addImage, Event e) {
		view.openAddComment(e, addImage);
	}

	public void addComment(Event e, String value, Image img) {
		service.addComment(e, value, img, view.getUser());
		e = service.loadDetails(e);
		view.navigateAndUpdate(e);
	}

	public void navigatedFromThrash(fi.pss.cleanbeach.data.Event e) {
		// just update view
		e = service.loadDetails(e);
		view.updateEventDetails(e);
	}

	public void addOtherDesc(ThrashType t, String value, Event event) {
		locService.setDescription(t, view.getUser(), value, event, null);
	}

	public void openInviteGroups(Event e) {
		// get existing invites, we don't want duplicates
		Collection<Invite> coll = inviteService.getPendingInvitations(e);
		User u = authService.refresh(view.getUser());

		view.openInviteGroups(coll, e, u);
	}

	public void invite(UsersGroup g, Event event) {
		inviteService.invite(view.getUser(), g, event);
	}
}
