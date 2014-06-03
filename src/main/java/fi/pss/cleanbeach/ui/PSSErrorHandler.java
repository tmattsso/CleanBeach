package fi.pss.cleanbeach.ui;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;

import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import fi.pss.cleanbeach.ui.util.ExceptionUtil;
import fi.pss.cleanbeach.ui.util.Lang;

@SessionScoped
public class PSSErrorHandler implements ErrorHandler, Serializable {

	private static final long serialVersionUID = 2471422708554126603L;

	private static final boolean prodMode = false;

	private transient final Logger log = Logger.getLogger("ErrorLogger");

	@Override
	public void error(ErrorEvent event) {

		log.log(Level.SEVERE, "Current user "
				+ MainAppUI.getCurrentUser().getEmail());
		log.log(Level.SEVERE, "Uncaught Exception in UI", event.getThrowable());

		Throwable root = ExceptionUtil.getRootCause(event.getThrowable());

		Notification n = new Notification("", "", Type.ERROR_MESSAGE, true);
		n.setHtmlContentAllowed(true);
		n.setCaption(Lang.get("main.errornotification.caption"));
		n.setDelayMsec(-1);

		if (prodMode) {
			n.setDescription(Lang.get("main.errornotification.msg"));
		} else {
			n.setDescription("<br/>" + root.getClass().getSimpleName() + ": "
					+ root.getMessage());
		}

		n.show(Page.getCurrent());
	}

}
