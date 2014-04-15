package fi.pss.cleanbeach.ui.views.locations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.views.eventdetails.CreateEventLayout;
import fi.pss.cleanbeach.ui.views.eventdetails.EventDetailsCapableView;

@UIScoped
public class LocationView extends EventDetailsCapableView<LocationPresenter>
		implements ILocation {

	private static final long serialVersionUID = 6914178286159188531L;

	private MapLayout map;

	private HistoryLayout historyLayout;

	public LocationView() {

	}

	@Override
	protected ComponentContainer getMainContent() {
		return map = new MapLayout(presenter);
	}

	@Override
	@Inject
	public void injectPresenter(LocationPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void addLocations(Set<Location> locs) {
		map.addLocations(locs);
	}

	@Override
	public void updateMarker(Location selected) {
		map.updateMarker(selected);
	}

	@Override
	public void selectMarker(Location l) {
		map.selectedExisting(l);
	}

	@Override
	public void showEvents(Location selected,
			Collection<fi.pss.cleanbeach.data.Event> events) {

		historyLayout = new HistoryLayout(selected, events, presenter);
		navigateTo(historyLayout);
	}

	@Override
	public void showTrends(Location selected,
			ArrayList<fi.pss.cleanbeach.data.Event> arrayList) {
		navigateTo(new TrendsLayout());
	}

	@Override
	public void showTrashInput(Location selected, ThrashDAO thrash) {
		navigateTo(new ThrashInputLocationLayout(selected, presenter, thrash));
	}

	@Override
	public void showCreateEvent(UsersGroup selectedGroup,
			Location selectedLocation) {
		navigateTo(new CreateEventLayout(null, selectedLocation, presenter));
	}

	@Override
	public void navigateBackAfterDelete(long eventId) {
		navigateBack();
		historyLayout.remove(eventId);
	}

	@Override
	public void updateEventList(Location loc, fi.pss.cleanbeach.data.Event e) {
		if (historyLayout != null) {
			historyLayout.add(e);
		}
	}

}
