package fi.pss.cleanbeach.ui.mvp;

import java.util.Locale;

import fi.pss.cleanbeach.data.User;

public interface IView {

	public User getUser();

	void showErrorNotification(String caption, String msg);

	void showErrorNotification(String caption);

	public void setUser(User current);

	void setLocale(Locale lang);
}
