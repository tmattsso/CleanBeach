package fi.pss.cleanbeach.ui.views.locations;

import java.util.ArrayList;
import java.util.Set;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.ui.mvp.IView;

public interface ILocation extends IView {

	void addLocations(Set<Location> locs);

	void updateMarker(Location selected);

	void selectMarker(Location l);

	void showEvents(Location selected, ArrayList<Event> arrayList);

	void showCreateEvent(Location selected);

	void showTrends(Location selected, ArrayList<Event> arrayList);

	void showTrashInput(Location selected, ThrashDAO thrash);

}
