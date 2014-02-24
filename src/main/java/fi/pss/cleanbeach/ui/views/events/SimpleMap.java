package fi.pss.cleanbeach.ui.views.events;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.ClassResource;

import fi.pss.cleanbeach.data.Location;

public class SimpleMap extends LMap {

	private static final long serialVersionUID = -3763791099550992718L;

	public SimpleMap(Location l) {

		addStyleName("simplemap");

		LTileLayer mapBoxTiles = new LTileLayer(
				"http://{s}.tiles.mapbox.com/v3/mstahv.h4mbchln/{z}/{x}/{y}.png");
		addBaseLayer(mapBoxTiles, "MapBox");

		setCenter(new Point(l.getLatitude(), l.getLongitude()));

		LMarker m = new LMarker(new Point(l.getLatitude(), l.getLongitude()));
		setIcon(m, l);
		addComponent(m);
	}

	private static void setIcon(LMarker m, Location loc) {
		switch (loc.getStatus()) {
		case OK:
			m.setIcon(new ClassResource("flag_green.png"));
			break;
		case DIRTY:
			m.setIcon(new ClassResource("flag_red.png"));
			break;
		case NO_DATA:
			m.setIcon(new ClassResource("flag_gray.png"));
			break;

		default:
			break;
		}

	}
}
