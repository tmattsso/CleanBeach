package fi.pss.cleanbeach.ui.mvp;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.User;

/**
 * Abstract Presenter superclass. Annotate subclass with {@link UIScoped}. You
 * are free to inject any beans/resources into this class.
 * 
 * @author thomas
 * 
 * @param <V>
 *            The interface of the paired view.
 */
public abstract class AbstractPresenter<V extends IView> {

	protected Logger log = Logger.getLogger(this.getClass().getSimpleName());

	protected V view;

	protected AbstractPresenter() {
	}

	void setView(V view) {
		this.view = view;

	}

	public abstract void init(User currentUser);

	public void handleError(Exception e) {
		log.log(Level.SEVERE, "Exception in a view", e);
		view.showErrorNotification("[An error has occured; the administrator has been notified");
	}
}
