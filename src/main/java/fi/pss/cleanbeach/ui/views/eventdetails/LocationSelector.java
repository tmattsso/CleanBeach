package fi.pss.cleanbeach.ui.views.eventdetails;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.ui.util.Lang;

/**
 * TODO make this better (e.g. map, search..)
 * 
 * @author thomas
 * 
 */
public class LocationSelector extends NavigationView {

	private static final long serialVersionUID = -3614854496104509958L;

	public interface LocationSelectedListener {
		public void selected(Location loc);
	}

	public LocationSelector(final LocationSelectedListener listener,
			EventDetailsPresenter<?> presenter) {

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(true);
		setContent(vl);

		setCaption(Lang.get("events.locselect.caption"));

		BeanItemContainer<Location> cont = new BeanItemContainer<>(
				Location.class, presenter.getLocations());
		cont.sort(new Object[] { "name" }, new boolean[] { true });
		final NativeSelect cb = new NativeSelect();
		cb.setContainerDataSource(cont);
		cb.setItemCaptionPropertyId("name");
		cb.setWidth("100%");
		vl.addComponent(cb);

		Button select = new Button(Lang.get("events.locselect.select"));
		vl.addComponent(select);

		select.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 3416682137776534470L;

			@Override
			public void buttonClick(ClickEvent event) {
				listener.selected((Location) cb.getValue());
				getNavigationManager().navigateBack();
			}
		});
	}
}
