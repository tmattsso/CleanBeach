package fi.pss.cleanbeach.ui.views.events;

import java.util.Collection;
import java.util.List;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.mvp.IView;

public interface IEvents extends IView {

	void showJoinedEvents(List<Event> l);

	void showAllEvents(List<Event> l);

	void showDetails(Event e);

	void populateSearchResults(List<Event> l);

	void navigateAndUpdate(Event e);

	void openThrashDetails(Event e);

	void openAddComment(Event e, boolean addImage);

	void updateEventDetails(Event e);

	void openInviteGroups(Collection<Invite> coll, Event e, User u);

}
