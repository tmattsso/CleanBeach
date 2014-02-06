package fi.pss.cleanbeach.ui.mvp;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.cdi.CDIView;
import com.vaadin.ui.ComponentContainer;

/**
 * Abstract View superclass. Annotate the view implementation with
 * {@link CDIView}.
 * 
 * @author thomas
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractView<P extends AbstractPresenter> extends
		NavigationView implements IView, Serializable {

	private static final long serialVersionUID = 3338257225854006391L;

	protected P presenter;

	public AbstractView() {
	}

	public abstract void injectPresenter(P presenter);

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		presenter.setView(this);
		setContent(getMainContent());
	}

	protected abstract ComponentContainer getMainContent();

	@Override
	public void attach() {
		super.attach();
		presenter.init();
	}
}
