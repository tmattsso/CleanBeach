package fi.pss.cleanbeach.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class Stepper extends CustomField<Integer> {

	private static final long serialVersionUID = -6591416612233324201L;

	private final Label currentValue = new Label();

	public Stepper(int initial) {
		setValue(initial);
		setSizeUndefined();
		setImmediate(true);
		setStyleName("stepper");
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	protected Component initContent() {
		Button minus = new Button(FontAwesome.MINUS);
		Button plus = new Button(FontAwesome.PLUS);

		currentValue.setValue(getValue() + "");
		currentValue.setWidth("100px");

		HorizontalLayout root = new HorizontalLayout(minus, currentValue, plus);
		root.setSpacing(true);
		root.setComponentAlignment(currentValue, Alignment.MIDDLE_CENTER);

		minus.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -198862221678984094L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (getValue() > 0) {
					setValue(getValue() - 1);
				}
			}
		});
		plus.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -198862221678984094L;

			@Override
			public void buttonClick(ClickEvent event) {
				setValue(getValue() + 1);
			}
		});

		return root;
	}

	@Override
	protected void setInternalValue(Integer newValue) {
		super.setInternalValue(newValue);
		currentValue.setValue(String.valueOf(getValue()));
	}

}
