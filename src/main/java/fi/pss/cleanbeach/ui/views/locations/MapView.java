/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.pss.cleanbeach.ui.views.locations;

import com.vaadin.addon.touchkit.ui.NavigationView;

import org.vaadin.addon.leaflet.LMap;

/**
 *
 * @author mattitahvonenitmill
 */
public class MapView extends NavigationView {

    LMap lMap = new LitterBaseMap();
    
    public MapView() {
        setCaption("Törkykartal");
        setContent(lMap);
        
    }
    
}
