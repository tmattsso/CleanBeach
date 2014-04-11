package fi.pss.cleanbeach.ui.views.eventdetails;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.mvp.IView;

public interface ICreateEvent extends IView {

	void showCreateEvent(UsersGroup selectedGroup, Location selectedLocation);

}
