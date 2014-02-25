package fi.pss.cleanbeach.ui;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.TabSheet.Tab;

import fi.pss.cleanbeach.ui.views.events.EventsView;
import fi.pss.cleanbeach.ui.views.group.GroupView;
import fi.pss.cleanbeach.ui.views.locations.LocationView;
import fi.pss.cleanbeach.ui.views.settings.SettingsView;

@UIScoped
public class MainView extends TabBarView {

	private static final long serialVersionUID = 2092286827739040407L;

	@Inject
	private EventsView eventsView;

	@Inject
	private LocationView mapView;

	@Inject
	private GroupView groupView;

	@Inject
	private SettingsView settingsView;

	public MainView() {

	}

	public void init() {

		Tab tab;

		tab = addTab(eventsView, "Events");
		TouchKitIcon.flag.addTo(tab);

		tab = addTab(groupView, "Groups");
		TouchKitIcon.group.addTo(tab);

		tab = addTab(mapView, "Locations");
		TouchKitIcon.globe.addTo(tab);

		tab = addTab(settingsView, "Settings");
		TouchKitIcon.cogs.addTo(tab);
	}
}
