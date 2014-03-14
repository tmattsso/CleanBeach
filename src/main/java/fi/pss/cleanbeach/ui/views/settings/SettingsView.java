package fi.pss.cleanbeach.ui.views.settings;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.cdi.UIScoped;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;

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

			final Popover pop = new Popover();
			pop.setWidth("80%");
			pop.center();
			pop.setModal(true);

			GridLayout gl = new GridLayout(2, 2);
			gl.setWidth("100%");
			gl.setMargin(true);
			gl.setSpacing(true);
			pop.setContent(gl);

			Label desc = new Label(Lang.get("settings.changepass.confirm"));
			gl.addComponent(desc, 0, 0, 1, 0);

			Button ok = new Button(Lang.get("global.yes"));
			ok.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 7082960292870880295L;

				@Override
				public void buttonClick(ClickEvent event) {
					presenter.changePass(pf.getValue());
					pop.close();
				}
			});
			gl.addComponent(ok);
			gl.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);

			Button cancel = new Button(Lang.get("global.cancel"));
			cancel.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 6353147704433465730L;

				@Override
				public void buttonClick(ClickEvent event) {
					pop.close();
				}
			});
			gl.addComponent(cancel);

			pop.showRelativeTo(SettingsView.this);
		}

	}
}
