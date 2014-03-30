package fi.pss.cleanbeach.ui.views.login;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.services.AuthenticationService.RegistrationException;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class LoginPresenter extends AbstractPresenter<ILogin> implements
		Serializable {

	private static final long serialVersionUID = 3951507517016979359L;

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

	public void register(String name, String email, String pass) {

		User u = new User();
		u.setName(name);
		u.setEmail(email);
		try {
			u = authService.createUser(u, pass);
			view.showRegisterSuccess(u);
			login.fire(new LoginEvent(u));
		} catch (RegistrationException e) {
			String msg = null;
			switch (e.getFault()) {
			case EMAILALREADYINUSE:
				msg = "[";
				break;
			case PASSTOOSHORT:
				msg = "[";
				break;
			case NOEMAIL:
				msg = "[";
				break;
			case NONAME:
				msg = "[";
				break;
			case NOPASS:
				msg = "[";
				break;
			case EMAILNOTVALID:
				msg = "[";
				break;
			}
			view.showRegistrationError(msg);
		}
	}

}
