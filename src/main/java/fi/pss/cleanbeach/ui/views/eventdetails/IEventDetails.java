package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.Collection;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.mvp.IView;

public interface IEventDetails extends IView {

	void showDetails(Event e);

	void openThrashDetails(Event e);

	void openEditEvent(Event e);

	void openAddComment(Event e);

	void updateEventDetails(Event e);

	void openInviteGroups(Collection<Invite> coll, Event e, User u);

	void navigateAndUpdate(Event e);

	void navigateBackAfterDelete(long eventId);

	void showCreateEvent(UsersGroup selectedGroup, Location selectedLocation);

	void navigateBackAfterEdit(Event saveEvent);
}
