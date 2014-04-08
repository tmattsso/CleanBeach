package fi.pss.cleanbeach.ui.mvp;

import fi.pss.cleanbeach.data.User;

public interface IView {

	public User getUser();

	void showErrorNotification(String caption, String msg);

	void showErrorNotification(String caption);
}
