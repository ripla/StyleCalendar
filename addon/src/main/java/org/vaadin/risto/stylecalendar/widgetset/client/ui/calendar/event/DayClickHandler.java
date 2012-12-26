package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event;

import com.google.gwt.event.shared.EventHandler;

public interface DayClickHandler extends EventHandler {

    void onDayClick(DayClickEvent event);
}