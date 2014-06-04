package unittests;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Event;
import javax.enterprise.util.TypeLiteral;

import fi.pss.cleanbeach.ui.views.login.LoginEvent;

public class FakeEvent implements Event<LoginEvent> {

	public LoginEvent sentEvent;

	@Override
	public void fire(LoginEvent arg0) {
		sentEvent = arg0;
	}

	@Override
	public Event<LoginEvent> select(Annotation... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U extends LoginEvent> Event<U> select(Class<U> arg0,
			Annotation... arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U extends LoginEvent> Event<U> select(TypeLiteral<U> arg0,
			Annotation... arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
