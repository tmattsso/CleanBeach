package fi.pss.cleanbeach.ui;

import javax.inject.Inject;
import javax.servlet.http.Cookie;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService;

/**
 * The UI's "main" class
 */
@SuppressWarnings("serial")
@Widgetset("fi.pss.cleanbeach.ui.gwt.AppWidgetSet")
// @Theme("cleanbeachtheme")
@Theme("touchkit")
@CDIUI
public class MyTouchKitUI extends UI {

	private static final String COOKIE_NAME = "CleanBeachUser";
	@Inject
	private AuthenticationService authService;
	private User currentUser;

	@Inject
	private CDIViewProvider provider;

	@Override
	protected void init(VaadinRequest request) {

		setErrorHandler(new PSSErrorHandler());

		if (!getPage().getWebBrowser().isTouchDevice()) {
			// TODO proper redirection; this obviously doesn't work :)
			getPage().setLocation("/fallback");
			return;
		}

		// build login
		Navigator n = new Navigator(this, this);
		n.addProvider(provider);
		n.navigateTo("login");
	}

	public boolean login(String user, String pass) {

		User u = authService.login(user, pass);
		if (u != null) {
			currentUser = u;

			setCookie();

			final TabBarView tabBarView = new TabBarView();
			final NavigationManager navigationManager = new NavigationManager();
			navigationManager.setCaption("Tab 1");
			navigationManager.setCurrentComponent(new MenuView());
			Tab tab;
			tab = tabBarView.addTab(navigationManager);
			TouchKitIcon.book.addTo(tab);
			tab = tabBarView.addTab(new Label("Tab 2"), "Tab 2");
			TouchKitIcon.ambulance.addTo(tab);
			tab = tabBarView.addTab(new Label("Tab 3"), "Tab 3");
			TouchKitIcon.download.addTo(tab);

			MapView mapView = new MapView();
			Tab maptab = tabBarView.addTab(mapView, "Map");
			TouchKitIcon.globe.addTo(maptab);

			setContent(tabBarView);
			return true;
		} else {
			return false;
		}
	}

	public static Cookie getUsernameCookie() {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		for (Cookie c : cookies) {
			if (c.getName().equals(COOKIE_NAME)) {
				return c;
			}
		}
		return null;
	}

	public static void setCookie() {
		// check for old
		if (getUsernameCookie() != null) {
			return;
		}

		Cookie newCookie = new Cookie(COOKIE_NAME,
				((MyTouchKitUI) UI.getCurrent()).currentUser.getUsername());
		newCookie.setDomain("localhost");
		// newCookie.setSecure(true); TODO enable
		// store for 30 days
		newCookie.setMaxAge(60 * 60 * 24 * 30);

		VaadinService.getCurrentResponse().addCookie(newCookie);
	}

}