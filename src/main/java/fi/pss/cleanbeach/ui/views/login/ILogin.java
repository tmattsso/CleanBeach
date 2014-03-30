package fi.pss.cleanbeach.ui.views.login;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.mvp.IView;

public interface ILogin extends IView {

	void showLoginError();

	void showRegisterSuccess(User u);

	void showRegistrationError(String message);

}
