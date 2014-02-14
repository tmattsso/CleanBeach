package fi.pss.cleanbeach.ui.gwt.client;

import com.vaadin.addon.touchkit.gwt.client.ThemeLoader;

public class CleanBeachThemeLoader extends ThemeLoader {

	@Override
	public final void load() {
		// Load default TouchKit theme...
		super.load();
		// ... and app specific additions from own client bundle
		CleanBeachBundle.INSTANCE.loginCss().ensureInjected();
	}

}