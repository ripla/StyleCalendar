package org.vaadin.risto.stylecalendar.client.shared.field;

import com.vaadin.shared.communication.ServerRpc;

public interface StyleCalendarFieldRpc extends ServerRpc {

    void popupVisibilityChanged(boolean visible);

    void setValue(String value);
}
