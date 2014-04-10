package fi.pss.cleanbeach.ui.views.group;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Location;

public class LocationSelector extends NavigationView {

	private static final long serialVersionUID = -3614854496104509958L;

	public interface LocationSelectedListener {
		public void selected(Location loc);
	}

	public LocationSelector(final LocationSelectedListener listener,
			GroupPresenter presenter) {

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(true);
		setContent(vl);

		setCaption("[selec location");

		BeanItemContainer<Location> cont = new BeanItemContainer<>(
				Location.class, presenter.getLocations());
		cont.sort(new Object[] { "name" }, new boolean[] { true });
		final NativeSelect cb = new NativeSelect();
		cb.setContainerDataSource(cont);
		cb.setItemCaptionPropertyId("name");
		cb.setWidth("100%");
		vl.addComponent(cb);

		Button select = new Button("[sel");
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
