package fi.pss.cleanbeach.standalone.map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.cdi.CDIUIProvider;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("maptheme")
@UIScoped
public class MapUI extends UI {

	private static final long serialVersionUID = 8560450329285696314L;

	@WebServlet("/map/*")
	@VaadinServletConfiguration(ui = MapUI.class, productionMode = false, widgetset = "fi.pss.cleanbeach.standalone.map.MapWidgetSet")
	public static class MapServlet extends VaadinServlet {

		private static final long serialVersionUID = 3342817781686266438L;

		private final CDIUIProvider cdiProvider = new CDIUIProvider() {
			private static final long serialVersionUID = 8587130632158809786L;

			@Override
			public Class<? extends UI> getUIClass(
					UIClassSelectionEvent selectionEvent) {
				return MapUI.class;
			}
		};

		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
			getService().addSessionInitListener(new SessionInitListener() {
				private static final long serialVersionUID = -7323757409778480145L;

				@Override
				public void sessionInit(SessionInitEvent event)
						throws ServiceException {
					event.getSession().addUIProvider(cdiProvider);
				}
			});
		}
	}

	@Inject
	private MapComponent map;

	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();
		setContent(map);
		map.init();
	}

}
