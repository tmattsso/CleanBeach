package fi.pss.cleanbeach.ui.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Image;

import fi.pss.cleanbeach.data.UsersGroup;

public class ImageUtil {

	public static Image getGroupLogo(UsersGroup group) {
		if (group == null || group.getLogo() == null) {
			return null;
		}
		return createLogoComponent(group.getName(), group.getLogo()
				.getContent(), group.getLogo().getMimetype());
	}

	private static com.vaadin.ui.Image createLogoComponent(String groupName,
			final byte[] content, String mime) {
		com.vaadin.ui.Image image = new com.vaadin.ui.Image();
		StreamSource source = new StreamSource() {

			private static final long serialVersionUID = 158820412989991373L;

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(content);
			}
		};
		String filename = groupName;
		if (mime.contains("png")) {
			filename += ".png";
		} else {
			filename += ".jpg";
		}
		// TODO cache resource per app?
		StreamResource resource = new StreamResource(source, filename);
		resource.setMIMEType(mime);
		resource.setCacheTime(1000 * 60 * 60 * 24 * 7);// 7 days
		image.setSource(resource);
		return image;
	}
}
