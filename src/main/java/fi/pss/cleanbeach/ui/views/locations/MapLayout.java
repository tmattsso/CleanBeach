package fi.pss.cleanbeach.ui.views.locations;

import java.util.Set;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.ui.views.locations.CreateLocationPopover.ConfirmListener;

public class MapLayout extends NavigationView implements
		MapPointSelectedListener {

	private static final long serialVersionUID = -7548792748684460162L;

	private final LitterBaseMap lMap;
	private Location selected;

	private final Layout actionButtons;
	private final HorizontalLayout addButtons;

	public MapLayout(final LocationPresenter presenter) {

		setCaption("Reported locations");

		lMap = new LitterBaseMap(presenter, this);
		lMap.setSizeFull();

		Button addLocation = new Button("Add location");
		addLocation.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 5940636967467650018L;

			@Override
			public void buttonClick(ClickEvent event) {

				// TODO check if too close to existing location

				// ask name for new location
				CreateLocationPopover pop = new CreateLocationPopover(
						new ConfirmListener() {

							@Override
							public void confirm(String name) {
								Point p = lMap.getMarker().getPoint();
								presenter.addLocation(p.getLat(), p.getLon(),
										name);
								lMap.clearTempMarker();
							}
						});
				pop.showRelativeTo(actionButtons);
			}
		});

		Button createEvent = new Button("event");
		createEvent.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 682703780760294261L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.createEvent(selected);
			}
		});

		Button markDirty = new Button("report");
		markDirty.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 682703780760294261L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.markBeachDirty(selected);
			}
		});

		Button showTrends = new Button("trends");
		showTrends.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -9055053032611311553L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showTrends(selected);
			}
		});

		Button showEvents = new Button("events");
		showEvents.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7853451135198225867L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showEvents(selected);
			}
		});

		actionButtons = new HorizontalLayout(createEvent, markDirty,
				showTrends, showEvents);
		actionButtons.setSizeUndefined();
		actionButtons.setVisible(false);

		addButtons = new HorizontalLayout(addLocation);
		addButtons.setSizeUndefined();
		addButtons.setVisible(false);

		VerticalLayout vl = new VerticalLayout(lMap, actionButtons, addButtons);
		vl.setSizeFull();
		vl.setExpandRatio(lMap, 1);
		vl.setComponentAlignment(actionButtons, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(addButtons, Alignment.MIDDLE_CENTER);

		setContent(vl);
	}

	@Override
	protected void onBecomingVisible() {
		super.onBecomingVisible();

		// Start positioning
		lMap.runPositioning();
	}

	public void addLocations(Set<Location> locs) {

		for (Location l : locs) {
			lMap.addPoint(l);
		}
	}

	public void updateMarker(Location selected) {
		lMap.update(selected);
	}

	@Override
	public void selectedNew(Point point) {

		actionButtons.setVisible(false);
		addButtons.setVisible(false);

		if (point != null) {
			addButtons.setVisible(true);
		}
	}

	@Override
	public void selectedExisting(Location loc) {
		actionButtons.setVisible(true);
		addButtons.setVisible(false);

		selected = loc;
		lMap.clearTempMarker();
	}
}
