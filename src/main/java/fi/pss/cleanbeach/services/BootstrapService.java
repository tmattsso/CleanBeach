package fi.pss.cleanbeach.services;

import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;

@Singleton
@Startup
public class BootstrapService {

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
    public void init() {

        log.warning("===Starting bootstrap===");

        User user = new User();
        user.setName("Thomas");
        user.setEmail("thomas");
        auth.createUser(user, "vaadin");

        log.info("Added user " + user.getId());

        User user2 = new User();
        user2.setName("Demo");
        user2.setEmail("demo");
        auth.createUser(user2, "demo");

        log.info("Added user " + user2.getId());

        UsersGroup group = groupService
                .createGroup(user, "Vaadin Ltd", "descr");
        groupService.addAdmin(group, user2);
        groupService.addAdmin(group, user);

        log.info("Added group " + group.getId());

        Location loc = locService.createLocation(60.45232937494697,
                22.300100326538086, "Vaadin Office");

        log.info("Added location " + loc.getId());

        Event e = eventService.createEvent(new Date(), loc, group, "Demo");

        log.info("Added event " + e.getId());

        eventService.addThrashType("Glass");
        eventService.addThrashType("Paper");
        eventService.addThrashType("Wood");
        eventService.addThrashType("Metal");
        eventService.addThrashType("Plastic");

        Invite invite = inviteService.invite(user, group, e);

        log.info("Sent invitation " + invite.getId());

        log.warning("===Bootstrap done===");

    }
}
