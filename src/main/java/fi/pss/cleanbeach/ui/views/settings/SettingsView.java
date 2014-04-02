package fi.pss.cleanbeach.ui.views.settings;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.components.ConfirmPopover;
import fi.pss.cleanbeach.ui.components.ConfirmPopover.ConfirmListener;
import fi.pss.cleanbeach.ui.mvp.AbstractView;
import fi.pss.cleanbeach.ui.util.Lang;

@UIScoped
public class SettingsView extends AbstractView<SettingsPresenter> implements
		ISettings {

	private static final long serialVersionUID = 1762636200155654459L;
	private PasswordField pf;

	@Override
	@Inject
	public void injectPresenter(SettingsPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	protected ComponentContainer getMainContent() {

		NavigationView vl = new NavigationView();
		vl.setCaption(Lang.get("settings.caption"));
		vl.setWidth("100%");

		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		root.setWidth("100%");
		vl.setContent(root);

		pf = new PasswordField(Lang.get("settings.changepass.caption"));
		pf.setWidth("100%");

		Button doChange = new Button(Lang.get("settings.changepass.button"));
		doChange.addClickListener(new PasswordChanger());
		doChange.setClickShortcut(KeyCode.ENTER);

		HorizontalLayout hl = new HorizontalLayout(pf, doChange);
		hl.setSpacing(true);
		hl.setWidth("100%");
		hl.setExpandRatio(pf, 1);
		root.addComponent(hl);

		return vl;
	}

	private class PasswordChanger implements ClickListener {

		private static final long serialVersionUID = -7214990644441205528L;

		@Override
		public void buttonClick(ClickEvent event) {

			ConfirmPopover pop = new ConfirmPopover(new ConfirmListener() {

				@Override
				public void confirmed() {
					presenter.changePass(pf.getValue());
				}
			}, Lang.get("settings.changepass.confirm"));

			pop.showRelativeTo(SettingsView.this);
		}

	}
}
