package fi.pss.cleanbeach.ui.views.eventdetails;

import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerState.Resolution;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.group.LocationSelector;
import fi.pss.cleanbeach.ui.views.group.LocationSelector.LocationSelectedListener;

public class CreateEventLayout extends NavigationView {

	private static final long serialVersionUID = 9206488173205979006L;

	private final CreateEventPresenter<?> presenter;

	private Location loc;
	private final UsersGroup group;

	public CreateEventLayout(final UsersGroup g, final Location l,
			final CreateEventPresenter<?> presenter) {
		group = g;
		loc = l;
		this.presenter = presenter;

		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		root.setHeight("100%");
		root.addStyleName("createevent");
		setContent(root);

		setCaption(Lang.get("events.create.caption"));

		final TextField desc = new TextField(Lang.get("events.create.desc"));
		desc.setRequired(true);
		desc.setWidth("100%");
		root.addComponent(desc);

		final DatePicker start = new DatePicker(Lang.get("events.create.start"));
		start.setResolution(Resolution.TIME);
		start.setRequired(true);
		start.setWidth("100%");
		root.addComponent(start);

		final Button locationSelect = new Button(
				Lang.get("events.create.noloc"));
		locationSelect.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 7283240300836901481L;

			@Override
			public void buttonClick(ClickEvent event) {
				getNavigationManager().navigateTo(
						new LocationSelector(new LocationSelectedListener() {

							@Override
							public void selected(Location loc) {
								CreateEventLayout.this.loc = loc;
								locationSelect.setCaption(loc.getName());
							}
						}, presenter));
			}
		});
		root.addComponent(locationSelect);
		root.setExpandRatio(locationSelect, 1);

		Button create = new Button(Lang.get("events.create.create"));
		create.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2735729970887726549L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (!desc.isValid() || !start.isValid()) {
					Notification.show(Lang.get("events.create.fillall"));
					return;
				}
				if (loc == null) {
					Notification.show(Lang.get("events.create.selectloc"));
					return;
				}

				CreateEventLayout.this.presenter.createEvent(group,
						desc.getValue(), start.getValue(), loc);
			}
		});
		root.addComponent(create);

	}
}
