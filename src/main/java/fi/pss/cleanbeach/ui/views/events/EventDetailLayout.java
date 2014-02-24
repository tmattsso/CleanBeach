package fi.pss.cleanbeach.ui.views.events;

import java.util.Iterator;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Comment;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.MyTouchKitUI;

public class EventDetailLayout extends NavigationView {

	private static final long serialVersionUID = 4303038489004245363L;

	private final EventsPresenter presenter;

	private final Label caption;
	private final Label desc;

	private final Label members;

	private final Label creator;

	private final HorizontalLayout actions;

	private final VerticalLayout comments;

	public EventDetailLayout(final fi.pss.cleanbeach.data.Event e,
			final EventsPresenter presenter) {

		this.presenter = presenter;

		getNavigationBar().addStyleName("eventdetail");
		setCaption("Event details");

		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		setContent(content);

		caption = new Label("", ContentMode.HTML);
		caption.addStyleName("eventcaption");
		content.addComponent(caption);

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
		actions.setVisible(false);
		actions.setWidth("100%");
		content.addComponent(actions);

		Button comment = new Button("Comment");
		comment.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		actions.addComponent(comment);

		Button photo = new Button("Photo");
		photo.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		actions.addComponent(photo);

		Button addThrash = new Button("Report");
		addThrash.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		actions.addComponent(addThrash);

		Button invite = new Button("Invite");
		invite.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		actions.addComponent(invite);

		comments = new VerticalLayout();
		comments.setSpacing(true);
		content.addComponent(comments);

		update(e);
	}

	public void update(final fi.pss.cleanbeach.data.Event e) {
		if (e.getJoinedUsers().contains(MyTouchKitUI.getCurrentUser())) {
			Button leave = new Button("leave event");
			getNavigationBar().setRightComponent(leave);
			leave.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 8307055006289465506L;

				@Override
				public void buttonClick(ClickEvent event) {
					presenter.leaveEvent(e, MyTouchKitUI.getCurrentUser());
				}
			});
		} else {
			Button join = new Button("join event");
			getNavigationBar().setRightComponent(join);
			join.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 509788041171724877L;

				@Override
				public void buttonClick(ClickEvent event) {
					presenter.joinEvent(e, MyTouchKitUI.getCurrentUser());
				}
			});
		}

		caption.setValue(e.getLocation().getName() + "<br/><span>"
				+ e.getStart().toString() + "</span>");

		desc.setValue(e.getDescription());

		creator.setValue("Organized by " + e.getOrganizer().getName());

		Iterator<User> users = e.getJoinedUsers().iterator();
		if (e.getJoinedUsers().size() > 2) {
			members.setValue(users.next().getName() + ", "
					+ users.next().getName() + ", and "
					+ (e.getJoinedUsers().size() - 2) + " more");
		} else if (e.getJoinedUsers().size() == 2) {
			members.setValue(users.next().getName() + ", "
					+ users.next().getName());
		} else if (e.getJoinedUsers().size() == 1) {
			members.setValue(users.next().getName());
		} else {
			members.setValue("No joined users yet");
		}

		actions.setVisible(e.getJoinedUsers().contains(
				MyTouchKitUI.getCurrentUser()));

		comments.removeAllComponents();
		for (Comment c : e.getComments()) {
			comments.addComponent(new CommentComponent(c));
		}
	}

}
