package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;

public class InviteGroupsLayout extends NavigationView {

	private static final long serialVersionUID = 3525640364491650445L;

	public InviteGroupsLayout(Collection<Invite> coll,
			final fi.pss.cleanbeach.data.Event e, User u,
			final EventDetailsPresenter<?> presenter) {

        setStyleName("invite");

		setCaption("Invite other groups");

		VerticalComponentGroup gl = new VerticalComponentGroup("Available groups");
		setContent(gl);

		if (u.getMemberIn().isEmpty()) {
			Label nein = new Label("You need to join some groups first!");
			gl.addComponent(nein);
			return;
		}

		Map<UsersGroup, Boolean> groupToAcceptance = new HashMap<UsersGroup, Boolean>();
		for (Invite i : coll) {
			groupToAcceptance.put(i.getInvitee(), i.isAccepted());
		}

		for (final UsersGroup g : u.getMemberIn()) {

            CssLayout groupLayout = new CssLayout();
            groupLayout.setCaption(g.getName());

			final Button b = new Button();
            groupLayout.addComponents(b);

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
						presenter.invite(g, e);
						b.setCaption("Invitation sent");
						b.setEnabled(false);
					}
				});
			}

            gl.addComponents(groupLayout);
		}
	}

}
