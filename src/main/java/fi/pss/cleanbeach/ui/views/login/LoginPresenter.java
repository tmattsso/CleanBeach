package fi.pss.cleanbeach.ui.views.login;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService;
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
			view.showError();
		}

	}

}
