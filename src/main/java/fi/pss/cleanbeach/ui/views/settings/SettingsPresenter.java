package fi.pss.cleanbeach.ui.views.settings;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.ui.LogoutEvent;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class SettingsPresenter extends AbstractPresenter<ISettings> {

	@Inject
	private Event<LogoutEvent> event;

	@Inject
	private AuthenticationService aserv;

	@Override
	public void init(User currentUser) {

	}

	public void changePass(String value, User user) {
		aserv.changeUserPassword(user, value);
	}

	public void changeEmail(String value, User currentUser) {
		currentUser = aserv.changeUserEmail(currentUser, value);
		view.setUser(currentUser);
	}

	public void requestLogout() {
		event.fire(new LogoutEvent());
	}

	public void changeName(String value, User currentUser) {
		currentUser = aserv.changeUserName(currentUser, value);
		view.setUser(currentUser);
	}

}
