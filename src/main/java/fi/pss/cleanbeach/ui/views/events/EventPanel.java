package fi.pss.cleanbeach.ui.views.events;

import java.text.SimpleDateFormat;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class EventPanel extends CustomComponent {

	private static final long serialVersionUID = -6402313439815158102L;

	public EventPanel(final fi.pss.cleanbeach.data.Event e,
			final EventsPresenter presenter) {

		setWidth("100%");
		addStyleName("eventbox");

		GridLayout root = new GridLayout(3, 3);
		root.setSpacing(true);
		setCompositionRoot(root);
		root.setWidth("100%");
		root.setColumnExpandRatio(0, 1);
		root.setRowExpandRatio(1, 1);

		Label l = new Label(e.getLocation().getName());
		l.setSizeUndefined();
		root.addComponent(l, 0, 0, 1, 0);

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		l = new Label(df.format(e.getStart()));
		l.setSizeUndefined();
		root.addComponent(l);

		// group logo
		HorizontalLayout hl = new HorizontalLayout();
		root.addComponent(hl);

		l = new Label("101<br/><span>pieces collected</span>", ContentMode.HTML);
		l.addStyleName("numpieces");
		l.setWidth("100px");
		root.addComponent(l);

		l = new Label(e.getNumComments() + " Comments<br/>"
				+ e.getNumCommentsWithImage() + " Pictures", ContentMode.HTML);
		l.setSizeUndefined();
		root.addComponent(l);
		root.setComponentAlignment(l, Alignment.MIDDLE_CENTER);

		l = new Label("Joined people", ContentMode.HTML);
		l.setSizeUndefined();
		root.addComponent(l, 0, 2, 2, 2);

		root.addLayoutClickListener(new LayoutClickListener() {

			private static final long serialVersionUID = 2235608101030585861L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				presenter.openSingleEvent(e);
			}
		});
	}
}
