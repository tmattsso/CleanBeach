package unittests;

import javax.enterprise.event.Event;

import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.ui.views.login.LoginEvent;
import fi.pss.cleanbeach.ui.views.login.LoginPresenter;
import fi.pss.cleanbeach.ui.views.login.LoginView;

public class Presenter extends LoginPresenter {
	public void setAuthService(AuthenticationService ser) {
		authService = ser;
	}

	public void setEvent(Event<LoginEvent> e) {
		login = e;
	}

	public void setView(LoginView view) {
		this.view = view;
	}
}