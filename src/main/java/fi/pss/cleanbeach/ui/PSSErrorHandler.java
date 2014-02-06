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

@SessionScoped
public class PSSErrorHandler implements ErrorHandler, Serializable {

	private static final long serialVersionUID = 2471422708554126603L;

	private Logger log;

	@Override
	public void error(ErrorEvent event) {

		log.log(Level.SEVERE, "Uncaught Exception in UI", event.getThrowable());

		Throwable root = getRootCause(event.getThrowable());
		// log.error("Root cause:", root);

		Notification n = new Notification("Exception caught", "<br/>"
				+ root.getClass().getSimpleName() + ": " + root.getMessage(),
				Type.ERROR_MESSAGE, true);
		n.setHtmlContentAllowed(true);
		n.setDescription("<br/>" + root.getClass().getSimpleName() + ": "
				+ root.getMessage());
		n.setDelayMsec(-1);

		n.show(Page.getCurrent());
	}

	private static Throwable getRootCause(Throwable throwable) {

		Throwable root = throwable;
		while (root.getCause() != null && root.getCause() != root) {
			root = root.getCause();
		}
		return root;
	}

}
