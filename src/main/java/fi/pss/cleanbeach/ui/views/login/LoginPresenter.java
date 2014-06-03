package fi.pss.cleanbeach.ui.views.login;

import java.util.Locale;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.vaadin.se.facebook.FacebookListener;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.services.AuthenticationService.NoSuchUser;
import fi.pss.cleanbeach.services.AuthenticationService.RegistrationException;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;
import fi.pss.cleanbeach.ui.util.Lang;

@UIScoped
public class LoginPresenter extends AbstractPresenter<ILogin> implements
		FacebookListener {

	@Inject
	private AuthenticationService authService;

	@Inject
	private Event<LoginEvent> login;

	protected LoginPresenter() {
	}

	@Override
	public void init(User currentUser) {
	}

	public void login(String user, String pass) {

		User u = authService.login(user, pass);

		if (u != null) {
			// main view takes over
			login.fire(new LoginEvent(u));
		} else {
			view.showLoginError();
		}

	}

	public void register(String name, String email, String pass, String id,
			String provider) {

		User u = new User();
		u.setName(name);
		u.setEmail(email);
		u.setOid(id);
		u.setOidProvider(provider);

		try {
			u = authService.createUser(u, pass);
			view.showRegisterSuccess(u);
			login.fire(new LoginEvent(u));
		} catch (RegistrationException e) {
			view.showRegistrationError(Lang.get(e.getFault()));
		}
	}

	@Override
	public void onFacebookPostCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFacebookPost(String postId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFacebookLogout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFacebookLogin(String userId) {

		User user;
		try {
			user = authService.getUser(userId,
					AuthenticationService.PROVIDER_FB);
			login.fire(new LoginEvent(user));
		} catch (NoSuchUser e) {
			// let user give info and register
			view.showRegister(userId, AuthenticationService.PROVIDER_FB);
		}

	}

	public void changeLang(Locale lang) {
		view.setLocale(lang);
	}

}
