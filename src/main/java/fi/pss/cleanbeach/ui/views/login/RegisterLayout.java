package fi.pss.cleanbeach.ui.views.login;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class RegisterLayout extends VerticalLayout {

	private static final long serialVersionUID = 3652366577918313188L;

	private final Label error;

	public RegisterLayout(final LoginPresenter presenter) {

		addStyleName("register");
		setSpacing(true);

		Label caption = new Label("[Register");
		caption.addStyleName("caption");
		addComponent(caption);

		Label desc = new Label("[Register for all the benefits!");
		desc.addStyleName("desc");
		addComponent(desc);

		final TextField name = new TextField("[name");
		name.setWidth("100%");
		name.setRequired(true);
		addComponent(name);

		final TextField email = new TextField("[email");
		email.setWidth("100%");
		email.setRequired(true);
		addComponent(email);

		final PasswordField password = new PasswordField("[password");
		password.setWidth("100%");
		password.setRequired(true);
		addComponent(password);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addStyleName("actionbuttons");

		Button register = new Button("[register", new ClickListener() {

			private static final long serialVersionUID = 96154108214861206L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.register(name.getValue(), email.getValue(),
						password.getValue());
			}
		});
		addComponent(register);

		error = new Label();
		error.setVisible(false);
		error.addStyleName("error");
		addComponent(error);

		Label assurance = new Label(
				"[We will never give your email adress to third parties or send you spam. We are also very strict with storing passwords; rest assured, your data is safe with us.");
		addComponent(assurance);
	}

	public void showError(String msg) {
		error.setVisible(true);
		error.setValue(msg);
	}
}
