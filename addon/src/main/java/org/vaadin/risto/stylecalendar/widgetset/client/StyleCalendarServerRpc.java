package org.vaadin.risto.stylecalendar.widgetset.client;

import com.vaadin.terminal.gwt.client.communication.ServerRpc;

public interface StyleCalendarServerRpc extends ServerRpc {

    public void previousMonthClicked();

    public void nextMonthClicked();

    public void dayClicked(Integer clickedDay, Integer clickedDayIndex);
}