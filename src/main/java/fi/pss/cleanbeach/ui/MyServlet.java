package fi.pss.cleanbeach.ui;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.cdi.CDIUIProvider;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

@SuppressWarnings("serial")
@WebServlet("/*")
@VaadinServletConfiguration(productionMode = true, ui = MainAppUI.class)
public class MyServlet extends TouchKitServlet {

	private final CDIUIProvider cdiProvider = new CDIUIProvider();

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().addSessionInitListener(new SessionInitListener() {
			@Override
			public void sessionInit(SessionInitEvent event)
					throws ServiceException {
				event.getSession().addUIProvider(cdiProvider);
			}
		});
	}

}
