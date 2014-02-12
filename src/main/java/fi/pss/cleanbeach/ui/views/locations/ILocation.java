package fi.pss.cleanbeach.ui.views.locations;

import java.util.Set;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.ui.mvp.IView;

public interface ILocation extends IView {

	void addLocations(Set<Location> locs);

	void updateMarker(Location selected);

	void selectMarker(Location l);

}
