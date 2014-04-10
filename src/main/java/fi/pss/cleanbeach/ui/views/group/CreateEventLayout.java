package fi.pss.cleanbeach.ui.views.group;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.views.group.LocationSelector.LocationSelectedListener;

public class CreateEventLayout extends NavigationView {

	private static final long serialVersionUID = 9206488173205979006L;

	private final GroupPresenter presenter;

	private Location loc;

	public CreateEventLayout(final UsersGroup group,
			final GroupPresenter presenter) {
		this.presenter = presenter;

		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		root.addStyleName("createevent");
		setContent(root);

		root.addComponent(new Label("[create event"));

		final TextField desc = new TextField("[desc");
		desc.setRequired(true);
		desc.setWidth("100%");
		root.addComponent(desc);

		final DateField start = new DateField("[start time");
		start.setResolution(Resolution.HOUR);
		root.addComponent(start);

		final Button locationSelect = new Button("[no loc");
		locationSelect.addClickListener(new ClickListener() {

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

		Button create = new Button("[create");
		create.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if (loc == null) {
					Notification.show("[please select loc");
					return;
				}

				CreateEventLayout.this.presenter.createEvent(group,
						desc.getValue(), start.getValue(), loc);
			}
		});
		root.addComponent(create);
	}
}
