package unittests;

import static org.junit.Assert.assertNull;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fi.pss.cleanbeach.services.AuthenticationService;
import fi.pss.cleanbeach.ui.views.login.LoginEvent;
import fi.pss.cleanbeach.ui.views.login.LoginPresenter;
import fi.pss.cleanbeach.ui.views.login.LoginView;

public class LoginPresenterTest {

	private LoginPresenter presenter;
	private AuthenticationService authServMock;
	private Event<LoginEvent> event;
	private LoginView view;

	@Before
	public void init() {
		Presenter presenter = new Presenter();

		view = Mockito.mock(LoginView.class);
		presenter.setView(view);

		authServMock = Mockito.mock(AuthenticationService.class);
		presenter.setAuthService(authServMock);

		// Mockito can't mock this, unfortunately
		event = new FakeEvent();
		presenter.setEvent(event);

		this.presenter = presenter;
	}

	@Test
	public void testLoginWithNull() {
		presenter.login(null, null);
		Mockito.verify(authServMock).login(null, null);
		Mockito.verify(view).showLoginError();

		assertNull(((FakeEvent) event).sentEvent);
	}
}
