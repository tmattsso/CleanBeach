package fi.pss.cleanbeach.ui.views.settings;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class SettingsPresenter extends AbstractPresenter<ISettings> {

	@Override
	public void init(User currentUser) {
		// TODO Auto-generated method stub

	}

	public void changePass(String value, User currentUser) {
		// TODO Auto-generated method stub

	}

}
