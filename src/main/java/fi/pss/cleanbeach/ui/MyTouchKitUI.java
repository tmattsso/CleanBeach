package fi.pss.cleanbeach.ui;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.Cookie;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.ui.views.login.LoginEvent;
import fi.pss.cleanbeach.ui.views.login.LoginView;

/**
 * The UI's "main" class
 */
@SuppressWarnings("serial")
@Widgetset("fi.pss.cleanbeach.ui.gwt.AppWidgetSet")
@Theme("cleanbeachtheme")
@CDIUI
public class MyTouchKitUI extends UI {

	private static final String COOKIE_NAME = "CleanBeachUser";

	// private static String AUTOLOGIN = null;
	private static String AUTOLOGIN = "thomas@t.com";
	// private static String AUTOLOGIN = "demo@demo.com";

	private User currentUser;

	@Inject
	private MainView mainView;

	@Inject
	private LoginView login;

	@Inject
	private AuthenticationService authService;

	@Override
	protected void init(VaadinRequest request) {

		setErrorHandler(new PSSErrorHandler());

		// if (!getPage().getWebBrowser().isTouchDevice()) {
		// // TODO proper redirection; this obviously doesn't work :)
		// getPage().setLocation("/fallback");
		// return;
		// }

		if (AUTOLOGIN != null) {
			User u = authService.login("thomas@t.com", "vaadin");
			login(new LoginEvent(u));
		} else {
			// build login
			setContent(login);
		}
	}

	public void login(@Observes LoginEvent e) {

		currentUser = e.user;

		setCookie();

		setContent(mainView);
		mainView.init();
	}

	public static Cookie getUsernameCookie() {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(COOKIE_NAME)) {
					return c;
				}
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
				((MyTouchKitUI) UI.getCurrent()).currentUser.getEmail());
		newCookie.setDomain("localhost");
		// newCookie.setSecure(true); TODO enable
		// store for 30 days
		newCookie.setMaxAge(60 * 60 * 24 * 30);

		VaadinService.getCurrentResponse().addCookie(newCookie);
	}

	/**
	 * Returns the currently logged in user.
	 */
	public static User getCurrentUser() {
		return ((MyTouchKitUI) getCurrent()).currentUser;
	}

}