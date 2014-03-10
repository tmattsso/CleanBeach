package fi.pss.cleanbeach.ui.views.events;

import java.text.SimpleDateFormat;
import java.util.Collection;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

public abstract class AbstractEventPanel<P extends IEventPresenter> extends
		CustomComponent {

	private static final long serialVersionUID = 5844647536868509786L;

	private final P presenter;

	public AbstractEventPanel(final fi.pss.cleanbeach.data.Event e,
			final P presenter) {

		this.presenter = presenter;

		setWidth("100%");
		addStyleName("eventbox");

		update(e);
	}

	public void update(final fi.pss.cleanbeach.data.Event e) {
		GridLayout root = new GridLayout(3, 3);
		root.setSpacing(true);
		root.setMargin(true);
		setCompositionRoot(root);
		root.setWidth("100%");
		root.setColumnExpandRatio(0, 1);
		root.setRowExpandRatio(1, 1);

		Label l = new Label(e.getLocation().getName());
		l.setSizeUndefined();
		l.addStyleName("location");
		root.addComponent(l, 0, 0, 1, 0);

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		l = new Label(df.format(e.getStart()));
		l.setSizeUndefined();
		l.addStyleName("date");
		root.addComponent(l);

		for (Component component : createContent(e)) {
			root.addComponent(component);
		}

		l = new Label(e.getNumComments() + " Comments<br/>"
				+ e.getNumCommentsWithImage() + " Pictures", ContentMode.HTML);
		l.setSizeUndefined();
		l.addStyleName("comments");
		root.addComponent(l);
		root.setComponentAlignment(l, Alignment.MIDDLE_CENTER);

		l = new Label("Joined people", ContentMode.HTML);
		l.setSizeUndefined();
		l.addStyleName("people");
		root.addComponent(l, 0, 2, 2, 2);

		root.addLayoutClickListener(new LayoutClickListener() {

			private static final long serialVersionUID = 2235608101030585861L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				presenter.openSingleEvent(e);
			}
		});
	}

	protected abstract Collection<? extends Component> createContent(
			fi.pss.cleanbeach.data.Event e);

	protected P getPresenter() {
		return presenter;
	}

	protected Component createCollectedComponent(fi.pss.cleanbeach.data.Event e) {
		Label label = new Label("<div><span>" + e.getThrash().getTotalNum()
				+ "</span></div><br/>pieces collected", ContentMode.HTML);
		label.addStyleName("numpieces");
		label.setWidth("100px");
		return label;
	}
}
