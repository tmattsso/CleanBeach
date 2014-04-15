package fi.pss.cleanbeach.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.AuthenticationService.RegistrationException;
import fi.pss.cleanbeach.services.GroupService.CannotDeleteException;

@Singleton
@Startup
public class BootstrapService {

	private static final boolean INIT = false;

	private final Logger log = Logger
			.getLogger(this.getClass().getSimpleName());

	@EJB
	private AuthenticationService auth;

	@EJB
	private GroupService groupService;

	@EJB
	private LocationService locService;

	@EJB
	private EventService eventService;

	@EJB
	private InviteService inviteService;

	@PostConstruct
	public void init() throws CannotDeleteException {

		if (!INIT) {
			return;
		}

		log.warning("===Starting bootstrap===");

		User user = new User();
		user.setName("Thomas");
		user.setEmail("thomas@t.com");
		try {
			auth.createUser(user, "vaadin");
		} catch (RegistrationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		log.info("Added user " + user.getId());

		User user2 = new User();
		user2.setName("Demo");
		user2.setEmail("demo@demo.com");
		try {
			auth.createUser(user2, "vaadin");
		} catch (RegistrationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		log.info("Added user " + user2.getId());

		UsersGroup group2 = groupService
				.createGroup(
						user,
						"PSS ry",
						"Ullamco fatback pork, strip steak anim do irure meatloaf shoulder frankfurter non fugiat tri-tip. Cow kevin cupidatat venison chuck biltong in laborum. Mollit pastrami pork belly, ex ad est id ullamco");
		groupService.addMember(group2, user);
		groupService.setAdminStatus(group2, user, true);

		UsersGroup group = groupService
				.createGroup(user, "Vaadin Ltd", "descr");
		groupService.addMember(group, user);
		groupService.setAdminStatus(group, user, true);
		groupService.addMember(group, user2);
		groupService.setAdminStatus(group, user2, true);

		log.info("Added group " + group.getId());

		Location loc = locService.createLocation(60.45232937494697,
				22.300100326538086, "Vaadin Office");

		log.info("Added location " + loc.getId());

		Calendar now = Calendar.getInstance();
		// now.add(Calendar.MONTH, -2);
		Event e = eventService
				.createEvent(
						now.getTime(),
						loc,
						group,
						"Ullamco fatback pork, strip steak anim do irure meatloaf shoulder frankfurter non fugiat tri-tip. Cow kevin cupidatat venison chuck biltong in laborum. Mollit pastrami pork belly, ex ad est id ullamco do exercitation landjaeger dolor shoulder qui. Anim jowl ea proident non, prosciutto nulla. Ut voluptate proident, shoulder sed magna ex. Chuck filet mignon eu jowl veniam deserunt. Capicola jowl do excepteur meatloaf quis, jerky pork belly dolore labore voluptate porchetta.");

		log.info("Added event " + e.getId());

		eventService.addThrashType("glass", false);
		eventService.addThrashType("paper", false);
		eventService.addThrashType("wood", false);
		eventService.addThrashType("metal", false);
		eventService.addThrashType("plastic", false);
		eventService.addThrashType("rubber", false);
		eventService.addThrashType("cloth", false);
		eventService.addThrashType("organic", false);
		eventService.addThrashType("other", true);

		eventService
				.addComment(
						e,
						"Bacon ipsum dolor sit amet ball tip turkey shoulder jerky porchetta pork chop bresaola. Landjaeger kevin ribeye, jerky chicken filet mignon porchetta pork turkey. Tri-tip bacon shank tail biltong salami meatloaf short ribs jerky boudin ham. Pastrami chuck shoulder beef. Shank flank porchetta andouille. Spare ribs beef salami swine.",
						null, user);

		Image img = new Image();
		img.setMimetype("image/png");
		img.setUploaded(new Date());
		Path path = Paths.get(getClass().getResource("../ui/logo.png")
				.getPath());
		try {
			img.setContent(Files.readAllBytes(path));
		} catch (IOException e1) {
			log.log(Level.WARNING, "read of image failed", e1);
		}
		eventService.addComment(e, "image to test with", img, user2);

		Invite invite = inviteService.invite(user, group2, e);

		log.info("Sent invitation " + invite.getId());

		log.warning("===Bootstrap done===");

	}
}
