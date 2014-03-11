package fi.pss.cleanbeach.ui.views.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Comment;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.util.Lang;

public class EventDetailLayout extends NavigationView {

	private static final long serialVersionUID = 4303038489004245363L;

	private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	private final EventsPresenter presenter;

	private fi.pss.cleanbeach.data.Event selectedEvent;

	private final Label caption;
	private final Label desc;
	private final Label members;
	private final Label creator;

	private final HorizontalLayout actions;

	private final VerticalLayout comments;

	private Button itemsButton;

	public EventDetailLayout(final fi.pss.cleanbeach.data.Event e,
			final EventsPresenter presenter) {

		this.presenter = presenter;
		selectedEvent = e;

		addStyleName("eventdetail");
		setCaption(Lang.get("events.details.caption"));

		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		setContent(content);

		caption = new Label("", ContentMode.HTML);
		caption.addStyleName("eventcaption");
		content.addComponent(caption);

		GridLayout gl = new GridLayout(3, 2);
		gl.setWidth("100%");
		gl.setHeight("150px");
		gl.setSpacing(true);
		gl.setColumnExpandRatio(0, 1);
		gl.setRowExpandRatio(0, 1);
		content.addComponent(gl);

		Component map = getMap(e.getLocation());
		map.setSizeFull();
		gl.addComponent(map, 0, 0, 0, 1);

		Component items = getItemsButton(e);
		gl.addComponent(items, 1, 0, 2, 0);

		Button fb = new Button();
		fb.setWidth("100%");
		fb.setEnabled(false);
		TouchKitIcon.facebookSign.addTo(fb);
		gl.addComponent(fb, 1, 1);

		Button twitter = new Button();
		twitter.setWidth("100%");
		twitter.setEnabled(false);
		TouchKitIcon.twitterSign.addTo(twitter);
		gl.addComponent(twitter, 2, 1);

		desc = new Label();
		desc.addStyleName("eventdescriptions");
		content.addComponent(desc);

		creator = new Label();
		creator.addStyleName("eventcreator");
		content.addComponent(creator);

		members = new Label();
		members.addStyleName("eventmembers");
		content.addComponent(members);

		actions = new HorizontalLayout();
		actions.addStyleName("actionbuttons");
		actions.setVisible(false);
		actions.setWidth("100%");
		actions.setSpacing(true);
		content.addComponent(actions);

		Button comment = new Button(Lang.get("events.details.comment"));
		TouchKitIcon.comment.addTo(comment);
		comment.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 3214768233962554854L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.openAddComment(false, selectedEvent);
			}
		});
		actions.addComponent(comment);

		Button photo = new Button(Lang.get("events.details.photo"));
		TouchKitIcon.cameraRetro.addTo(photo);
		photo.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -5417239994058318781L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.openAddComment(true, selectedEvent);
			}
		});
		actions.addComponent(photo);

		Button addThrash = new Button(Lang.get("events.details.thrash"));
		TouchKitIcon.exclamationSign.addTo(addThrash);
		addThrash.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2718050820972847459L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.openAddThrash(selectedEvent);
			}
		});
		actions.addComponent(addThrash);

		Button invite = new Button(Lang.get("events.details.invite"));
		TouchKitIcon.user.addTo(invite);
		invite.setEnabled(false);
		// TODO enable when group invites work
		invite.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 3852409971806848078L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		actions.addComponent(invite);

		comments = new VerticalLayout();
		comments.setCaption(Lang.get("events.details.comments"));
		comments.setSpacing(true);
		content.addComponent(comments);

		update(e);
	}

	private Component getItemsButton(fi.pss.cleanbeach.data.Event e) {
		itemsButton = new Button();
		itemsButton.setWidth("120px");
		itemsButton.setHeight("100%");
		itemsButton.addStyleName("thrash");
		itemsButton.setHtmlContentAllowed(true);

		updateItemsButton(e);
		return itemsButton;
	}

	private void updateItemsButton(fi.pss.cleanbeach.data.Event e) {
		itemsButton.setCaption("<span>" + e.getThrash().getTotalNum()
				+ "</span></div><br/>"
				+ Lang.get("events.eventpanel.numpieces"));
	}

	private Component getMap(Location location) {
		return new SimpleMap(location);
	}

	public void update(final fi.pss.cleanbeach.data.Event e) {

		selectedEvent = e;

		if (e.getJoinedUsers().contains(MyTouchKitUI.getCurrentUser())) {
			Button leave = new Button(Lang.get("events.details.leave"));
			getNavigationBar().setRightComponent(leave);
			leave.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 8307055006289465506L;

				@Override
				public void buttonClick(ClickEvent event) {
					presenter.leaveEvent(e, MyTouchKitUI.getCurrentUser());
				}
			});
		} else {
			Button join = new Button(Lang.get("events.details.join"));
			getNavigationBar().setRightComponent(join);
			join.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 509788041171724877L;

				@Override
				public void buttonClick(ClickEvent event) {
					presenter.joinEvent(e, MyTouchKitUI.getCurrentUser());
				}
			});
		}

		caption.setValue("<span>" + e.getLocation().getName() + "</span>"
				+ df.format(e.getStart()));

		desc.setValue(e.getDescription());

		creator.setValue(Lang.get("events.details.organizedby") + " "
				+ e.getOrganizer().getName());

		Iterator<User> users = e.getJoinedUsers().iterator();
		if (e.getJoinedUsers().size() > 2) {
			members.setValue(users.next().getName() + ", "
					+ users.next().getName() + ", "
					+ Lang.get("events.details.joined.andseparator") + " "
					+ (e.getJoinedUsers().size() - 2) + " "
					+ Lang.get("events.details.joined.more"));
		} else if (e.getJoinedUsers().size() == 2) {
			members.setValue(users.next().getName() + ", "
					+ users.next().getName());
		} else if (e.getJoinedUsers().size() == 1) {
			members.setValue(users.next().getName());
		} else {
			members.setValue(Lang.get("events.details.joined.none"));
		}

		updateItemsButton(e);

		actions.setVisible(e.getJoinedUsers().contains(
				MyTouchKitUI.getCurrentUser()));

		comments.removeAllComponents();
		for (Comment c : e.getComments()) {
			comments.addComponent(new CommentComponent(c));
		}
	}

}
