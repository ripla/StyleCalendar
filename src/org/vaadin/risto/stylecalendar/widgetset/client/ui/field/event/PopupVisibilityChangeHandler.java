package org.vaadin.risto.stylecalendar.widgetset.client.ui.field.event;

import com.google.gwt.event.shared.EventHandler;

public interface PopupVisibilityChangeHandler extends EventHandler {

    void handlePopupVisibilityChanged(PopupVisibilityChangedEvent event);
}
