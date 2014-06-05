package arquilliantests;

import java.util.Locale;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.views.login.ILogin;

public class DummyLogin implements ILogin {

	boolean loginErrorShown;

	@Override
	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showErrorNotification(String caption, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorNotification(String caption) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUser(User current) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocale(Locale lang) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showLoginError() {
		loginErrorShown = true;
	}

	@Override
	public void showRegister(String userId, String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showRegisterSuccess(User u) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showRegistrationError(String message) {
		// TODO Auto-generated method stub

	}

}
