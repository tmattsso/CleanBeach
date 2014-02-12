package fi.pss.cleanbeach.services;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;

@Singleton
@Startup
public class BootstrapService {

    @EJB
    private AuthenticationService auth;

    @PersistenceContext(unitName = "cleanbeach")
    private EntityManager em;

    @EJB
    private GroupService groupService;

    @PostConstruct
    public void init() {
        User user = new User();
        user.setName("Thomas");
        user.setEmail("thomas@vaadin.com");
        user.setUsername("thomas");

        System.out.println(user.getId());

        auth.createUser(user, "vaadin");

        user = auth.login("thomas", "vaadin");
        System.out.println(user.getName());

        UsersGroup group = new UsersGroup();
        group.setName("group");
        group.setDescription("descr");
        groupService.addAdmin(group, user);
    }
}
