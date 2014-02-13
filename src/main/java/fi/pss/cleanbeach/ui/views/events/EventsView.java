package fi.pss.cleanbeach.ui.views.events;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.mvp.AbstractView;

@UIScoped
public class EventsView extends AbstractView<EventsPresenter> implements
		ClickListener {

	private static final long serialVersionUID = -259521650823470699L;
	private Button allEvents;
	private Button joinedEvents;
	private Button search;
	private CssLayout content;

	public EventsView() {
		setCaption("Events");
	}

	@Override
	protected ComponentContainer getMainContent() {

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(false);

		Toolbar tabs = new Toolbar();

		allEvents = new Button("All events");
		allEvents.addClickListener(this);
		allEvents.setData(new WallLayout());
		allEvents.addStyleName("selected");
		tabs.addComponent(allEvents);

		joinedEvents = new Button("Joined events");
		joinedEvents.addClickListener(this);
		joinedEvents.setData(new WallLayout());
		tabs.addComponent(joinedEvents);

		search = new Button("Search");
		search.addClickListener(this);
		search.setData(new Label("search!"));
		tabs.addComponent(search);

		content = new CssLayout((Component) allEvents.getData());
		content.setSizeFull();

		vl.addComponent(tabs);
		vl.addComponent(content);
		vl.setExpandRatio(content, 1);
		return vl;
	}

	@Override
	@Inject
	public void injectPresenter(EventsPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		allEvents.removeStyleName("selected");
		joinedEvents.removeStyleName("selected");
		search.removeStyleName("selected");

		content.removeAllComponents();
		content.addComponent((Component) event.getButton().getData());
		event.getButton().addStyleName("selected");

	}

}
