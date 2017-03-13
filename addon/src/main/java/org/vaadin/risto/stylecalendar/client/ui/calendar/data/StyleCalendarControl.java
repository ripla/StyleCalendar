package org.vaadin.risto.stylecalendar.client.ui.calendar.data;

import java.io.Serializable;

public class StyleCalendarControl implements Serializable {

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
