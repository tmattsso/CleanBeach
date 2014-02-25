package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.ui.mvp.IView;

public interface IEvents extends IView {

	void showJoinedEvents(List<Event> l);

	void showAllEvents(List<Event> l);

	void showDetails(Event e);

	void populateSearchResults(List<Event> l);

	void updateEventDetails(Event e);

	void openThrashDetails(Event e);

	void openAddComment(Event e, boolean addImage);

}
