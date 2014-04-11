package fi.pss.cleanbeach.ui.views.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.util.Lang;

public class GroupMembersLayout extends NavigationView {

	private static final long serialVersionUID = -5546173151577864067L;

	public GroupMembersLayout(UsersGroup group, final GroupPresenter presenter) {

		VerticalLayout content = new VerticalLayout();
		setContent(content);

		content.setSpacing(true);
		content.setMargin(true);

		Label caption = new Label(
				Lang.get("Groups.view.members.membersingroup"));
		caption.addStyleName("caption");
		content.addComponent(caption);

		List<User> users = new ArrayList<>(group.getMembers());
		Collections.sort(users);
		boolean isAdmin = group.getCreator().equals(
				MainAppUI.getCurrentUser())
				|| group.getAdmins().contains(MainAppUI.getCurrentUser());
		for (User u : users) {

			Label user = new Label();
			if (isAdmin) {
				user.setContentMode(ContentMode.HTML);
				user.setValue(u.getName() + " <span>(" + u.getEmail()
						+ ")</span>");
			} else {
				user.setValue(u.getName());
			}
			user.addStyleName("user");
			content.addComponent(user);
		}

	}
}
