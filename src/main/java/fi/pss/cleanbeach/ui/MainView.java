package fi.pss.cleanbeach.ui;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.TabSheet.Tab;

import fi.pss.cleanbeach.ui.util.Lang;
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

	public void init(Long selectedEventId) {

		Tab tab;

		tab = addTab(eventsView, Lang.get("events.caption"));
		TouchKitIcon.flag.addTo(tab);

		tab = addTab(groupView, Lang.get("Groups.view.caption"));
		TouchKitIcon.group.addTo(tab);

		tab = addTab(mapView, Lang.get("locations.caption"));
		TouchKitIcon.globe.addTo(tab);

		tab = addTab(settingsView, Lang.get("settings.caption"));
		TouchKitIcon.cogs.addTo(tab);

		if (selectedEventId != null) {
			eventsView.showEvent(selectedEventId);
		}
	}
}
