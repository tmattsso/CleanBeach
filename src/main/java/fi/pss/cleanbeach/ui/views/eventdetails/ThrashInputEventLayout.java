package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.List;

import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.components.ThrashInputLayout;

public class ThrashInputEventLayout extends ThrashInputLayout {

	private static final long serialVersionUID = -1504594448394008523L;

	private final EventDetailsPresenter<?> presenter;
	private final fi.pss.cleanbeach.data.Event event;

	public ThrashInputEventLayout(fi.pss.cleanbeach.data.Event e,
			final EventDetailsPresenter<?> presenter) {

		this.presenter = presenter;
		event = e;
	}

	@Override
	protected String getCurrentDesc(ThrashType t) {
		return event.getThrash().getDesc(t);
	}

	@Override
	protected void setDesc(ThrashType type, String value) {
		presenter.addOtherDesc(type, value, event);
	}

	@Override
	protected List<ThrashType> getThrashTypes() {
		return presenter.getThrashTypes();
	}

	@Override
	protected void storeValue(ThrashType type, Integer value) {
		presenter.setNumThrash(value, type, event);
	}

	@Override
	protected int getValue(ThrashType type) {
		return event.getThrash().getOfTypeForUser(type,
				MainAppUI.getCurrentUser());
	}

	@Override
	protected void navigateAway() {
		presenter.navigatedFromThrash(event);
	}

}
