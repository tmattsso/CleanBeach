package fi.pss.cleanbeach.ui.views.locations;

import org.vaadin.addon.leaflet.shared.Point;

import fi.pss.cleanbeach.data.Location;

public interface MapPointSelectedListener {

	public void selected(Point point, Location l);
}
