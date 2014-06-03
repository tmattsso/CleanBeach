package fi.pss.cleanbeach.ui.views.locations;

import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.util.Lang;

public class CreateLocationPopover extends Popover {

	private static final long serialVersionUID = -1805148322449517833L;

	public interface ConfirmLocationListener {
		public void confirm(String name);
	}

	public CreateLocationPopover(final ConfirmLocationListener l) {

		addStyleName("createlocation");
		setHeight(null);
		setModal(true);

		Label description = new Label(Lang.get("locations.addloc.name"));
		final TextField tf = new TextField();
		tf.setWidth("100%");
		tf.focus();
		Button create = new Button(Lang.get("locations.addloc.create"));
		create.setIcon(FontAwesome.PLUS);
		create.setClickShortcut(KeyCode.ENTER);
		VerticalLayout vl = new VerticalLayout(description, tf, create);
		vl.addStyleName("actionbuttons");
		vl.setMargin(true);
		vl.setSpacing(true);
		setContent(vl);

		create.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 4638093591745420447L;

			@Override
			public void buttonClick(ClickEvent event) {
				l.confirm(tf.getValue());
				close();
			}
		});
	}
}
