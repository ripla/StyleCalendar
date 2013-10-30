package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StyleCalendarWeek implements Serializable {

    private static final long serialVersionUID = -8224871323421938497L;

    private String weekNumber;
    private List<StyleCalendarDay> days;

    public StyleCalendarWeek() {
        this.days = new ArrayList<StyleCalendarDay>();
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(String weekNumber) {
        this.weekNumber = weekNumber;
    }

    public List<StyleCalendarDay> getDays() {
        return Collections.unmodifiableList(days);
    }

    public void setDays(List<StyleCalendarDay> days) {
        this.days = days;
    }

    public void addDay(StyleCalendarDay day) {
        this.days.add(day);
    }
}