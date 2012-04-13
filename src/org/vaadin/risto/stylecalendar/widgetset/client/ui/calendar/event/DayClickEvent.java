package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event;

import com.google.gwt.event.shared.GwtEvent;

public class DayClickEvent extends GwtEvent<DayClickHandler> {

    private static Type<DayClickHandler> TYPE;

    private Integer clickedDay;
    private Integer clickedDayIndex;

    public DayClickEvent(Integer clickedDay, Integer clickedDayIndex) {
        this.clickedDay = clickedDay;
        this.clickedDayIndex = clickedDayIndex;
    }

    @Override
    public Type<DayClickHandler> getAssociatedType() {
        return getType();
    }

    public static Type<DayClickHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DayClickHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(DayClickHandler handler) {
        handler.onDayClick(this);
    }

    public Integer getClickedDay() {
        return clickedDay;
    }

    public void setClickedDay(Integer clickedDay) {
        this.clickedDay = clickedDay;
    }

    public Integer getClickedDayIndex() {
        return clickedDayIndex;
    }

    public void setClickedDayIndex(Integer clickedDayIndex) {
        this.clickedDayIndex = clickedDayIndex;
    }
}
