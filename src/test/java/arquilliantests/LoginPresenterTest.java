package arquilliantests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.h2.Driver;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vaadin.se.facebook.FacebookListener;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.services.AuthenticationService.RegistrationException;
import fi.pss.cleanbeach.services.util.LoggingInterceptor;
import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;
import fi.pss.cleanbeach.ui.mvp.IView;
import fi.pss.cleanbeach.ui.views.login.ILogin;
import fi.pss.cleanbeach.ui.views.login.LoginPresenter;

@RunWith(Arquillian.class)
public class LoginPresenterTest {

	@SessionScoped
	public static class Presenter extends LoginPresenter implements
			Serializable {
		private static final long serialVersionUID = 3437094310018884267L;

		public void setDummyView(ILogin view) {
			this.view = view;
		}
	}

	@Deployment
	public static JavaArchive createDeployment() {

		return ShrinkWrap
				.create(JavaArchive.class)
				// .addPackage(BootstrapService.class.getPackage())
				.addPackage(User.class.getPackage())
				.addClass(AuthenticationService.class)
				.addClass(LoggingInterceptor.class)

				.addClass(DummyLogin.class).addClass(LoginPresenter.class)
				.addClass(ILogin.class)
				.addClass(fi.pss.cleanbeach.ui.views.login.LoginEvent.class)
				.addClass(IView.class).addClass(AbstractPresenter.class)
				.addClass(FacebookListener.class)

				.addClass(Logger.class).addClass(Driver.class)
				.addAsResource("META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private Presenter presenter;

	@Inject
	private AuthenticationService aserv;

	private DummyLogin mock;

	@Before
	public void preSetup() throws RegistrationException {
		System.out.println(" ===== starting test");
		mock = new DummyLogin();
		presenter.setDummyView(mock);

		User u = new User();
		u.setName("Test");
		u.setEmail("correct@correct.com");
		User createUser = aserv.createUser(u, "correct");
		System.out.println(" ===== created test user " + createUser);
	}

	@Test
	public void loginWithNull() {

		presenter.login(null, null);
		assertTrue(mock.loginErrorShown);
	}

	@Test
	public void loginWithCorrect() {

		presenter.login("correct@correct.com", "correct");
		assertFalse(mock.loginErrorShown);
	}
}
