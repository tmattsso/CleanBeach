package fi.pss.cleanbeach.ui;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
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
		tab.setIcon(FontAwesome.FLAG);

		tab = addTab(groupView, Lang.get("Groups.view.caption"));
		tab.setIcon(FontAwesome.USERS);

		tab = addTab(mapView, Lang.get("locations.caption"));
		tab.setIcon(FontAwesome.GLOBE);

		tab = addTab(settingsView, Lang.get("settings.caption"));
		tab.setIcon(FontAwesome.COGS);

		if (selectedEventId != null) {
			eventsView.showEvent(selectedEventId);
		}
	}
}
