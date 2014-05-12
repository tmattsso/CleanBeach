/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.pss.cleanbeach.standalone.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.cdi.UIScoped;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.Window;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.services.EventService;

/**
 * 
 * @author mattitahvonenitmill, thomas
 */
@UIScoped
public class MapComponent extends LMap {

	private static final long serialVersionUID = -4582977579039441885L;

	private final Map<Location, LMarker> markers = new HashMap<>();

	@Inject
	private EventService eService;

	@Inject
	private EventDetails details;

	public MapComponent() {

		addStyleName("map");
		setSizeFull();

		LTileLayer mapBoxTiles = new LTileLayer(
				"http://{s}.tiles.mapbox.com/v3/mstahv.h4mbchln/{z}/{x}/{y}.png");
		addBaseLayer(mapBoxTiles, "MapBox");

		// LTileLayer ortokuva = new LTileLayer(
		// "http://v3.tahvonen.fi/mvm71/tiles/ortokuva/{z}/{x}/{y}.png");
		// ortokuva.setAttributionString(MML_KAPSI_ATTRIBUTION_STRING);
		// ortokuva.setMaxZoom(18);
		// addBaseLayer(ortokuva, "Ilmakuva");
		//
		// LTileLayer peruskartta = new LTileLayer(
		// "http://v3.tahvonen.fi/mvm71/tiles/peruskartta/{z}/{x}/{y}.png");
		// peruskartta.setAttributionString(MML_KAPSI_ATTRIBUTION_STRING);
		// addBaseLayer(peruskartta, "Peruskartta");
		// peruskartta.setMaxZoom(18);
		// peruskartta.setDetectRetina(true);

		// default
		setCenter(60.4, 22.0);
		setZoomLevel(9);
	}

	public Double getLong() {
		return getCenter().getLon();
	}

	public Double getLat() {
		return getCenter().getLat();
	}

	public void addPoint(final fi.pss.cleanbeach.data.Event e) {

		Location l = e.getLocation();

		LMarker m = new LMarker(l.getLatitude(), l.getLongitude());
		m.setData(l);
		addComponent(m);

		setIcon(m, l);
		m.setIconAnchor(new Point(16, 32));

		m.addClickListener(new LeafletClickListener() {

			@Override
			public void onClick(LeafletClickEvent event) {
				details.update(e);
				Window pop = new Window(null, details);
				pop.addStyleName("detailpop");
				pop.setModal(true);
				getUI().addWindow(pop);

				setCenter(e.getLocation().getLatitude(), e.getLocation()
						.getLongitude() + getDetailsPosOffset());
			}

		});
		markers.put(l, m);
	}

	private double getDetailsPosOffset() {
		return 0.00033333 * Math.pow(2, Math.abs(getZoomLevel() - 18));
	}

	private static void setIcon(LMarker m, Location loc) {
		switch (loc.getStatus()) {
		case OK:
			m.setIcon(new ClassResource("../../ui/flag_green.png"));
			break;
		case DIRTY:
			m.setIcon(new ClassResource("../../ui/flag_red.png"));
			break;
		case NO_DATA:
			m.setIcon(new ClassResource("../../ui/flag_gray.png"));
			break;

		default:
			break;
		}

	}

	public void init() {
		setZoomLevel(10);

		Collection<fi.pss.cleanbeach.data.Event> eventsNear = eService
				.getEventsNear(getLat(), getLong(), getZoomLevel());
		for (fi.pss.cleanbeach.data.Event e : eventsNear) {
			addPoint(e);
		}
	}

}
