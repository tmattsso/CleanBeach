package fi.pss.cleanbeach.ui.views.events;

import java.io.Serializable;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class EventsPresenter extends AbstractPresenter<IEvents> implements
		Serializable {

	private static final long serialVersionUID = 3951507517016979359L;

	protected EventsPresenter() {
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
