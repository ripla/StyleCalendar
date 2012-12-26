package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VStyleCalendarWeek {
    private String weekNumber;
    private List<VStyleCalendarDay> days;

    public VStyleCalendarWeek() {
        this.days = new ArrayList<VStyleCalendarDay>();
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(String weekNumber) {
        this.weekNumber = weekNumber;
    }

    public List<VStyleCalendarDay> getDays() {
        return Collections.unmodifiableList(days);
    }

    public void setDays(List<VStyleCalendarDay> days) {
        this.days = days;
    }

    public void addDay(VStyleCalendarDay day) {
        this.days.add(day);
    }
}