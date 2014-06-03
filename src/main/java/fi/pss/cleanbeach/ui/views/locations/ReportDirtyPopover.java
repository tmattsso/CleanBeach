package fi.pss.cleanbeach.ui.views.locations;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.util.Lang;

public class ReportDirtyPopover extends Popover {

	private static final long serialVersionUID = -1805148322449517833L;

	public interface ConfirmThrashListener {
		public void confirm(String desc);
	}

	public ReportDirtyPopover(final ConfirmThrashListener l) {

		addStyleName("reportdirty");
		setHeight(null);

		Label description = new Label(Lang.get("locations.report.desc"));
		final TextArea tf = new TextArea();
		tf.setRows(5);
		tf.setWidth("100%");
		tf.focus();
		Button create = new Button(Lang.get("locations.report.submit"));
		create.addStyleName("submit");
		TouchKitIcon.plus.addTo(create);
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
				if (tf.getValue() != null && tf.getValue().length() > 0) {
					l.confirm(tf.getValue());
					close();
				}
			}
		});
	}
}
