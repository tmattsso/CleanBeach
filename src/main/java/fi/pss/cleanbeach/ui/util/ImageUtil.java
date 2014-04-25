package fi.pss.cleanbeach.ui.util;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.imgscalr.Scalr;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Image;

import fi.pss.cleanbeach.data.UsersGroup;

public class ImageUtil {

	private static final int[] RGB_MASKS = { 0xFF0000, 0xFF00, 0xFF };
	private static final ColorModel RGB_OPAQUE = new DirectColorModel(32,
			RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);

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

	public static byte[] resizeIfBigger(byte[] image, int maxSize,
			String mimetype) {

		String filetype = mimetype.contains("png") ? "png" : "jpg";

		ImageInputStream iis = null;
		try {

			java.awt.Image in = Toolkit.getDefaultToolkit().createImage(image);
			in.flush();

			PixelGrabber pg = new PixelGrabber(in, 0, 0, -1, -1, true);
			pg.grabPixels();
			int width = pg.getWidth(), height = pg.getHeight();

			if (width < maxSize && height < maxSize) {
				// image is already smaller
				return image;
			}

			DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(),
					pg.getWidth() * pg.getHeight());
			WritableRaster raster = Raster.createPackedRaster(buffer, width,
					height, width, RGB_MASKS, null);
			BufferedImage out = new BufferedImage(RGB_OPAQUE, raster, false,
					null);

			BufferedImage thumbnail = Scalr.resize(out, maxSize);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			boolean write = ImageIO.write(thumbnail, filetype, output);
			if (write) {
				image = output.toByteArray();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		return image;
	}
}
