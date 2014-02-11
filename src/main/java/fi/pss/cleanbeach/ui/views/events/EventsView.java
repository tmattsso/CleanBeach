package fi.pss.cleanbeach.ui.views.events;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.mvp.AbstractView;

@UIScoped
public class EventsView extends AbstractView<EventsPresenter> {

	private static final long serialVersionUID = -259521650823470699L;

	public EventsView() {
		setCaption("Events");
	}

	@Override
	protected ComponentContainer getMainContent() {
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addStyleName("login");

		return vl;
	}

	@Override
	@Inject
	public void injectPresenter(EventsPresenter presenter) {
		this.presenter = presenter;
	}

}
