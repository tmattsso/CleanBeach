package fi.pss.cleanbeach.ui.views.locations;

import java.util.List;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.components.ThrashInputLayout;

public class ThrashInputLocationLayout extends ThrashInputLayout {

	private static final long serialVersionUID = 5016780405533356871L;

	private final LocationPresenter presenter;
	private final ThrashDAO thrash;
	private final Location loc;

	public ThrashInputLocationLayout(Location l,
			final LocationPresenter presenter, ThrashDAO thrash) {

		this.presenter = presenter;
		this.thrash = thrash;
		loc = l;
	}

	@Override
	protected String getCurrentDesc(ThrashType t) {
		return thrash.getDesc(t);
	}

	@Override
	protected void setDesc(ThrashType type, String value) {
		presenter.addOtherDesc(type, MyTouchKitUI.getCurrentUser(), value);
	}

	@Override
	protected List<ThrashType> getThrashTypes() {
		return presenter.getThrashTypes();
	}

	@Override
	protected void storeValue(ThrashType type, Integer value) {
		presenter.setNumThrash(value, type, loc, MyTouchKitUI.getCurrentUser());
	}

	@Override
	protected int getValue(ThrashType type) {
		return thrash.getOfType(type);
	}

}
