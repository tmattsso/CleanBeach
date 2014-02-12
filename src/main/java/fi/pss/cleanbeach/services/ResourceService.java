/**
 *
 */
package fi.pss.cleanbeach.services;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.Stateless;

/**
 * @author denis
 * 
 */
@Stateless
public class ResourceService {

    private static final String MESSAGES = ResourceService.class.getPackage()
            .getName() + ".messages";

    public String getMessage(Locale locale, String key, Object... params) {
        String message = ResourceBundle.getBundle(MESSAGES,
                locale == null ? Locale.getDefault() : locale).getString(key);
        if (params.length > 0) {
            return MessageFormat.format(message, params);
        } else {
            return message;
        }
    }
}
