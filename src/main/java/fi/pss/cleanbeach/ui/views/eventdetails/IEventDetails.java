package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.Collection;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.User;

public interface IEventDetails extends ICreateEvent {

	void showDetails(Event e);

	void openThrashDetails(Event e);

	void openAddComment(Event e, boolean addImage);

	void updateEventDetails(Event e);

	void openInviteGroups(Collection<Invite> coll, Event e, User u);

	void navigateAndUpdate(Event e);

}
