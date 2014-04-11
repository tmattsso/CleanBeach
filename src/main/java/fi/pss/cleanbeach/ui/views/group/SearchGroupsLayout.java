package fi.pss.cleanbeach.ui.views.group;

import java.util.List;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.util.Lang;

public class SearchGroupsLayout extends NavigationView {

	private static final long serialVersionUID = -395091057217869168L;
	private final VerticalLayout resultsLayout;
	private final GroupPresenter presenter;

	public SearchGroupsLayout(final GroupPresenter presenter) {

		this.presenter = presenter;
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		vl.setSizeFull();
		setContent(vl);
		setSizeFull();

		setCaption(Lang.get("group.search.caption"));

		final TextField tf = new TextField();
		tf.setInputPrompt(Lang.get("group.search.prompt"));
		tf.setWidth("100%");
		vl.addComponent(tf);

		Button search = new Button(Lang.get("group.search.search"));
		search.setClickShortcut(KeyCode.ENTER);
		TouchKitIcon.search.addTo(search);
		vl.addComponent(search);
		search.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -5652755119664386024L;

			@Override
			public void buttonClick(ClickEvent event) {
				List<UsersGroup> results = presenter.getSearchResults(
						tf.getValue(), MainAppUI.getCurrentUser());
				populateResults(results);
			}
		});

		resultsLayout = new VerticalLayout();
		resultsLayout.setSpacing(true);

		Panel wrapper = new Panel(resultsLayout);
		wrapper.setSizeFull();
		vl.addComponent(wrapper);
		vl.setExpandRatio(wrapper, 1);
	}

	protected void populateResults(List<UsersGroup> results) {
		resultsLayout.removeAllComponents();
		for (UsersGroup group : results) {
			boolean isAdmin = group.isAdmin(MainAppUI.getCurrentUser());
			GroupComponent component = new GroupComponent(group, presenter,
					isAdmin);
			resultsLayout.addComponent(component);
		}
	}
}
