package fi.pss.cleanbeach.ui.views.eventdetails;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Thrash;
import fi.pss.cleanbeach.data.ThrashDAO;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.ui.util.Lang;

public class ThrashDetailLayout extends NavigationView {

	private static final long serialVersionUID = 6544231017301561837L;

	public ThrashDetailLayout(fi.pss.cleanbeach.data.Event e,
			EventDetailsPresenter<?> presenter) {

		VerticalLayout root = new VerticalLayout();
		root.setSpacing(true);
		root.setMargin(true);
		setContent(root);

		setCaption(Lang.get("events.details.thrash.caption"));

		ThrashDAO thrash = e.getThrash();
		if (thrash.isEmpty()) {

			Label l = new Label(Lang.get("events.details.thrash.nothing"));
			root.addComponent(l);
			return;
		}
		Label l = new Label(Lang.get("events.details.thrash.summary"));
		root.addComponent(l);

		ThrashType otherType = null;
		for (ThrashType type : presenter.getThrashTypes()) {

			if (thrash.getOfType(type) == 0) {
				continue;
			}

			if (!type.isOther()) {
				l = new Label(Lang.get(type) + ": " + thrash.getOfType(type));
				root.addComponent(l);
			} else {
				otherType = type;
			}
		}

		if (otherType != null) {
			l = new Label(Lang.get(otherType) + ": "
					+ thrash.getOfType(otherType) + ":");
			root.addComponent(l);
			for (Thrash t : e.getThrash().getOtherMarkings()) {
				l = new Label(t.getDescription());
				root.addComponent(l);
			}
		}
	}
}
