package fi.pss.cleanbeach.ui.views.locations;

import java.util.Set;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.Location.STATUS;
import fi.pss.cleanbeach.services.LocationService;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class LocationPresenter extends AbstractPresenter<ILocation> {

	@Inject
	private LocationService locService;

	@Override
	public void init() {

	}

	public void addLocation(double latitude, double longitude, String name) {
		Location l = locService.createLocation(latitude, longitude, name);
		log.info("Created loc: " + l.getId());

		view.updateMarker(l);
		view.selectMarker(l);
	}

	public void showEvents(Location selected) {
		// TODO Auto-generated method stub

	}

	public void createEvent(Location selected) {
		// TODO Auto-generated method stub

	}

	public void showTrends(Location selected) {
		// TODO Auto-generated method stub

	}

	public void readyForPoints(Double lat, Double long1) {
		Set<Location> locs = locService.getLocationsNear(60.08504, 22.15187);
		log.info("fetched from db:" + locs.size());

		view.addLocations(locs);
	}

	public void markBeachDirty(Location selected) {
		selected.setStatus(STATUS.DIRTY);
		selected = locService.save(selected);

		view.updateMarker(selected);
	}

}
