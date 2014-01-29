/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.pss.cleanbeach;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;

/**
 *
 * @author mattitahvonenitmill
 */
public class LitterBaseMap extends LMap {

    private static final String MML_KAPSI_ATTRIBUTION_STRING = "Maanmittauslaitos, hosted by kartat.kapsi.fi";

	public LitterBaseMap() {

        LTileLayer mapBoxTiles = new LTileLayer("http://{s}.tiles.mapbox.com/v3/mstahv.h4mbchln/{z}/{x}/{y}.png");
        addBaseLayer(mapBoxTiles, "MapBox");

        LTileLayer ortokuva = new LTileLayer("http://v3.tahvonen.fi/mvm71/tiles/ortokuva/{z}/{x}/{y}.png");
        ortokuva.setAttributionString(MML_KAPSI_ATTRIBUTION_STRING);
        ortokuva.setMaxZoom(18);
        addBaseLayer(ortokuva, "Ilmakuva");
        
        
        LTileLayer peruskartta = new LTileLayer("http://v3.tahvonen.fi/mvm71/tiles/peruskartta/{z}/{x}/{y}.png");
        peruskartta.setAttributionString(MML_KAPSI_ATTRIBUTION_STRING);
        addBaseLayer(peruskartta, "Peruskartta");
        peruskartta.setMaxZoom(18);
        peruskartta.setDetectRetina(true);
        
        setCenter(60.08504, 22.15187);

    }
    
    
    
}
