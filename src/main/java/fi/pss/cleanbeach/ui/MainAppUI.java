package fi.pss.cleanbeach.ui;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.Cookie;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.events.PublicEventView;
import fi.pss.cleanbeach.ui.views.login.LoginEvent;
import fi.pss.cleanbeach.ui.views.login.LoginView;

/**
 * The UI's "main" class
 */
@SuppressWarnings("serial")
@Widgetset("fi.pss.cleanbeach.ui.gwt.AppWidgetSet")
@Theme("cleanbeachtheme")
@CDIUI
public class MainAppUI extends UI {

	private static final String COOKIE_NAME = "CleanBeachUser";

	private User currentUser;

	@Inject
	private MainView mainView;

	@Inject
	private LoginView login;

	@Inject
	private EventService eService;

	private Long selectedEventId;

	@Override
	protected void init(VaadinRequest request) {

		setErrorHandler(new PSSErrorHandler());

		String parameter = request.getParameter("event");
		if (parameter != null) {

			try {
				long eventId = Long.parseLong(parameter);
				fi.pss.cleanbeach.data.Event e = eService.loadDetails(eventId);
				if (e != null) {
					setContent(new PublicEventView(e));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Label noEventFound = new Label(Lang.get("public.noevent"));
			setContent(noEventFound);
			return;
		}

		// if (!getPage().getWebBrowser().isTouchDevice()) {
		// // TODO proper redirection; this obviously doesn't work :)
		// getPage().setLocation("/fallback");
		// return;
		// }

		// build login
		setContent(login);
	}

	public void login(@Observes LoginEvent e) {

		currentUser = e.user;

		setCookie();

		setContent(mainView);
		mainView.init(selectedEventId);
	}

	public void logout(@Observes LogoutEvent e) {

		getCurrent().close();
		getCurrent().getPage().setLocation("");
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
				((MainAppUI) UI.getCurrent()).currentUser.getEmail());
		newCookie.setDomain("localhost");
		newCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		// newCookie.setSecure(true); TODO enable
		// store for 30 days
		newCookie.setMaxAge(60 * 60 * 24 * 30);

		VaadinService.getCurrentResponse().addCookie(newCookie);
	}

	/**
	 * Returns the currently logged in user.
	 */
	public static User getCurrentUser() {
		return getCurrent().currentUser;
	}

	public static void setCurrentUser(User current) {
		getCurrent().currentUser = current;
	}

	public static MainAppUI getCurrent() {
		return (MainAppUI) UI.getCurrent();
	}

	public void loginFromPublic(Long id) {

		selectedEventId = id;
		setContent(login);
	}

}
