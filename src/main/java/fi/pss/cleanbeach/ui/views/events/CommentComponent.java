package fi.pss.cleanbeach.ui.views.events;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.Comment;

public class CommentComponent extends CustomComponent {

	private static final long serialVersionUID = -457189718760395829L;

	public CommentComponent(final Comment c) {

		CssLayout root = new CssLayout();
		setCompositionRoot(root);
		root.setWidth("100%");
		addStyleName("comment");

		Label author = new Label(c.getWritetime() + " "
				+ c.getAuthor().getName(), ContentMode.HTML);
		author.addStyleName("author");
		root.addComponent(author);

		if (c.getImage() != null) {
			Image img = new Image();
			img.setWidth("100%");

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
