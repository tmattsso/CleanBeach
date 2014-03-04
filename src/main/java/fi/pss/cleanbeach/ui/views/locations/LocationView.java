package fi.pss.cleanbeach.ui.views.locations;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.ui.mvp.AbstractView;

@UIScoped
public class LocationView extends AbstractView<LocationPresenter> implements
		ILocation {

	private static final long serialVersionUID = 6914178286159188531L;

	private MapLayout map;

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
			ArrayList<fi.pss.cleanbeach.data.Event> arrayList) {

		navigateTo(new HistoryLayout());
	}

	@Override
	public void showCreateEvent(Location selected) {
		navigateTo(new CreateEventLayout());
	}

	@Override
	public void showTrends(Location selected,
			ArrayList<fi.pss.cleanbeach.data.Event> arrayList) {
		navigateTo(new TrendsLayout());
	}

	@Override
	public void showTrashInput(Location selected, ThrashDAO thrash) {
		navigateTo(new ThrashLocationInput(selected, presenter, thrash));
	}

}
