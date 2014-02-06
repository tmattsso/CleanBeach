package fi.pss.cleanbeach.ui.mvp;

import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;

/**
 * Abstract Presenter superclass. Annotate subclass with {@link SessionScoped}.
 * You are free to inject any beans/resources into this class.
 * 
 * @author thomas
 * 
 * @param <V>
 *            The interface of the paired view.
 */
public abstract class AbstractPresenter<V extends IView> {

	protected Logger log = Logger.getLogger(this.getClass().getName());

	protected V view;

	protected AbstractPresenter() {
	}

	void setView(V view) {
		this.view = view;

	}

	public abstract void init();
}
