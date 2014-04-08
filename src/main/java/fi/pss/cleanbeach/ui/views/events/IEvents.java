package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.ui.views.eventdetails.IEventDetails;

public interface IEvents extends IEventDetails {

	void showJoinedEvents(List<Event> l);

	void showAllEvents(List<Event> l);

	void populateSearchResults(List<Event> l);

}
