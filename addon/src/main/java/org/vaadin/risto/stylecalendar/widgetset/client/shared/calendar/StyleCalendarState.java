package org.vaadin.risto.stylecalendar.widgetset.client.shared.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.data.StyleCalendarControl;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.data.StyleCalendarWeek;

import com.vaadin.shared.AbstractFieldState;

public class StyleCalendarState extends AbstractFieldState {

    private static final long serialVersionUID = 3182947665115383514L;

    private boolean renderWeekNumbers;
    private boolean renderHeader;
    private boolean renderControls;

    private List<StyleCalendarWeek> weeks;
    private List<String> weekDayNames;

    private StyleCalendarControl previousMonthControl;
    private StyleCalendarControl nextMonthControl;

    private String currentMonth;
    private String currentYear;

    public boolean hasTooltip;

    public StyleCalendarState() {
        weeks = new ArrayList<StyleCalendarWeek>();
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

    public List<StyleCalendarWeek> getWeeks() {
        return Collections.unmodifiableList(weeks);
    }

    public void setWeeks(List<StyleCalendarWeek> weeks) {
        this.weeks = weeks;
    }

    public void addWeek(StyleCalendarWeek week) {
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

    public StyleCalendarControl getPreviousMonthControl() {
        return previousMonthControl;
    }

    public void setPreviousMonthControl(
            StyleCalendarControl previousMonthControl) {
        this.previousMonthControl = previousMonthControl;
    }

    public StyleCalendarControl getNextMonthControl() {
        return nextMonthControl;
    }

    public void setNextMonthControl(StyleCalendarControl nextMonthControl) {
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
