package fi.pss.cleanbeach.ui.views.group;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.util.ExceptionUtil;
import fi.pss.cleanbeach.ui.util.ImageUtil;
import fi.pss.cleanbeach.ui.util.Lang;

public class EditGroupLayout extends NavigationView {

	private static final long serialVersionUID = 3250546797765104847L;
	private final UsersGroup group;
	private final GroupPresenter presenter;
	private final FieldGroup form;
	private final Upload logo;
	private com.vaadin.ui.Image logoImg;
	private final VerticalLayout root;

	public EditGroupLayout(UsersGroup group, GroupPresenter presenter) {
		this.group = group;
		this.presenter = presenter;

		root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		setContent(root);

		if (isNew()) {
			setCaption(Lang.get("Group.edit.caption.new"));
		} else {
			setCaption(Lang.get("Group.edit.caption"));
		}

		BeanItem<UsersGroup> item = new BeanItem<UsersGroup>(group);
		form = new FieldGroup(item);

		TextField groupName = new TextField(Lang.get("Group.edit.name"));
		groupName.setWidth("100%");
		groupName.setNullRepresentation("");
		groupName.setRequired(true);
		root.addComponent(groupName);
		form.bind(groupName, "name");

		TextArea groupDesc = new TextArea(Lang.get("Group.edit.desc"));
		groupDesc.setWidth("100%");
		groupDesc.setRows(5);
		groupDesc.setRequired(true);
		groupDesc.setNullRepresentation("");
		root.addComponent(groupDesc);
		form.bind(groupDesc, "description");

		if (group.getLogo() != null) {

			logoImg = ImageUtil.getGroupLogo(group);
		} else {
			logoImg = new com.vaadin.ui.Image();
		}
		logoImg.addStyleName("logo");
		root.addComponent(logoImg);
		root.setComponentAlignment(logoImg, Alignment.MIDDLE_CENTER);

		logo = new Upload();
		if (group.getLogo() == null) {
			logo.setButtonCaption(Lang.get("Group.edit.logo.nologo"));
		} else {
			logo.setButtonCaption(Lang.get("Group.edit.logo.notedited"));
		}
		logo.setWidth("100%");
		logo.setImmediate(true);
		ImageUploader listener = new ImageUploader(logo);
		logo.setReceiver(listener);
		logo.addSucceededListener(listener);
		logo.addFailedListener(listener);
		logo.addProgressListener(listener);
		root.addComponent(logo);

		Button save = new Button(Lang.get("Group.edit.save"));
		root.addComponent(save);
		save.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2803745753760737634L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					form.commit();
					EditGroupLayout.this.presenter.saveGroup(
							EditGroupLayout.this.group,
							MainAppUI.getCurrentUser());
				} catch (CommitException e) {
					if (ExceptionUtil.getRootCause(e) instanceof EmptyValueException) {
						Notification.show(Lang.get("Group.edit.error.fillall"));
					} else {
						EditGroupLayout.this.presenter.handleError(e);
					}
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

		final static int maxLength = 1024 * 1024; // 1M;
		ByteArrayOutputStream fos = null;
		String filename;
		Upload upload;

		boolean failedBecauseTooLarge = false;

		public ImageUploader(Upload upload) {
			this.upload = upload;
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			failedBecauseTooLarge = false;
			this.filename = filename;
			fos = new ByteArrayOutputStream(maxLength + 1);
			return fos; // Return the output stream to write to
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			if (readBytes > maxLength) {
				Notification.show(Lang.get("Group.edit.filetoolarge"),
						Type.ERROR_MESSAGE);
				failedBecauseTooLarge = true;
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
			logo.setButtonCaption(Lang.get("Group.edit.logo.edited"));

			com.vaadin.ui.Image temp = ImageUtil.getGroupLogo(group);

			root.replaceComponent(logoImg, temp);
			logoImg = temp;
			root.setComponentAlignment(logoImg, Alignment.MIDDLE_CENTER);
			logoImg.addStyleName("logo");
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			if (!failedBecauseTooLarge) {
				Notification.show(Lang.get("Group.edit.uploadfailed"),
						Type.ERROR_MESSAGE);
			}
		}
	};
}
