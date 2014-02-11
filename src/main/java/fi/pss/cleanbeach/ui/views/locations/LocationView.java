package fi.pss.cleanbeach.ui.views.locations;

import javax.inject.Inject;

import org.vaadin.addon.leaflet.LMap;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.mvp.AbstractView;

@UIScoped
public class LocationView extends AbstractView<LocationPresenter> implements
		ILocation {

	private static final long serialVersionUID = 6914178286159188531L;
	LMap lMap = new LitterBaseMap();

	@Override
	@Inject
	public void injectPresenter(LocationPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	protected ComponentContainer getMainContent() {

		setCaption("Törkykartal");

		Button addLocation = new Button("add");
		addLocation.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});

		CssLayout actionButtons = new CssLayout(addLocation);

		VerticalLayout vl = new VerticalLayout(lMap, actionButtons);
		vl.setExpandRatio(lMap, 1);

		return lMap;
	}

}
