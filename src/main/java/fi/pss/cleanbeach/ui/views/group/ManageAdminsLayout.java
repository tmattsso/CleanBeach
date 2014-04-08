package fi.pss.cleanbeach.ui.views.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;

public class ManageAdminsLayout extends NavigationView {

	private static final long serialVersionUID = -8477874142689474108L;

	public ManageAdminsLayout(final UsersGroup group,
			final GroupPresenter presenter) {

		setCaption("Manage group admins");

		GridLayout vl = new GridLayout(2, 1);
		vl.setMargin(true);
		vl.setSpacing(true);
		vl.setColumnExpandRatio(0, 1);
		vl.setWidth("100%");
		setContent(vl);

		List<User> users = new ArrayList<>(group.getMembers());
		Collections.sort(users);
		for (final User u : users) {

			Label l = new Label(u.getName());
			vl.addComponent(l);

			final Switch w = new Switch();
			w.setValue(group.getAdmins().contains(u));
			if (u.equals(group.getCreator())) {
				w.setEnabled(false);
				w.setValue(true);
				l.setValue(u.getName() + " (creator)");
			}
			w.setWidth("100%");
			w.addValueChangeListener(new ValueChangeListener() {

				private static final long serialVersionUID = 1029980666504824173L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					presenter.setAdmin(group, u, w.getValue());
				}
			});

			vl.addComponent(w);
		}
	}
}
