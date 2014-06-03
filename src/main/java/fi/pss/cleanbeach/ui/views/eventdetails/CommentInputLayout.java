package fi.pss.cleanbeach.ui.views.eventdetails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.ui.util.Lang;

public class CommentInputLayout extends NavigationView {

	private static final long serialVersionUID = 5640950674463792976L;
	private final Upload addImage;

	private Image uploadedImage;
	private final EventDetailsPresenter<?> presenter;
	private final Button saveComment;

	public CommentInputLayout(final fi.pss.cleanbeach.data.Event e,

	final EventDetailsPresenter<?> presenter) {

		this.presenter = presenter;
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		root.setSizeFull();
		addStyleName("addcomment");

		setContent(root);
		setCaption(Lang.get("events.comment.addcomment"));

		final TextArea comment = new TextArea();
		comment.setSizeFull();
		root.addComponent(comment);
		root.setExpandRatio(comment, 1);

		addImage = new Upload();
		addImage.setButtonCaption(Lang.get("events.comment.addimagebutton"));
		addImage.setImmediate(true);
		addImage.setWidth("100%");
		ImageUploader ul = new ImageUploader(addImage);
		addImage.setReceiver(ul);
		addImage.addSucceededListener(ul);
		addImage.addFailedListener(ul);
		addImage.setIcon(FontAwesome.CAMERA_RETRO);
		root.addComponent(addImage);

		saveComment = new Button(Lang.get("events.comment.addcomment"));
		saveComment.setIcon(FontAwesome.COMMENT);
		saveComment.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 5577262959129927178L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.addComment(e, comment.getValue(), uploadedImage);
			}
		});
		root.addComponent(saveComment);

	}

	// Implement both receiver that saves upload in a file and
	// listener for successful upload
	class ImageUploader implements Receiver, SucceededListener, FailedListener,
			ProgressListener {
		private static final long serialVersionUID = 1L;

		final static int maxLength = 5 * 1024 * 1024;// 5M
		ByteArrayOutputStream fos = null;
		String filename;
		Upload upload;

		public ImageUploader(Upload upload) {
			this.upload = upload;
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			this.filename = filename;
			fos = new ByteArrayOutputStream();
			return fos; // Return the output stream to write to
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			saveComment.setEnabled(false);
			if (readBytes > maxLength) {
				Notification.show(Lang.get("events.comment.imageFailed"));
				upload.interruptUpload();
				saveComment.setEnabled(true);
			}
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			uploadedImage = new Image();
			uploadedImage.setContent(fos.toByteArray());
			try {
				fos.close();
			} catch (IOException e) {
				presenter.handleError(e);
			}
			uploadedImage.setMimetype("image/jpeg");
			uploadedImage.setUploaded(new Date());

			saveComment.setEnabled(true);
		}

		@Override
		public void uploadFailed(FailedEvent event) {

			Notification.show(Lang.get("events.comment.imageFailed"));
		}
	};
}
