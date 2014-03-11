package fi.pss.cleanbeach.ui.views.locations;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Label;

/**
 * TODO
 * 
 * @author thomas
 * 
 */
public class CreateEventLayout extends NavigationView {

	private static final long serialVersionUID = -98682367317399067L;

	{
		setContent(new Label("Add events!"));
		setCaption("New event:");
	}
}
