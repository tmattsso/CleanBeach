package fi.pss.cleanbeach.ui.view.login;

import javax.inject.Inject;
import javax.servlet.http.Cookie;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.mvp.AbstractView;

@CDIView("login")
public class LoginView extends AbstractView<LoginPresenter> {

	private static final long serialVersionUID = -259521650823470699L;

	private Label errorLabel;

	public LoginView() {
	}

	@Override
	protected ComponentContainer getContent() {
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addStyleName("login");

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
		vl.addComponent(errorLabel);

		Button login = new Button("Login", new ClickListener() {

			private static final long serialVersionUID = 9111006710984400388L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!((MyTouchKitUI) UI.getCurrent()).login(
						username.getValue(), password.getValue())) {
					errorLabel.setValue("Invalid credentials");
				}
			}
		});
		login.setClickShortcut(KeyCode.ENTER);
		login.addStyleName("login");
		vl.addComponent(login);

		// auto-fill username
		Cookie c = MyTouchKitUI.getUsernameCookie();
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

		return vl;
	}

	@Override
	@Inject
	public void injectPresenter(LoginPresenter presenter) {
		this.presenter = presenter;
	}

}
