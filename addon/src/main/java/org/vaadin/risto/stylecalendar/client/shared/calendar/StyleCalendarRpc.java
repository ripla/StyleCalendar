package org.vaadin.risto.stylecalendar.client.shared.calendar;

import com.vaadin.shared.communication.ServerRpc;

public interface StyleCalendarRpc extends ServerRpc {

    void previousMonthClicked();

    void nextMonthClicked();

    void dayClicked(Integer clickedDay, Integer clickedDayIndex);
}
