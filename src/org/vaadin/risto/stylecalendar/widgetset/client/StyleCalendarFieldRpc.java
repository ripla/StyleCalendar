package org.vaadin.risto.stylecalendar.widgetset.client;

import com.vaadin.terminal.gwt.client.communication.ServerRpc;

public interface StyleCalendarFieldRpc extends ServerRpc {

    public void popupVisibilityChanged(boolean visible);

    public void setValue(String value);
}
