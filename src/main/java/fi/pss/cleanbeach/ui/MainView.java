package fi.pss.cleanbeach.ui;

import javax.enterprise.context.SessionScoped;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;

@SessionScoped
public class MainView extends TabBarView implements ViewDisplay {

	private static final long serialVersionUID = 2092286827739040407L;

	public MainView() {

	}

	public void init() {
		final NavigationManager navigationManager = new NavigationManager();
		navigationManager.setCaption("Tab 1");
		navigationManager.setCurrentComponent(new MenuView());
		Tab tab;
		tab = addTab(navigationManager);
		TouchKitIcon.book.addTo(tab);
		tab = addTab(new Label("Tab 2"), "Tab 2");
		TouchKitIcon.ambulance.addTo(tab);
		tab = addTab(new Label("Tab 3"), "Tab 3");
		TouchKitIcon.download.addTo(tab);

		MapView mapView = new MapView();
		Tab maptab = addTab(mapView, "Map");
		TouchKitIcon.globe.addTo(maptab);
	}

	@Override
	public void showView(View view) {
		// TODO Auto-generated method stub

	}

}
