package fi.pss.cleanbeach.ui.views.locations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.ui.views.eventdetails.IEventDetails;

public interface ILocation extends IEventDetails {

	void addLocations(Set<Location> locs);

	void updateMarker(Location selected);

	void selectMarker(Location l);

	void showEvents(Location selected, Collection<Event> collection);

	void showTrends(Location selected, ArrayList<Event> arrayList);

	void showTrashInput(Location selected, ThrashDAO thrash);

	void updateEventList(Location loc, Event e);

	void showLocCreate(Double lat, Double lon);

}
