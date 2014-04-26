package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

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

public abstract class EventDetailsPresenter<T extends IEventDetails> extends
		CreateEventPresenter<T> {

	@Inject
	protected EventService service;

	@Inject
	protected AuthenticationService authService;

	@Inject
	protected InviteService inviteService;

	@Inject
	protected LocationService locService;

	public EventDetailsPresenter() {
		super();
	}

	public List<ThrashType> getThrashTypes() {
		return service.getThrashTypes();
	}

	public void setNumThrash(Integer value, ThrashType t, Event e) {
		service.setThrash(value, t, e, view.getUser());
	}

	public void openSingleEvent(Event event) {
		event = service.loadDetails(event);
		view.showDetails(event);
	}

	public void leaveEvent(Event e) {
		e = service.setUserJoined(e, view.getUser(), false);
		e = service.loadDetails(e);
		view.updateEventDetails(e);
	}

	public void joinEvent(Event e) {
		e = service.setUserJoined(e, view.getUser(), true);
		e = service.loadDetails(e);
		view.updateEventDetails(e);

	}

	public void openAddThrash(Event e) {
		view.openThrashDetails(e);
	}

	public void openAddComment(Event e) {
		view.openAddComment(e);
	}

	public void openInviteGroups(Event e) {
		// get existing invites, we don't want duplicates
		Collection<Invite> coll = inviteService.getPendingInvitations(e);
		User u = authService.refresh(view.getUser());

		view.openInviteGroups(coll, e, u);
	}

	public void navigatedFromThrash(fi.pss.cleanbeach.data.Event e) {
		// just update view
		e = service.loadDetails(e);
		view.updateEventDetails(e);
	}

	public void addOtherDesc(ThrashType t, String value, Event event) {
		locService.setDescription(t, view.getUser(), value, event, null);
	}

	public void addComment(Event e, String value, Image img) {
		service.addComment(e, value, img, view.getUser());
		e = service.loadDetails(e);
		view.navigateAndUpdate(e);
	}

	public void invite(UsersGroup g, Event event) {
		inviteService.invite(view.getUser(), g, event);
	}

	public void deleteEvent(fi.pss.cleanbeach.data.Event e) {
		service.delete(e);
		view.navigateBackAfterDelete(e.getId());
	}

	public void openEditEvent(fi.pss.cleanbeach.data.Event e) {
		view.openEditEvent(e);
	}
}