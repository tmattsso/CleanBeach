package fi.pss.cleanbeach.ui.mvp;

import javax.annotation.PostConstruct;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;

/**
 * Abstract View superclass. Annotate the view implementation with
 * {@link CDIView}.
 * 
 * @author thomas
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractView<P extends AbstractPresenter> extends
		CustomComponent implements IView, View {

	private static final long serialVersionUID = 3338257225854006391L;

	protected P presenter;

	public AbstractView() {
	}

	public abstract void injectPresenter(P presenter);

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		presenter.setView(this);
		setCompositionRoot(getContent());
	}

	protected abstract ComponentContainer getContent();

	/**
	 * OK to override
	 * 
	 * @see View#enter(ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		presenter.init();
	}
}
