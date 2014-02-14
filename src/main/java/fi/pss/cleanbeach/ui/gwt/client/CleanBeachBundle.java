package fi.pss.cleanbeach.ui.gwt.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface CleanBeachBundle extends ClientBundle {

	CleanBeachBundle INSTANCE = GWT.create(CleanBeachBundle.class);

	@Source("login.css")
	LoginCss loginCss();

	// @Source("parking.ttf")
	// @DoNotEmbed
	// DataResource parkingFont();

}