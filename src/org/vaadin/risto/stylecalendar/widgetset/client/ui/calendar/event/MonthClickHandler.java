package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event;

import com.google.gwt.event.shared.EventHandler;

public interface MonthClickHandler extends EventHandler {

    public void onMonthClick(MonthClickEvent event);
}