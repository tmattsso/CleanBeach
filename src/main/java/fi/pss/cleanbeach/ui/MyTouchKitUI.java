package fi.pss.cleanbeach.ui;

import javax.inject.Inject;
import javax.servlet.http.Cookie;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

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
	private Label errorLabel;

	@Override
	protected void init(VaadinRequest request) {

		if (!getPage().getWebBrowser().isTouchDevice()) {
			// TODO proper redirection; this obviously doesn't work :)
			getPage().setLocation("/fallback");
			return;
		}

		// build login
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addStyleName("login");
		setContent(vl);

		Label desc = new Label(
				"<span>Siisti Biitsi 2014</span>Siisti Biitsi is a volunteer project organized by Pidä Saaristo Siistinä ry. The purpose is to clean all beaches in Finland. The app helps you and your friends to organize your efforts, as well as provide the organization wiht invaluable data.",
				ContentMode.HTML);
		desc.addStyleName("desc");

		Image logo = new Image();
		logo.setSource(new ThemeResource("img/logo.png"));
		logo.setHeight("102px");
		logo.setWidth("102px");
		logo.addStyleName("logo");

		HorizontalLayout hl = new HorizontalLayout(desc, logo);
		hl.setWidth("100%");
		hl.setExpandRatio(desc, 1);
		hl.addStyleName("logolayout");
		vl.addComponent(hl);

		final TextField username = new TextField("Username");
		username.setImmediate(true);
		username.addStyleName("username");
		vl.addComponent(username);

		final PasswordField password = new PasswordField("Password");
		password.setImmediate(true);
		password.addStyleName("password");
		vl.addComponent(password);

		ValueChangeListener vlc = new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				errorLabel.setValue("");
			}
		};
		username.addValueChangeListener(vlc);
		password.addValueChangeListener(vlc);

		errorLabel = new Label();
		errorLabel.addStyleName("error");
		vl.addComponent(errorLabel);

		Button login = new Button("Login", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				login(username.getValue(), password.getValue());
			}
		});
		login.setClickShortcut(KeyCode.ENTER);
		login.addStyleName("login");
		vl.addComponent(login);

		// auto-fill username
		Cookie c = getUsernameCookie();
		if (c != null) {
			username.setValue(c.getValue());
			password.focus();
		}

		Button forgotPass = new Button("Forgot your password?");
		forgotPass.addStyleName(BaseTheme.BUTTON_LINK);
		forgotPass.addStyleName("forgotpass");
		vl.addComponent(forgotPass);
		vl.setComponentAlignment(forgotPass, Alignment.MIDDLE_CENTER);

		Button fbLogin = new Button("Facebook");
		fbLogin.setWidth("100%");
		Button twitterLogin = new Button("Twitter");
		twitterLogin.setWidth("100%");

		hl = new HorizontalLayout(fbLogin, twitterLogin);
		hl.addStyleName("socialbuttons");
		hl.setSpacing(true);
		vl.addComponent(hl);

	}

	public void login(String user, String pass) {

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
		} else {
			errorLabel.setValue("Invalid credentials");

		}
	}

	private Cookie getUsernameCookie() {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		for (Cookie c : cookies) {
			if (c.getName().equals(COOKIE_NAME)) {
				return c;
			}
		}
		return null;
	}

	private void setCookie() {
		// check for old
		if (getUsernameCookie() != null) {
			return;
		}

		Cookie newCookie = new Cookie(COOKIE_NAME, currentUser.getUsername());
		// newCookie.setSecure(true); TODO enable
		// store for 30 days
		newCookie.setMaxAge(60 * 60 * 24 * 30);

		VaadinService.getCurrentResponse().addCookie(newCookie);
	}

}