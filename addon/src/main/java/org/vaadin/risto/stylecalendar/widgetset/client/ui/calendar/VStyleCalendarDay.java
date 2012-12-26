package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar;

import java.io.Serializable;

public class VStyleCalendarDay implements Serializable {

    private static final long serialVersionUID = -2847418714090484578L;

    private Integer number;
    private Integer index;
    private String style;
    private boolean clickable;
    private boolean disabled;
    private String tooltip;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
