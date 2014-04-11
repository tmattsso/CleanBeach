package fi.pss.cleanbeach.ui.views.eventdetails;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.mvp.AbstractView;

public abstract class CreateEventCapableView<T extends CreateEventPresenter<?>>
		extends AbstractView<T> implements ICreateEvent {

	private static final long serialVersionUID = -2875022567576606757L;

	@Override
	public void showCreateEvent(UsersGroup selectedGroup,
			Location selectedLocation) {
		navigateTo(new CreateEventLayout(selectedGroup, selectedLocation,
				presenter));
	}
}