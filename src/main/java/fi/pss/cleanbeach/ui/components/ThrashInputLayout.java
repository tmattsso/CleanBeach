package fi.pss.cleanbeach.ui.components;

import java.util.List;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;

import fi.pss.cleanbeach.data.ThrashType;

public abstract class ThrashInputLayout extends NavigationView {

	private static final long serialVersionUID = -8058030751028066774L;

	private GridLayout gl;
	private TextArea otherDescField;

	public ThrashInputLayout() {

		addStyleName("thrashinput");

		((NavigationButton) getLeftComponent())
				.addClickListener(new NavigationButtonClickListener() {

					private static final long serialVersionUID = -7901488627243106710L;

					@Override
					public void buttonClick(NavigationButtonClickEvent event) {
						navigateAway();
					}
				});
	}

	protected void build() {

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

		ThrashType otherType = null;
		for (ThrashType t : getThrashTypes()) {
			if (!t.isOther()) {
				addRow(t);
			} else {
				otherType = t;
			}
		}

		if (otherType != null) {
			addRow(otherType);

			// do a trick to add row to GL
			Label temp = new Label();
			gl.addComponent(temp);
			gl.removeComponent(temp);

			otherDescField = new TextArea();
			otherDescField.setWidth("100%");
			otherDescField.setRows(3);
			otherDescField.setImmediate(true);
			otherDescField.setNullRepresentation("");
			otherDescField.setInputPrompt("what did you find?");
			gl.addComponent(otherDescField, 0, gl.getRows() - 1, 1,
					gl.getRows() - 1);

			otherDescField.setValue(getCurrentDesc(otherType));

			final ThrashType type = otherType;
			otherDescField.addValueChangeListener(new ValueChangeListener() {

				private static final long serialVersionUID = -5076399054112044132L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					setDesc(type, otherDescField.getValue());
				}
			});
		}
	}

	@Override
	public void attach() {
		super.attach();
		build();
	}

	protected abstract void navigateAway();

	protected abstract String getCurrentDesc(ThrashType t);

	protected abstract void setDesc(ThrashType type, String value);

	protected abstract List<ThrashType> getThrashTypes();

	private void addRow(final ThrashType t) {
		Label name = new Label(t.getName() + ":");
		name.addStyleName("typename");
		gl.addComponent(name);
		gl.setComponentAlignment(name, Alignment.MIDDLE_LEFT);

		final Stepper s = new Stepper(getValue(t));
		gl.addComponent(s);
		s.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -993524115746739524L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				storeValue(t, s.getValue());
			}
		});
	}

	protected abstract void storeValue(ThrashType type, Integer value);

	protected abstract int getValue(ThrashType type);

}
