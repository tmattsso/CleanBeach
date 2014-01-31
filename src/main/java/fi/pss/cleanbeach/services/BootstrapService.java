package fi.pss.cleanbeach.services;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fi.pss.cleanbeach.data.User;

@Singleton
@Startup
public class BootstrapService {

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

	@PostConstruct
	public void init() {

		User user = new User();
		user.setName("Thomas");
		user.setEmail("thomas@vaadin.com");
		user.setUsername("thomas");
		em.persist(user);

		System.out.println(user.getId());

		em.flush();

		user = em.find(User.class, user.getId());
		System.out.println(user.getName());
	}
}
