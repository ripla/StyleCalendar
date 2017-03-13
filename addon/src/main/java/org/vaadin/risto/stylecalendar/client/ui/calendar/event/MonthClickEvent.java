package org.vaadin.risto.stylecalendar.client.ui.calendar.event;

import com.google.gwt.event.shared.GwtEvent;

public class MonthClickEvent extends GwtEvent<MonthClickHandler> {

    private static Type<MonthClickHandler> TYPE;

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<MonthClickHandler> getAssociatedType() {
        return getType();
    }

    public static Type<MonthClickHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(MonthClickHandler handler) {
        handler.onMonthClick(this);
    }

}
