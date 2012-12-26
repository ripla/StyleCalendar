package org.vaadin.risto.stylecalendar.widgetset.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.VStyleCalendarControl;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.VStyleCalendarWeek;

import com.vaadin.shared.AbstractFieldState;

public class StyleCalendarState extends AbstractFieldState {

    private static final long serialVersionUID = 3182947665115383514L;

    private boolean renderWeekNumbers;
    private boolean renderHeader;
    private boolean renderControls;

    private List<VStyleCalendarWeek> weeks;
    private List<String> weekDayNames;

    private VStyleCalendarControl previousMonthControl;
    private VStyleCalendarControl nextMonthControl;

    private String currentMonth;
    private String currentYear;

    public StyleCalendarState() {
        weeks = new ArrayList<VStyleCalendarWeek>();
        weekDayNames = new ArrayList<String>();
    }

    public boolean isRenderWeekNumbers() {
        return renderWeekNumbers;
    }

    public void setRenderWeekNumbers(boolean renderWeekNumbers) {
        this.renderWeekNumbers = renderWeekNumbers;
    }

    public boolean isRenderHeader() {
        return renderHeader;
    }

    public void setRenderHeader(boolean renderHeader) {
        this.renderHeader = renderHeader;
    }

    public boolean isRenderControls() {
        return renderControls;
    }

    public void setRenderControls(boolean renderControls) {
        this.renderControls = renderControls;
    }

    public List<VStyleCalendarWeek> getWeeks() {
        return Collections.unmodifiableList(weeks);
    }

    public void setWeeks(List<VStyleCalendarWeek> weeks) {
        this.weeks = weeks;
    }

    public void addWeek(VStyleCalendarWeek week) {
        weeks.add(week);
    }

    public void clearWeeks() {
        weeks.clear();
    }

    public List<String> getWeekDayNames() {
        return Collections.unmodifiableList(weekDayNames);
    }

    public void setWeekDayNames(List<String> weekDayNames) {
        this.weekDayNames = weekDayNames;
    }

    public void addWeekDayName(String weekDayName) {
        weekDayNames.add(weekDayName);
    }

    public void clearWeekDayNames() {
        weekDayNames.clear();
    }

    public VStyleCalendarControl getPreviousMonthControl() {
        return previousMonthControl;
    }

    public void setPreviousMonthControl(
            VStyleCalendarControl previousMonthControl) {
        this.previousMonthControl = previousMonthControl;
    }

    public VStyleCalendarControl getNextMonthControl() {
        return nextMonthControl;
    }

    public void setNextMonthControl(VStyleCalendarControl nextMonthControl) {
        this.nextMonthControl = nextMonthControl;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }
}
