package fi.pss.cleanbeach.ui.views.events;

import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.components.Stepper;

public class ThrashInputLayout extends NavigationView {

	private static final long serialVersionUID = -8058030751028066774L;

	private final GridLayout gl;

	private final EventsPresenter presenter;
	private final fi.pss.cleanbeach.data.Event event;

	public ThrashInputLayout(fi.pss.cleanbeach.data.Event e,
			final EventsPresenter presenter) {

		this.presenter = presenter;
		event = e;

		addStyleName("thrashinput");

		gl = new GridLayout(2, 1);
		gl.setSpacing(true);
		gl.setMargin(true);
		gl.setColumnExpandRatio(0, 1);
		gl.setWidth("100%");
		setContent(gl);

		Label caption = new Label("Mark your collected thrash here:");
		caption.addStyleName("caption");
		caption.setHeight("30px");
		gl.addComponent(caption, 0, 0, 1, 0);

		for (ThrashType t : presenter.getThrashTypes()) {
			addRow(t);
		}

	}

	@Override
	public void attach() {
		super.attach();
		getNavigationManager().addNavigationListener(new NavigationListener() {

			@Override
			public void navigate(NavigationEvent event) {
				// navigate back, update details view
				presenter.navigatedFromThrash(ThrashInputLayout.this.event);
			}
		});
	}

	private void addRow(final ThrashType t) {
		Label name = new Label(t.getName() + ":");
		name.addStyleName("typename");
		gl.addComponent(name);
		gl.setComponentAlignment(name, Alignment.MIDDLE_LEFT);

		final Stepper s = new Stepper(event.getThrash().getOfTypeForUser(t,
				MyTouchKitUI.getCurrentUser()));
		gl.addComponent(s);
		s.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -993524115746739524L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				presenter.setNumThrash(s.getValue(), t,
						ThrashInputLayout.this.event,
						MyTouchKitUI.getCurrentUser());
			}
		});
	}

}
