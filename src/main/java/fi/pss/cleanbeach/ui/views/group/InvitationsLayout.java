package fi.pss.cleanbeach.ui.views.group;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.ui.util.Lang;

public class InvitationsLayout extends NavigationView {

	private static final long serialVersionUID = -5546173151577864067L;

	public InvitationsLayout(Collection<Invite> pendingInvitations,
			final GroupPresenter presenter) {

		GridLayout content = new GridLayout(2, 1);
		setContent(content);

		content.setSpacing(true);
		content.setMargin(true);
		content.setWidth("100%");
		content.setColumnExpandRatio(0, 1);

		Label caption = new Label(Lang.get("Groups.view.invitations.caption"));
		caption.addStyleName("caption");
		content.addComponent(caption, 0, 0, 1, 0);

		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		for (final Invite i : pendingInvitations) {

			Label invite = new Label();
			invite.setContentMode(ContentMode.HTML);
			invite.setValue(i.getEvent().getLocation().getName() + " "
					+ df.format(i.getEvent().getStart()));
			invite.addStyleName("user");
			content.addComponent(invite);
			content.setComponentAlignment(invite, Alignment.MIDDLE_LEFT);

			final Switch s = new Switch();
			s.setSizeUndefined();
			s.setValue(i.isAccepted());
			s.setImmediate(true);
			s.addValueChangeListener(new ValueChangeListener() {

				private static final long serialVersionUID = 2319743848498697115L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					presenter.changeInvite(i, s.getValue());
				}
			});
			content.addComponent(s);
		}
	}
}
