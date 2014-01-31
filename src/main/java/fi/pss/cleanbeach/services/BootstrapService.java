package fi.pss.cleanbeach.services;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import fi.pss.cleanbeach.data.User;

@Singleton
@Startup
public class BootstrapService {

	@EJB
	private AuthenticationService auth;

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
	}
}
