package fi.pss.cleanbeach.standalone.map;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.ui.util.Lang;

@UIScoped
public class EventDetails extends VerticalLayout {

	private static final long serialVersionUID = -5947426357994408475L;

	private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	@Inject
	private EventService eService;

	public EventDetails() {
		addStyleName("details");
		setWidth("400px");
		setMargin(true);

	}

	public void update(fi.pss.cleanbeach.data.Event e) {

		if (getParent() != null) {
			((SingleComponentContainer) getParent()).setContent(null);
		}
		removeAllComponents();

		e = eService.loadDetails(e);

		Label caption = new Label(e.getLocation().getName());
		caption.addStyleName("caption");
		addComponent(caption);

		Label time = new Label(Lang.get("map.details.timeline",
				df.format(e.getStart()))
				+ "&nbsp;<span>" + e.getOrganizer().getName() + "</span>");
		time.setContentMode(ContentMode.HTML);
		time.addStyleName("time");
		addComponent(time);

		Label desc = new Label(e.getDescription());
		desc.addStyleName("desc");
		addComponent(desc);

		Label joined = new Label(Lang.get("map.details.joinedline", e
				.getJoinedUsers().size(), e.getThrash().getTotalNum()));
		joined.addStyleName("joined");
		addComponent(joined);
	}
}
