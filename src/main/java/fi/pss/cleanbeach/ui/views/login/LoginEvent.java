package fi.pss.cleanbeach.ui.views.login;

import fi.pss.cleanbeach.data.User;

public class LoginEvent {

	public User user;

	public LoginEvent(User u) {
		user = u;
	}
}
