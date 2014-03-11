/**
 *
 */
package fi.pss.cleanbeach.ui.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.vaadin.ui.UI;

public class Lang {

	private static final Locale DEFAULTLANG = new Locale("en");

	private static Map<Locale, ResourceBundle> bundles = new HashMap<>();

	protected static Locale getLocaleInUse() {
		// load from browser
		Locale l = UI.getCurrent().getLocale();

		if (l == null) {
			l = DEFAULTLANG;
			UI.getCurrent().setLocale(l);
		} else if (l.getCountry() != null) {
			// parse only language
			l = new Locale(l.getLanguage());
			UI.getCurrent().setLocale(l);
		}

		// test loading bundle
		if (!loadBundle(l)) {

			ResourceBundle b = bundles.get(l);
			if (b == null) {
				System.out.println("Bundle not available for locale '" + l
						+ "', replacing with default (" + DEFAULTLANG + ")");
				l = DEFAULTLANG;
				loadBundle(l);
			}
		}

		return l;
	}

	protected static ResourceBundle getBundle() {
		return bundles.get(getLocaleInUse());
	}

	/**
	 * Return translated string.
	 */
	public static String get(String key) {

		// Check for empty
		if (key == null) {
			return null;
		}

		// Translate
		String translatedStr = null;
		try {
			translatedStr = getBundle().getString(key);
		} catch (MissingResourceException e) {
			translatedStr = null;
		}
		if (translatedStr != null) {
			// Return translated string
			return translatedStr;
		} else {
			// Unstranslated (string not found)
			return "*" + key;
		}
	}

	public static String get(Enum<?> e) {

		String key = e.getClass().getName() + "." + e.getClass();
		return get(key);
	}

	/**
	 * Return translated string.
	 * <p>
	 * Use {0} inside translation file for each parameter:<br/>
	 * <code>Hello user {0}, welcome to {1}!</code> <br/>
	 * then call this method like this:<br/>
	 * <code>get("id", user.getFirstName(), Application.getName());</code>
	 * 
	 */
	public static String get(String key, Object... args) {
		String translated = get(key);
		if (args != null && args.length != 0) {
			translated = MessageFormat.format(translated, args);
		}
		return translated;
	}

	/**
	 * Load resource bundle for given Locale.
	 * 
	 * @param locale
	 *            Locale of the resource bundle
	 * @return ResourceBundle or null if not found.
	 */
	protected static boolean loadBundle(Locale locale)
			throws MissingResourceException {

		if (bundles.containsKey(locale)) {
			return false;
		}

		ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
		synchronized (bundles) {
			bundles.put(locale, bundle);
		}
		System.out.println("Loaded translations 'translations' for locale '"
				+ locale + "'");

		return true;
	}

}
