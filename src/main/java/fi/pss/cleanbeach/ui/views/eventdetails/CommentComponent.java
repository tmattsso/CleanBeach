package fi.pss.cleanbeach.ui.views.eventdetails;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

import fi.pss.cleanbeach.data.Comment;
import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.components.ConfirmPopover;
import fi.pss.cleanbeach.ui.components.ConfirmPopover.ConfirmListener;
import fi.pss.cleanbeach.ui.util.Lang;

public class CommentComponent extends CustomComponent {

	private static final long serialVersionUID = -457189718760395829L;

	private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	public CommentComponent(final Comment c,
			final fi.pss.cleanbeach.data.Event event,
			final EventDetailsPresenter<?> presenter) {

		CssLayout root = new CssLayout();
		setCompositionRoot(root);
		root.setWidth("100%");
		addStyleName("comment");

		Label author = new Label(c.getAuthor().getName() + ", "
				+ df.format(c.getWritetime()), ContentMode.HTML);
		author.addStyleName("author");
		root.addComponent(author);

		if (event.getOrganizer().isAdmin(MainAppUI.getCurrentUser())) {
			final Button delete = new Button(Lang.get("events.comment.delete"));
			delete.addStyleName(BaseTheme.BUTTON_LINK);
			delete.addStyleName("delete");
			root.addComponent(delete);

			delete.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 4017668724515317210L;

				@Override
				public void buttonClick(ClickEvent e) {
					ConfirmPopover pop = new ConfirmPopover(
							new ConfirmListener() {

								@Override
								public void confirmed() {
									presenter.deleteComment(c, event);
								}
							}, Lang.get("events.comment.delete.conf"));
					pop.showRelativeTo(delete);
				}
			});
		}

		if (c.getImage() != null) {
			Image img = new Image();
			StreamSource ss = new StreamSource() {

				private static final long serialVersionUID = -377231260894914070L;

				@Override
				public InputStream getStream() {
					// TODO centralize and share the resources?
					return new ByteArrayInputStream(c.getImage().getContent());
				}
			};
			// use ID as a unique filename for browser caching
			StreamResource sr = new StreamResource(ss, c.getImage().getId()
					+ ".jpg");
			sr.setCacheTime(1000 * 60 * 60 * 24 * 7);// week
			sr.setMIMEType(c.getImage().getMimetype());
			img.setSource(sr);

			root.addComponent(img);
		}

		Label content = new Label(c.getContent());
		content.addStyleName("content");
		root.addComponent(content);
	}
}
