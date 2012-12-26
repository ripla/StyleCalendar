package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar;

import com.google.gwt.user.client.ui.Label;

public class DayLabel extends Label {

    private final String tooltip;

    public DayLabel(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getTooltip() {
        return tooltip;
    }
}
