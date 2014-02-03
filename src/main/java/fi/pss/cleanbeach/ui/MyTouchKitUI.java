package fi.pss.cleanbeach.ui;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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

	@Inject
	private AuthenticationService authService;
	private User currentUser;

	@Override
	protected void init(VaadinRequest request) {

		if (!getPage().getWebBrowser().isTouchDevice()) {
			// TODO proper redirection; this obviously doesn't work :)
			getPage().setLocation("/fallback");
			return;
		}

		// build login
		VerticalLayout vl = new VerticalLayout();
		setContent(vl);

		Image logo = new Image();
		logo.setSource(new ThemeResource("img/logo.png"));
		logo.setHeight("102px");
		logo.setWidth("102px");
		vl.addComponent(logo);
		vl.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

		final TextField username = new TextField("Username");
		vl.addComponent(username);

		final PasswordField password = new PasswordField("Username");
		vl.addComponent(password);

		Button login = new Button("Login", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				login(username.getValue(), password.getValue());
			}
		});
		login.setClickShortcut(KeyCode.ENTER);
		vl.addComponent(login);

	}

	public void login(String user, String pass) {

		User u = authService.login(user, pass);
		if (u != null) {
			currentUser = u;

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
		}
	}
}
