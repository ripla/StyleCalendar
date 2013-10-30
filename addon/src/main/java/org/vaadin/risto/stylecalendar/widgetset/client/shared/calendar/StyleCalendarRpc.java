package org.vaadin.risto.stylecalendar.widgetset.client.shared.calendar;

import com.vaadin.shared.communication.ServerRpc;

public interface StyleCalendarRpc extends ServerRpc {

    public void previousMonthClicked();

    public void nextMonthClicked();

    public void dayClicked(Integer clickedDay, Integer clickedDayIndex);
}