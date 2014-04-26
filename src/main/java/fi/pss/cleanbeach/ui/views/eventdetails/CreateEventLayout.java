package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerState.Resolution;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.eventdetails.LocationSelector.LocationSelectedListener;

public class CreateEventLayout extends NavigationView {

	private static final long serialVersionUID = 9206488173205979006L;

	private final EventDetailsPresenter<?> presenter;

	private Location loc;
	private UsersGroup group;

	private fi.pss.cleanbeach.data.Event e;

	public CreateEventLayout(final UsersGroup g, final Location l,
			final EventDetailsPresenter<?> presenter) {
		group = g;
		loc = l;

		this.presenter = presenter;

		create();
	}

	public CreateEventLayout(fi.pss.cleanbeach.data.Event e,
			final EventDetailsPresenter<?> presenter) {
		this.e = e;
		this.presenter = presenter;

		create();
	}

	private void create() {

		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		root.addStyleName("createevent");
		setContent(root);

		if (e == null) {
			setCaption(Lang.get("events.create.caption"));
		} else {
			setCaption(Lang.get("events.create.caption.edit"));
		}

		final TextArea desc = new TextArea(Lang.get("events.create.desc"));
		desc.setRows(5);
		desc.setRequired(true);
		desc.setWidth("100%");
		root.addComponent(desc);

		if (e != null) {
			desc.setValue(e.getDescription());
		}

		// desktop or mobile?

		final AbstractField<Date> start;
		if (MainAppUI.getCurrent().getPage().getWebBrowser().isTouchDevice()) {
			start = new DatePicker(Lang.get("events.create.start"));
			((DatePicker) start).setResolution(Resolution.TIME);
		} else {
			start = new InlineDateField(Lang.get("events.create.start"));
			((DateField) start)
					.setResolution(com.vaadin.shared.ui.datefield.Resolution.MINUTE);
		}
		start.setRequired(true);
		start.setLocale(new Locale("fi", "FI"));
		start.setRequired(true);
		start.setWidth("100%");
		root.addComponent(start);

		if (e != null) {
			start.setValue(e.getStart());
		}

		if (loc == null && e == null) {
			final Button locationSelect = new Button(
					Lang.get("events.create.noloc"));
			locationSelect.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 7283240300836901481L;

				@Override
				public void buttonClick(ClickEvent event) {
					getNavigationManager().navigateTo(
							new LocationSelector(
									new LocationSelectedListener() {

										@Override
										public void selected(Location loc) {
											CreateEventLayout.this.loc = loc;
											locationSelect.setCaption(loc
													.getName());
										}
									}, presenter));
				}
			});
			root.addComponent(locationSelect);
			root.setExpandRatio(locationSelect, 1);
		}

		if (group == null && e == null) {
			presenter.updateUser(MainAppUI.getCurrentUser());

			Set<UsersGroup> groups = MainAppUI.getCurrentUser().getMemberIn();
			Set<UsersGroup> adminIn = new HashSet<>();
			for (UsersGroup ug : groups) {
				if (ug.isAdmin(MainAppUI.getCurrentUser())) {
					adminIn.add(ug);
				}
			}

			if (adminIn.isEmpty()) {
				root.removeAllComponents();
				Label noGroups = new Label(Lang.get("events.create.notadmin"));
				root.addComponent(noGroups);
				return;
			}

			final NativeSelect select = new NativeSelect(
					Lang.get("events.groupselect.caption"));
			for (UsersGroup ug : groups) {
				select.addItem(ug);
				select.setItemCaption(ug, ug.getName());
			}
			select.setRequired(true);
			select.setNullSelectionAllowed(false);
			select.setWidth("100%");
			root.addComponent(select);

			select.addValueChangeListener(new ValueChangeListener() {

				private static final long serialVersionUID = -2371910476692810249L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					group = (UsersGroup) select.getValue();
				}
			});
		}

		Button create = new Button();
		if (e == null) {
			create.setCaption(Lang.get("events.create.create"));
		} else {
			create.setCaption(Lang.get("events.create.save"));
		}
		create.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2735729970887726549L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (!desc.isValid() || !start.isValid()) {
					Notification.show(Lang.get("events.create.fillall"));
					return;
				}
				if (loc == null && e == null) {
					Notification.show(Lang.get("events.create.selectloc"));
					return;
				}
				if (group == null && e == null) {
					Notification.show(Lang.get("events.create.selectgroup"));
					return;
				}

				if (e == null) {
					presenter.createEvent(group, desc.getValue(),
							start.getValue(), loc);
				} else {
					presenter.saveEvent(e, desc.getValue(), start.getValue());
				}
			}
		});
		root.addComponent(create);

	}

}
