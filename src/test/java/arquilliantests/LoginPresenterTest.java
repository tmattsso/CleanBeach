package arquilliantests;

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
import org.mockito.Mockito;
import org.vaadin.se.facebook.FacebookListener;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.services.AuthenticationService;
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

				.addClass(LoginPresenter.class).addClass(ILogin.class)
				.addClass(fi.pss.cleanbeach.ui.views.login.LoginEvent.class)
				.addClass(IView.class).addClass(AbstractPresenter.class)
				.addClass(FacebookListener.class)

				.addClass(Logger.class).addClass(Driver.class)
				.addAsResource("META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private Presenter presenter;
	private ILogin mock;

	@Before
	public void preSetup() {
		mock = Mockito.mock(ILogin.class);
		presenter.setDummyView(mock);
	}

	@Test
	public void loginWithNull() {

		presenter.login(null, null);
		Mockito.verify(mock).showLoginError();
	}
}
