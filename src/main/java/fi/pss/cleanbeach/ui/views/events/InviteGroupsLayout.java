package fi.pss.cleanbeach.ui.views.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.MyTouchKitUI;

public class InviteGroupsLayout extends NavigationView {

	private static final long serialVersionUID = 3525640364491650445L;

	public InviteGroupsLayout(Collection<Invite> coll,
			final fi.pss.cleanbeach.data.Event e,
			final EventsPresenter presenter) {

		setCaption("Invite other groups");

		GridLayout gl = new GridLayout(2, 1);
		setContent(gl);
		gl.setSpacing(true);
		gl.setMargin(true);
		gl.setWidth("100%");
		gl.setColumnExpandRatio(0, 1);

		Map<UsersGroup, Boolean> groupToAcceptance = new HashMap<UsersGroup, Boolean>();
		for (Invite i : coll) {
			groupToAcceptance.put(i.getInvitee(), i.isAccepted());
		}

		for (final UsersGroup g : MyTouchKitUI.getCurrentUser().getMemberIn()) {
			Label l = new Label(g.getName());
			gl.addComponent(l);

			final Button b = new Button();
			gl.addComponent(b);

			if (e.getOrganizer().equals(g)) {
				b.setCaption("Organizer");
				b.setEnabled(false);
			} else if (groupToAcceptance.containsKey(g)) {
				b.setCaption("Already invited");
				b.setEnabled(false);
			} else {
				b.setCaption("Invite group");
				b.addClickListener(new ClickListener() {

					private static final long serialVersionUID = 3309022449084013098L;

					@Override
					public void buttonClick(ClickEvent event) {
						presenter.invite(g, MyTouchKitUI.getCurrentUser(), e);
						b.setCaption("Invitation sent");
						b.setEnabled(false);
					}
				});
			}
		}
	}

}
