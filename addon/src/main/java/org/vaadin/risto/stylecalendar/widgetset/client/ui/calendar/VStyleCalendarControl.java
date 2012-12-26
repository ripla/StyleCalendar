package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar;

import java.io.Serializable;

public class VStyleCalendarControl implements Serializable {

    private static final long serialVersionUID = 2194350557619354990L;
    private String text;
    private boolean enabled;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
