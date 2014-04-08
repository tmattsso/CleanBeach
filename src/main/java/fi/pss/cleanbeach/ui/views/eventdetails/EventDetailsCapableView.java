package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.Collection;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.ui.mvp.AbstractView;

public abstract class EventDetailsCapableView<T extends EventDetailsPresenter<?>>
		extends AbstractView<T> implements IEventDetails {

	private static final long serialVersionUID = 5027141664466020875L;

	protected EventDetailLayout details;

	public EventDetailsCapableView() {
		super();
	}

	@Override
	public void showDetails(fi.pss.cleanbeach.data.Event e) {
		navigateTo(details = new EventDetailLayout(e, presenter));
	}

	@Override
	public void openThrashDetails(fi.pss.cleanbeach.data.Event e) {
		navigateTo(new ThrashInputEventLayout(e, presenter));
	}

	@Override
	public void openAddComment(fi.pss.cleanbeach.data.Event e, boolean addImage) {
		navigateTo(new CommentInputLayout(e, false, presenter));
	}

	@Override
	public void updateEventDetails(fi.pss.cleanbeach.data.Event e) {
		details.update(e);
	}

	@Override
	public void openInviteGroups(Collection<Invite> coll,
			fi.pss.cleanbeach.data.Event e, User u) {
		navigateTo(new InviteGroupsLayout(coll, e, u, presenter));
	}

	@Override
	public void navigateAndUpdate(fi.pss.cleanbeach.data.Event e) {
		while (getCurrentComponent() != details) {
			navigateBack();
		}
		updateEventDetails(e);
	}

}