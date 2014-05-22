package fi.pss.cleanbeach.standalone.stats;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.cdi.CDIUIProvider;
import com.vaadin.cdi.UIScoped;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.Thrash;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.services.GroupService;
import fi.pss.cleanbeach.services.LocationService;

@UIScoped
public class StatsUI extends UI {

	private static final long serialVersionUID = 8560450329285696314L;

	@WebServlet("/stats/*")
	@VaadinServletConfiguration(ui = StatsUI.class, productionMode = false, widgetset = "fi.pss.cleanbeach.standalone.map.MapWidgetSet")
	public static class MapServlet extends VaadinServlet {

		private static final long serialVersionUID = 3342817781686266438L;

		private final CDIUIProvider cdiProvider = new CDIUIProvider() {
			private static final long serialVersionUID = 8587130632158809786L;

			@Override
			public Class<? extends UI> getUIClass(
					UIClassSelectionEvent selectionEvent) {
				return StatsUI.class;
			}
		};

		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
			getService().addSessionInitListener(new SessionInitListener() {
				private static final long serialVersionUID = -7323757409778480145L;

				@Override
				public void sessionInit(SessionInitEvent event)
						throws ServiceException {
					event.getSession().addUIProvider(cdiProvider);
				}
			});
		}
	}

	@Inject
	private EventService eService;

	@Inject
	private LocationService locService;

	@Inject
	private AuthenticationService authService;

	@Inject
	private GroupService groupService;

	@Override
	protected void init(VaadinRequest request) {

		final PasswordField pw = new PasswordField();
		pw.focus();
		pw.setImmediate(true);

		setSizeFull();
		setContent(pw);

		pw.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -6534485764344563512L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				if (pw.getValue().equals("itmill2vaadin")) {
					buildStats();
				}
			}
		});
	}

	protected void buildStats() {

		VerticalLayout main = new VerticalLayout();
		main.setSpacing(true);
		main.setMargin(true);
		setContent(main);

		Label l = new Label("Users:");
		l.addStyleName(Reindeer.LABEL_H1);
		main.addComponent(l);

		for (User u : authService.getUsers()) {
			l = new Label(u.getName() + " " + u.getEmail());
			main.addComponent(l);
		}

		l = new Label("Groups:");
		l.addStyleName(Reindeer.LABEL_H1);
		main.addComponent(l);

		List<UsersGroup> groups = groupService.getGroups();
		l = new Label("total of " + groups.size() + " groups");
		main.addComponent(l);
		for (UsersGroup g : groups) {
			l = new Label(g.getName());
			l.addStyleName(Reindeer.LABEL_H2);
			main.addComponent(l);
			l = new Label("Creator: " + g.getCreator().getEmail());
			main.addComponent(l);

			for (User u : g.getAdmins()) {
				l = new Label("Admin: " + u.getEmail());
				main.addComponent(l);
			}

			l = new Label("Members:");
			l.addStyleName(Reindeer.LABEL_H2);
			main.addComponent(l);
			StringBuilder users = new StringBuilder();
			for (User u : g.getMembers()) {
				users.append(u.getEmail() + ", ");
			}
			l = new Label(users.toString());
			main.addComponent(l);

			l = new Label("Group events:");
			l.addStyleName(Reindeer.LABEL_H2);
			main.addComponent(l);
			for (fi.pss.cleanbeach.data.Event u : eService.getEvents(g)) {
				l = new Label(u.getId() + ": " + u.getLocation().getName()
						+ " " + u.getStart());
				main.addComponent(l);
			}
		}

		l = new Label("Events");
		l.addStyleName(Reindeer.LABEL_H1);
		main.addComponent(l);
		List<fi.pss.cleanbeach.data.Event> events = eService.getEvents();
		l = new Label("Total of " + events.size() + " events");
		main.addComponent(l);
		for (fi.pss.cleanbeach.data.Event e : events) {

			l = new Label(e.getId() + ": " + e.getLocation().getName() + " "
					+ e.getStart());
			l.addStyleName(Reindeer.LABEL_H2);
			main.addComponent(l);
			l = new Label("Organizer: " + e.getOrganizer().getName());
			main.addComponent(l);
			l = new Label("Joined users");
			main.addComponent(l);
			StringBuilder users = new StringBuilder();
			for (User u : e.getJoinedUsers()) {
				users.append(u.getEmail() + ", ");
			}
			l = new Label(users.toString());
			main.addComponent(l);

			l = new Label("Event thrash:");
			l.addStyleName(Reindeer.LABEL_H2);
			main.addComponent(l);

			ThrashDAO trash = e.getThrash();

			printThrash(trash, main);

		}

		l = new Label("Beaches:");
		l.addStyleName(Reindeer.LABEL_H1);
		main.addComponent(l);
		for (Location loc : locService.getLocations()) {
			l = new Label(loc.getId() + " " + loc.getName());
			l.addStyleName(Reindeer.LABEL_H2);
			main.addComponent(l);
			l = new Label(eService.getEvents(loc).size() + " events held");
			main.addComponent(l);
			ThrashDAO trash = locService.getThrash(loc);

			l = new Label("Location thrash:");
			l.addStyleName(Reindeer.LABEL_H2);
			main.addComponent(l);
			printThrash(trash, main);

		}

		l = new Label("All thrash");
		l.addStyleName(Reindeer.LABEL_H1);
		main.addComponent(l);

		ThrashDAO trash = locService.getThrash();

		printThrash(trash, main);
	}

	private void printThrash(ThrashDAO trash, Layout main) {
		Label l = new Label("Total collected: " + trash.getTotalNum());
		main.addComponent(l);

		for (ThrashType tt : eService.getThrashTypes()) {

			if (trash.getOfType(tt) == 0) {
				continue;
			}
			l = new Label(tt.getName() + ":" + trash.getOfType(tt));
			main.addComponent(l);
		}

		for (Thrash t : trash.getOtherMarkings()) {

			l = new Label(t.getType() + ": " + t.getDescription());
			main.addComponent(l);
		}
	}

}
