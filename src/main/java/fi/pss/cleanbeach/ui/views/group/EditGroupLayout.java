package fi.pss.cleanbeach.ui.views.group;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.MyTouchKitUI;

public class EditGroupLayout extends NavigationView {

	private static final long serialVersionUID = 3250546797765104847L;
	private final UsersGroup group;
	private final GroupPresenter presenter;
	private final FieldGroup form;
	private final Label logoLabel;

	public EditGroupLayout(UsersGroup group, GroupPresenter presenter) {
		this.group = group;
		this.presenter = presenter;

		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		setContent(vl);

		if (isNew()) {
			setCaption("[Create group");
		} else {
			setCaption("[Edit group");
		}

		BeanItem<UsersGroup> item = new BeanItem<UsersGroup>(group);
		form = new FieldGroup(item);

		TextField groupName = new TextField("[group name");
		groupName.setWidth("100%");
		groupName.setNullRepresentation("");
		vl.addComponent(groupName);
		form.bind(groupName, "name");

		TextArea groupDesc = new TextArea("[group name");
		groupDesc.setWidth("100%");
		groupDesc.setRows(5);
		groupDesc.setNullRepresentation("");
		vl.addComponent(groupDesc);
		form.bind(groupDesc, "description");

		Upload logo = new Upload();
		logo.setImmediate(true);
		ImageUploader listener = new ImageUploader(logo);
		logo.setReceiver(listener);
		logo.addSucceededListener(listener);
		logo.addFailedListener(listener);
		logo.addProgressListener(listener);
		vl.addComponent(logo);

		logoLabel = new Label();
		logoLabel.setCaption("[logo");
		vl.addComponent(logoLabel);
		if (group.getLogo() == null) {
			logoLabel.setValue("[No logo uploaded");
		} else {
			logoLabel.setValue("[Logo not changed");
		}

		Button save = new Button("[save");
		vl.addComponent(save);
		save.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2803745753760737634L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					form.commit();
					EditGroupLayout.this.presenter.saveGroup(
							EditGroupLayout.this.group,
							MyTouchKitUI.getCurrentUser());
				} catch (CommitException e) {
					EditGroupLayout.this.presenter.handleError(e);
				}
			}
		});

	}

	private boolean isNew() {
		return group.getId() == null;
	}

	class ImageUploader implements Receiver, SucceededListener,
			ProgressListener, FailedListener {

		private static final long serialVersionUID = -6303362300158056742L;

		final static int maxLength = 128 * 1024; // 128K;
		ByteArrayOutputStream fos = null;
		String filename;
		Upload upload;

		public ImageUploader(Upload upload) {
			this.upload = upload;
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			this.filename = filename;
			fos = new ByteArrayOutputStream(maxLength + 1);
			return fos; // Return the output stream to write to
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			if (readBytes > maxLength) {
				Notification.show("[file too large");
				upload.interruptUpload();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void uploadSucceeded(SucceededEvent event) {
			Image uploadedImage = new Image();
			uploadedImage.setContent(fos.toByteArray());
			try {
				fos.close();
			} catch (IOException e) {
				presenter.handleError(e);
			}
			uploadedImage.setMimetype("image/jpeg");
			uploadedImage.setUploaded(new Date());

			form.getItemDataSource().getItemProperty("logo")
					.setValue(uploadedImage);
			logoLabel.setValue("[Logo has been changed");
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			Notification.show("[ upload failed); please try again");
		}
	};
}
