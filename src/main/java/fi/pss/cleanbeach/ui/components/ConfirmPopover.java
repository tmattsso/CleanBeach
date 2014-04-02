package fi.pss.cleanbeach.ui.components;

import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.ui.util.Lang;

public class ConfirmPopover extends Popover {

	private static final long serialVersionUID = -3363629662075879130L;

	public interface ConfirmListener {
		public void confirmed();
	}

	public ConfirmPopover(final ConfirmListener l, String caption) {

		addStyleName("confirm");

		setWidth("80%");
		center();
		setModal(true);

		GridLayout gl = new GridLayout(2, 2);
		gl.setWidth("100%");
		gl.setMargin(true);
		gl.setSpacing(true);
		setContent(gl);

		Label desc = new Label(caption);
		gl.addComponent(desc, 0, 0, 1, 0);

		Button ok = new Button(Lang.get("global.yes"));
		ok.setWidth("100%");
		ok.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 7082960292870880295L;

			@Override
			public void buttonClick(ClickEvent event) {
				l.confirmed();
				close();
			}
		});
		gl.addComponent(ok);
		gl.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);

		Button cancel = new Button(Lang.get("global.cancel"));
		cancel.setWidth("100%");
		cancel.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 6353147704433465730L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		gl.addComponent(cancel);
	}
}
