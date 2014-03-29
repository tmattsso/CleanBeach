package fi.pss.cleanbeach.standalone.map;

import javax.inject.Inject;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Widgetset("fi.pss.cleanbeach.standalone.map.MapWidgetSet")
@Theme("cleanbeachtheme")
@CDIUI("map")
public class MapUI extends UI {

	private static final long serialVersionUID = 8560450329285696314L;

	@Inject
	private MapComponent map;

	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();
		setContent(map);
		map.init();
	}

}
