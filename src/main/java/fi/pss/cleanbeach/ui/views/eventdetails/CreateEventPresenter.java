package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.LocationService;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

public abstract class CreateEventPresenter<T extends ICreateEvent> extends
		AbstractPresenter<T> {

	@Inject
	private LocationService locService;

	@Override
	public void init(User currentUser) {
	}

	public Collection<Location> getLocations() {
		return locService.getLocationsForCreate();
	}

	public abstract void createEvent(UsersGroup creator, String desc,
			Date start, Location loc);
}
