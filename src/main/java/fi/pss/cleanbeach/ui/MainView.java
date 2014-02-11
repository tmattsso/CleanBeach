package fi.pss.cleanbeach.ui;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;

import fi.pss.cleanbeach.ui.views.events.EventsView;
import fi.pss.cleanbeach.ui.views.locations.LocationView;

@UIScoped
public class MainView extends TabBarView implements ViewDisplay {

	private static final long serialVersionUID = 2092286827739040407L;

	@Inject
	private EventsView eventsView;

	@Inject
	private LocationView mapView;

	public MainView() {

	}

	public void init() {

		// TODO refactor to be inside view itself; this level doesn't care
		final NavigationManager navigationManager = new NavigationManager();
		navigationManager.setCaption("Events");
		navigationManager.setCurrentComponent(eventsView);
		Tab tab;
		tab = addTab(navigationManager);
		TouchKitIcon.flag.addTo(tab);

		tab = addTab(new Label("Tab 2"), "Groups");
		TouchKitIcon.group.addTo(tab);

		tab = addTab(mapView, "Locations");
		TouchKitIcon.globe.addTo(tab);

		tab = addTab(new Label("Tab 3"), "Settings");
		TouchKitIcon.cogs.addTo(tab);
	}

	@Override
	public void showView(View view) {
		// TODO Auto-generated method stub

	}

}
