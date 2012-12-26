package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.terminal.gwt.client.VTooltip;

public class DayLabel extends Label {

    private final VStyleCalendar parent;
    private final Integer index;

    public DayLabel(Integer index, VStyleCalendar parent) {
        this.index = index;
        this.parent = parent;
        sinkEvents(VTooltip.TOOLTIP_EVENTS);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        parent.handleTooltipEvent(event, this);
    }

    public Integer getIndex() {
        return index;
    }
}
