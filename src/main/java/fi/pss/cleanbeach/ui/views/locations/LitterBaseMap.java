/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.pss.cleanbeach.ui.views.locations;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.client.PopupState;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.addon.touchkit.extensions.Geolocator;
import com.vaadin.addon.touchkit.extensions.PositionCallback;
import com.vaadin.addon.touchkit.gwt.client.vcom.Position;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.ui.util.Lang;

/**
 * 
 * @author mattitahvonenitmill, thomas
 */
public class LitterBaseMap extends LMap implements PositionCallback {

	private static final long serialVersionUID = -4582977579039441885L;

	// private static final String MML_KAPSI_ATTRIBUTION_STRING =
	// "Maanmittauslaitos, hosted by kartat.kapsi.fi";
	private final LocationPresenter presenter;

	private final MapPointSelectedListener listener;

	private final Map<Location, LMarker> markers = new HashMap<>();
	private LMarker tempMarker;

	private boolean positioningHasBeenRun;

	public LitterBaseMap(LocationPresenter presenter,
			final MapPointSelectedListener l) {

		addStyleName("map");

		this.presenter = presenter;
		listener = l;

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
		setCenter(60.08504, 22.15187);

		addClickListener(new LeafletClickListener() {

			@Override
			public void onClick(LeafletClickEvent event) {

				if (tempMarker == null || !tempMarker.isAttached()) {
					tempMarker = new LMarker();
					addComponent(tempMarker);

					tempMarker.setPoint(event.getPoint());
					l.selectedNew(event.getPoint());
				} else {
					clearTempMarker();
					l.selectedNew(null);
				}

			}
		});

	}

	LMarker getMarker() {
		return tempMarker;
	}

	public Double getLong() {
		return getCenter().getLon();
	}

	public Double getLat() {
		return getCenter().getLat();
	}

	public void addPoint(final Location l) {
		LMarker m = new LMarker(l.getLatitude(), l.getLongitude());
		m.setData(l);
		addComponent(m);

		setIcon(m, l);
		m.setIconAnchor(new Point(16, 32));

		m.setPopup(l.getName());
		PopupState state = new PopupState();
		state.closeButton = false;
		state.zoomAnimation = false;
		state.minWidth = 150;
		state.offset = new Point(0, -32);
		state.autoPan = true;
		state.autoPanPadding = new Point(10, 10);
		m.setPopupState(state);

		m.addClickListener(new LeafletClickListener() {

			@Override
			public void onClick(LeafletClickEvent event) {
				listener.selectedExisting(l);
			}
		});
		markers.put(l, m);
	}

	@Override
	public void onSuccess(Position position) {
		setCenter(position.getLatitude(), position.getLongitude());
		presenter.readyForPoints(position.getLatitude(),
				position.getLongitude());
	}

	@Override
	public void onFailure(int errorCode) {
		Notification.show(Lang.get("locations.map.noposition"),
				Type.WARNING_MESSAGE);
		presenter.readyForPoints(getCenter().getLat(), getCenter().getLon());
	}

	public void runPositioning() {
		if (!positioningHasBeenRun) {
			Geolocator.detect(this);
			positioningHasBeenRun = true;
		}
	}

	public void update(Location selected) {
		LMarker m = markers.get(selected);
		if (m == null) {
			addPoint(selected);
		} else {
			setIcon(m, selected);
		}

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

	public void clearTempMarker() {
		if (tempMarker != null && tempMarker.isAttached()) {
			removeComponent(tempMarker);
		}
	}

}
