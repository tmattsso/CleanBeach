package fi.pss.cleanbeach.ui.views.events;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.util.ImageUtil;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.eventdetails.EventDetailsPresenter;

public class EventPanel extends CustomComponent {

	private static final long serialVersionUID = -6402313439815158102L;

	private final EventDetailsPresenter<?> presenter;

	public EventPanel(fi.pss.cleanbeach.data.Event e,
			EventDetailsPresenter<?> presenter) {
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
		root.setColumnExpandRatio(1, 1);
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

		Image i = ImageUtil.getGroupLogo(e.getOrganizer());
		if (i != null) {
			i.addStyleName("logosmall");
			root.addComponent(i, 0, 1, 0, 2);
		}

		Component collected = createCollectedComponent(e);
		root.addComponent(collected);
		root.setComponentAlignment(collected, Alignment.MIDDLE_CENTER);

		l = new Label(e.getNumComments() + " "
				+ Lang.get("events.eventpanel.numcomments") + "<br/>"
				+ e.getNumCommentsWithImage() + " "
				+ Lang.get("events.eventpanel.numpics"), ContentMode.HTML);
		l.setSizeUndefined();
		l.addStyleName("comments");
		root.addComponent(l);
		root.setComponentAlignment(l, Alignment.MIDDLE_CENTER);

		Label members = new Label();
		members.setSizeUndefined();
		members.addStyleName("people");
		if (i == null) {
			root.addComponent(members, 0, 2, 2, 2);
		} else {
			root.addComponent(members, 1, 2, 2, 2);
		}

		if (e.getJoinedUsers().isEmpty()) {

			members.setValue(Lang.get("events.details.joined.none"));
		} else {
			Iterator<User> users = e.getJoinedUsers().iterator();

			String content = Lang.get("events.details.joined.volunteers", e
					.getJoinedUsers().size())
					+ " " + users.next().getName();
			if (e.getJoinedUsers().size() > 2) {
				members.setValue(content + ", " + users.next().getName() + ", "
						+ Lang.get("events.details.joined.andseparator") + " "
						+ (e.getJoinedUsers().size() - 2) + " "
						+ Lang.get("events.details.joined.more"));
			} else if (e.getJoinedUsers().size() == 2) {
				members.setValue(content + ", " + users.next().getName());
			} else if (e.getJoinedUsers().size() == 1) {
				members.setValue(content);
			}
		}

		root.addLayoutClickListener(new LayoutClickListener() {

			private static final long serialVersionUID = 2235608101030585861L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				presenter.openSingleEvent(e);
			}
		});
	}

	protected Component createCollectedComponent(fi.pss.cleanbeach.data.Event e) {
		Label label = new Label("<div><span>" + e.getThrash().getTotalNum()
				+ "</span></div><br/>"
				+ Lang.get("events.eventpanel.numpieces"), ContentMode.HTML);
		label.addStyleName("numpieces");
		label.setWidth("100px");
		return label;
	}
}
