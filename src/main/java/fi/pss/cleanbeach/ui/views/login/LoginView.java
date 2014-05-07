package fi.pss.cleanbeach.ui.views.login;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.Cookie;

import org.vaadin.se.facebook.LoginButton;

import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.cdi.UIScoped;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.mvp.AbstractView;
import fi.pss.cleanbeach.ui.util.Lang;

@UIScoped
public class LoginView extends AbstractView<LoginPresenter> implements ILogin {

	private static final long serialVersionUID = -259521650823470699L;

	private Label errorLabel;

	private RegisterLayout register;

	private VerticalLayout main;

	private VerticalLayout root;

	private Button registerButton;

	private HorizontalLayout socialButtons;

	public LoginView() {
	}

	@Override
	protected ComponentContainer getMainContent() {
		root = new VerticalLayout();
		root.setSpacing(true);
		root.setMargin(true);
		root.setSizeFull();
		root.addStyleName("login");

		Cookie c = MainAppUI.getCurrent().getLangCookie();
		if (c == null) {
			build(null);
		} else if (c != null) {
			Locale selectedLocale = new Locale(c.getValue());
			super.setLocale(selectedLocale);
			build(selectedLocale);
		}

		return root;
	}

	private void build(Locale selectedLocale) {

		root.removeAllComponents();

		main = new VerticalLayout();
		main.setSpacing(true);
		main.addStyleName("mainlogin");

		register = new RegisterLayout(null, null, presenter);

		final NativeSelect langSelect = new NativeSelect();
		langSelect.setImmediate(true);
		langSelect.setNullSelectionAllowed(false);
		langSelect.setWidth("100%");

		Locale l = new Locale("fi");
		langSelect.addItem(l);
		langSelect.setItemCaption(l, "Suomeksi");
		l = new Locale("sv");
		langSelect.addItem(l);
		langSelect.setItemCaption(l, "På Svenska");
		l = new Locale("en");
		langSelect.addItem(l);
		langSelect.setItemCaption(l, "In English");
		main.addComponent(langSelect);

		if (selectedLocale != null) {
			langSelect.setValue(selectedLocale);
		} else {
			langSelect.setValue(Lang.getLangInUse());
		}

		langSelect.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -4623575640110949845L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				presenter.changeLang((Locale) langSelect.getValue());
			}
		});

		Label desc = new Label("<span>" + Lang.get("login.caption.big")
				+ "</span>" + Lang.get("login.caption.small"), ContentMode.HTML);
		desc.addStyleName("desc");

		Image logo = new Image();
		logo.setSource(new ClassResource("logo.png"));
		logo.setHeight("102px");
		logo.setWidth("102px");
		logo.addStyleName("logo");

		HorizontalLayout hl = new HorizontalLayout(desc, logo);
		hl.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
		hl.setSpacing(true);
		hl.setWidth("100%");
		hl.setExpandRatio(desc, 1);
		hl.addStyleName("logolayout");
		main.addComponent(hl);

		final EmailField username = new EmailField(Lang.get("login.username"));
		username.setWidth("100%");
		username.setImmediate(true);
		username.addStyleName("username");
		main.addComponent(username);

		final PasswordField password = new PasswordField(
				Lang.get("login.password"));
		password.setWidth("100%");
		password.setImmediate(true);
		password.addStyleName("password");
		main.addComponent(password);

		ValueChangeListener vlc = new ValueChangeListener() {

			private static final long serialVersionUID = 6817076897541522451L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				errorLabel.setValue("");
			}
		};
		username.addValueChangeListener(vlc);
		password.addValueChangeListener(vlc);

		errorLabel = new Label();
		errorLabel.addStyleName("error");
		main.addComponent(errorLabel);

		Button login = new Button(Lang.get("login.login"), new ClickListener() {

			private static final long serialVersionUID = 9111006710984400388L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.login(username.getValue(), password.getValue());
			}
		});
		login.setClickShortcut(KeyCode.ENTER);
		login.addStyleName("login");
		main.addComponent(login);

		// auto-fill username
		Cookie c = MainAppUI.getCurrent().getUsernameCookie();
		if (c != null) {
			username.setValue(c.getValue());
			password.focus();
		}

		Button forgotPass = new Button(Lang.get("login.forgotpass"));
		forgotPass.addStyleName(BaseTheme.BUTTON_LINK);
		forgotPass.addStyleName("forgotpass");
		main.addComponent(forgotPass);
		main.setComponentAlignment(forgotPass, Alignment.MIDDLE_CENTER);

		root.addComponent(main);
		root.setExpandRatio(main, 1);

		registerButton = new Button(Lang.get("login.register"));
		registerButton.setWidth("100%");
		registerButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -5189522876236967527L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (!register.isAttached()) {
					showRegister();
				} else {
					showMain();
				}
			}
		});

		LoginButton test = new LoginButton();
		test.setWidth("123px");
		test.setHeight("40px");

		// Button fbLogin = new Button(Lang.get("login.fb"));
		// TouchKitIcon.facebook.addTo(fbLogin);
		// fbLogin.setWidth("100%");
		// fbLogin.addClickListener(new ClickListener() {
		//
		// private static final long serialVersionUID = 8781936707317808266L;
		//
		// @Override
		// public void buttonClick(ClickEvent event) {
		// MainAppUI.getCurrent().loginFromFB();
		// }
		// });

		// Button twitterLogin = new Button(Lang.get("login.twitter"));
		// TouchKitIcon.twitter.addTo(twitterLogin);
		// twitterLogin.setWidth("100%");

		socialButtons = new HorizontalLayout(test, registerButton);
		socialButtons.addStyleName("socialbuttons");
		socialButtons.addStyleName("actionbuttons");
		socialButtons.setWidth("100%");
		socialButtons.setSpacing(true);
		socialButtons.setExpandRatio(registerButton, 1);
		socialButtons.setComponentAlignment(registerButton,
				Alignment.MIDDLE_RIGHT);
		root.addComponent(socialButtons);

	}

	protected void showRegister() {
		root.replaceComponent(main, register);
		root.setExpandRatio(register, 1);
		registerButton.setCaption(Lang.get("login.backfromregister"));
	}

	protected void showMain() {
		root.replaceComponent(register, main);
		root.setExpandRatio(main, 1);
		registerButton.setCaption(Lang.get("login.register"));
	}

	@Override
	@Inject
	public void injectPresenter(LoginPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showLoginError() {
		errorLabel.setValue(Lang.get("login.invalid"));
	}

	@Override
	public void showRegisterSuccess(User u) {
		Notification.show(Lang.get("login.welcome", u.getName()));
	}

	@Override
	public void showRegistrationError(String message) {
		register.showError(message);
	}

	@Override
	public void showRegister(String userId, String provider) {
		register = new RegisterLayout(userId, provider, presenter);
		showRegister();
	}

	@Override
	public void setLocale(Locale locale) {
		super.setLocale(locale);
		build(locale);
	}
}
