package fi.pss.cleanbeach.ui.views.settings;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.components.ConfirmPopover;
import fi.pss.cleanbeach.ui.components.ConfirmPopover.ConfirmListener;
import fi.pss.cleanbeach.ui.mvp.AbstractView;
import fi.pss.cleanbeach.ui.util.Lang;

@UIScoped
public class SettingsView extends AbstractView<SettingsPresenter> implements
		ISettings {

	private static final long serialVersionUID = 1762636200155654459L;
	private PasswordField pf;
	private EmailField ef;
	private TextField nf;

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
		root.setSizeFull();
		vl.setContent(root);

		Component toExpand;

		nf = new TextField(Lang.get("settings.changename.caption"));
		nf.setWidth("100%");
		nf.addValidator(new StringLengthValidator(Lang
				.get("settings.changename.notvalid"), 1, 100, false));
		nf.setValue(MainAppUI.getCurrentUser().getName());
		root.addComponent(nf);

		Button doChange = new Button(Lang.get("settings.changename.button"));
		doChange.addClickListener(new NameChanger());
		root.addComponent(doChange);

		pf = new PasswordField(Lang.get("settings.changepass.caption"));
		pf.setWidth("100%");
		pf.addValidator(new StringLengthValidator(Lang
				.get("settings.changepass.notvalid"), 6, 100, false));
		pf.setValidationVisible(false);
		root.addComponent(pf);

		doChange = new Button(Lang.get("settings.changepass.button"));
		doChange.addClickListener(new PasswordChanger());
		root.addComponent(doChange);

		ef = new EmailField(Lang.get("settings.changeemail.caption"));
		ef.setWidth("100%");
		ef.addValidator(new EmailValidator(Lang
				.get("settings.changeemail.notvalid")));
		ef.setImmediate(true);
		ef.setValue(MainAppUI.getCurrentUser().getEmail());
		root.addComponent(ef);

		doChange = new Button(Lang.get("settings.changeemail.button"));
		doChange.addClickListener(new EmailChanger());
		root.addComponent(doChange);
		toExpand = doChange;

		doChange = new Button(Lang.get("settings.logout.button"));
		doChange.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 4275728302315656288L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.requestLogout();
			}
		});
		root.addComponent(doChange);

		root.setExpandRatio(toExpand, 1);
		return vl;
	}

	private class PasswordChanger implements ClickListener {

		private static final long serialVersionUID = -7214990644441205528L;

		@Override
		public void buttonClick(ClickEvent event) {
			if (!pf.isValid()) {
				Notification.show(Lang.get("settings.changepass.givevalid"),
						Type.WARNING_MESSAGE);
				return;
			}

			ConfirmPopover pop = new ConfirmPopover(new ConfirmListener() {

				@Override
				public void confirmed() {
					presenter.changePass(pf.getValue(),
							MainAppUI.getCurrentUser());
				}
			}, Lang.get("settings.changepass.confirm"));

			pop.showRelativeTo(SettingsView.this);
		}

	}

	private class EmailChanger implements ClickListener {

		private static final long serialVersionUID = -5596676810011377645L;

		@Override
		public void buttonClick(ClickEvent event) {

			if (!ef.isValid()) {
				Notification.show(Lang.get("settings.changeemail.givevalid"),
						Type.WARNING_MESSAGE);
				return;
			}

			ConfirmPopover pop = new ConfirmPopover(new ConfirmListener() {

				@Override
				public void confirmed() {

					presenter.changeEmail(ef.getValue(),
							MainAppUI.getCurrentUser());
				}
			}, Lang.get("settings.changeemail.confirm"));

			pop.showRelativeTo(SettingsView.this);
		}

	}

	private class NameChanger implements ClickListener {

		private static final long serialVersionUID = 3301538396079645034L;

		@Override
		public void buttonClick(ClickEvent event) {

			if (!nf.isValid()) {
				Notification.show(Lang.get("settings.changename.givevalid"),
						Type.WARNING_MESSAGE);
				return;
			}

			ConfirmPopover pop = new ConfirmPopover(new ConfirmListener() {

				@Override
				public void confirmed() {

					presenter.changeName(nf.getValue(),
							MainAppUI.getCurrentUser());
				}
			}, Lang.get("settings.changename.confirm"));

			pop.showRelativeTo(SettingsView.this);
		}

	}
}
