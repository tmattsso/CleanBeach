package fi.pss.cleanbeach.ui.views.locations;

import java.util.Set;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.views.locations.CreateLocationPopover.ConfirmLocationListener;
import fi.pss.cleanbeach.ui.views.locations.ReportDirtyPopover.ConfirmThrashListener;

public class MapLayout extends NavigationView implements
		MapPointSelectedListener {

	private static final long serialVersionUID = -7548792748684460162L;

	private final LitterBaseMap lMap;
	private Location selected;

	private final HorizontalLayout actionButtons;
	private final HorizontalLayout addButtons;

	public MapLayout(final LocationPresenter presenter) {

		setCaption("Reported locations");
		addStyleName("maplayout");

		lMap = new LitterBaseMap(presenter, this);
		lMap.setSizeFull();

		final Button addLocation = new Button("Add location");
		TouchKitIcon.plus.addTo(addLocation);
		addLocation.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 5940636967467650018L;

			@Override
			public void buttonClick(ClickEvent event) {

				// TODO check if too close to existing location

				// ask name for new location
				CreateLocationPopover pop = new CreateLocationPopover(
						new ConfirmLocationListener() {

							@Override
							public void confirm(String name) {
								Point p = lMap.getMarker().getPoint();
								presenter.addLocation(p.getLat(), p.getLon(),
										name);
								lMap.clearTempMarker();
							}
						});
				pop.showRelativeTo(addLocation);
			}
		});

		Button createEvent = new Button("event");
		TouchKitIcon.calendar.addTo(createEvent);
		createEvent.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 682703780760294261L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.createEvent(selected);
			}
		});

		Button markDirty = new Button("dirty");
		TouchKitIcon.eyeOpen.addTo(markDirty);
		markDirty.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 682703780760294261L;

			@Override
			public void buttonClick(ClickEvent event) {
				ReportDirtyPopover pop = new ReportDirtyPopover(
						new ConfirmThrashListener() {

							@Override
							public void confirm(String desc) {
								presenter.markBeachDirty(selected, desc);
							}
						});
				pop.showRelativeTo(actionButtons);
			}
		});

		Button markThrash = new Button("report");
		TouchKitIcon.exclamationSign.addTo(markThrash);
		markThrash.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -9055053032611311553L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showTrash(selected, MyTouchKitUI.getCurrentUser());
			}
		});

		// Button showTrends = new Button("trends");
		// TouchKitIcon.link.addTo(showTrends);
		// showTrends.addClickListener(new ClickListener() {
		//
		// private static final long serialVersionUID = -9055053032611311553L;
		//
		// @Override
		// public void buttonClick(ClickEvent event) {
		// presenter.showTrends(selected);
		// }
		// });

		Button showEvents = new Button("history");
		TouchKitIcon.listAlt.addTo(showEvents);
		showEvents.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7853451135198225867L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showEvents(selected);
			}
		});

		actionButtons = new HorizontalLayout(createEvent, markDirty,
				markThrash, showEvents);
		actionButtons.setWidth("100%");
		actionButtons.setSpacing(true);
		actionButtons.setVisible(false);
		actionButtons.addStyleName("actionbuttons");

		addButtons = new HorizontalLayout(addLocation);
		addButtons.setWidth("100%");
		addButtons.setSpacing(true);
		addButtons.setVisible(false);
		addButtons.addStyleName("actionbuttons");

		CssLayout vl = new CssLayout(lMap, actionButtons, addButtons);
		vl.setSizeFull();

		setContent(vl);
	}

	@Override
	protected void onBecomingVisible() {
		super.onBecomingVisible();

		getNavigationManager().setPreviousComponent(null);

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
