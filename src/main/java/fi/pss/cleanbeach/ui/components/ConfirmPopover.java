package fi.pss.cleanbeach.ui.components;

import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import fi.pss.cleanbeach.ui.util.Lang;

public class ConfirmPopover extends Popover {

	private static final long serialVersionUID = -3363629662075879130L;

	public interface ConfirmListener {
		public void confirmed();
	}

	public ConfirmPopover(final ConfirmListener l, String caption) {

		addStyleName("confirm");

		setWidth("400px");

        String shortened = null;
        if(caption.length() > 30) {
            shortened = caption.substring(0,27) + "...";
        }

        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(true);
        vl.setSpacing(true);
        NavigationView navigationView = new NavigationView(caption);
        HorizontalButtonGroup components = new HorizontalButtonGroup();
        if(shortened != null) {
            navigationView.setCaption(shortened);
            vl.addComponents(new Label(caption));
        }
        vl.addComponents(components);

        navigationView.setHeight("160px");
        components.setWidth("100%");
        navigationView.setContent(vl);

		Button ok = new Button(Lang.get("global.yes"));
		ok.setWidth("50%");
		ok.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 7082960292870880295L;

            @Override
            public void buttonClick(ClickEvent event) {
                l.confirmed();
                close();
            }
        });

		Button cancel = new Button(Lang.get("global.cancel"));
		cancel.setWidth("50%");
		cancel.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 6353147704433465730L;

            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });

        components.addComponents(ok,cancel);
        setContent(navigationView);
	}
}
