package org.vaadin.risto.stylecalendar;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class StyleCalendarDemoUIProvider extends UIProvider {

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        String value = event.getRequest().getParameter("ticket");
        if ("7".equals(value)) {
            return StyleCalendarFieldFullWidthUI.class;
        } else {
            return StyleCalendarDemoUI.class;
        }
    }
}
