package fi.pss.cleanbeach.ui.mvp;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.MyTouchKitUI;

/**
 * Abstract View superclass. Annotate the view implementation with
 * {@link UIScoped}.
 * 
 * @author thomas
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractView<P extends AbstractPresenter> extends
		NavigationManager implements IView, Serializable {

	private static final long serialVersionUID = 3338257225854006391L;

	protected P presenter;

	public AbstractView() {
	}

	public abstract void injectPresenter(P presenter);

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		presenter.setView(this);
	}

	protected abstract ComponentContainer getMainContent();

	@Override
	public void attach() {
		super.attach();
		presenter.init(getUser());
		navigateTo(getMainContent());
	}

	@Override
	public User getUser() {
		return MyTouchKitUI.getCurrentUser();
	}

	@Override
	public void showErrorNotification(String string) {
		Notification.show(string, Type.ERROR_MESSAGE);
	}

	@Override
	public void showErrorNotification(String string, String msg) {
		Notification.show(string, "<br/>" + msg, Type.ERROR_MESSAGE);
	}
}
