package org.vaadin.risto.stylecalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.vaadin.risto.stylecalendar.widgetset.client.ui.VStyleCalendar;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ClientWidget;

/**
 * 
 * StyleCalendar is designed to be a simple, easily stylable calendar component.
 * 
 * {@link DateOptionsGenerator} can be used to generate stylenames externally.
 * 
 * @author Risto Yrjänä / Vaadin
 * 
 */
@ClientWidget(VStyleCalendar.class)
public class StyleCalendar extends AbstractField {

    private static final long serialVersionUID = 7797206568110243067L;

    private DateOptionsGenerator dateOptionsGenerator;

    private boolean renderControls;

    private boolean renderHeader;

    private boolean renderWeekNumbers;

    private Date showingDate = null;

    private List<Integer> disabledRenderedDays;

    private Date enabledStartDate;

    private Date enabledEndDate;

    private boolean nextMonthEnabled;

    private boolean prevMonthEnabled;

    /**
     * Create a new StyleCalendar instance. Header, controls and week numbers
     * are rendered by default.
     */
    public StyleCalendar() {
        super();
        setRenderHeader(true);
        setRenderControls(true);
        setRenderWeekNumbers(true);

        setShowingDate(new Date());
    }

    /**
     * Create a new StyleCalendar instance. Header, controls and week numbers
     * are rendered by default.
     * 
     * @param caption
     *            components caption
     */
    public StyleCalendar(String caption) {
        this();
        setCaption(caption);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractField#getType()
     */
    @Override
    public Class<?> getType() {
        return Date.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.AbstractField#paintContent(com.vaadin.terminal.PaintTarget)
     */
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        // init calendar and date related variables
        Date selectedDate = (Date) getValue();
        Date today = new Date();
        Date showingDate = getShowingDate();
        Locale locale = getLocale();

        // for remembering disabled variables sent to client
        disabledRenderedDays = new ArrayList<Integer>();
        nextMonthEnabled = true;
        prevMonthEnabled = true;

        Calendar calendar = getCalendarInstance();

        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int daysInWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();

        // set main tag attributes
        target.addAttribute(VStyleCalendar.ATTR_RENDER_WEEK_NUMBERS,
                isRenderWeekNumbers());
        target.addAttribute(VStyleCalendar.ATTR_RENDER_HEADER, isRenderHeader());
        target.addAttribute(VStyleCalendar.ATTR_RENDER_CONTROLS,
                isRenderControls());

        // render header
        if (isRenderHeader()) {
            renderHeader(target);
        }

        // render weekday names
        Calendar calendarForWeekdays = (Calendar) calendar.clone();
        calendarForWeekdays.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

        String[] weekDaysArray = new String[daysInWeek];
        for (int weekday = 0; weekday < daysInWeek; weekday++) {
            weekDaysArray[weekday] = calendarForWeekdays.getDisplayName(
                    Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
            calendarForWeekdays.add(Calendar.DAY_OF_WEEK, 1);
        }

        target.addVariable(this, VStyleCalendar.ATTR_WEEK_DAY_NAMES,
                weekDaysArray);

        // render weeks and days

        int dayIndex = 0;

        // compute week start days
        Set<Date> firstDaysOfWeek = getFirsDaysOfWeeks();

        for (Date weekStartDate : firstDaysOfWeek) {

            // reset to the start of the week
            calendar.setTime(weekStartDate);
            target.startTag(VStyleCalendar.TAG_WEEK);

            target.addAttribute(VStyleCalendar.ATTR_WEEK_NUMBER,
                    calendar.get(Calendar.WEEK_OF_YEAR));

            for (int day = 0; day < daysInWeek; day++) {
                target.startTag(VStyleCalendar.TAG_DAY);
                target.addAttribute(VStyleCalendar.ATTR_DAY_NUMBER,
                        calendar.get(Calendar.DAY_OF_MONTH));
                target.addAttribute(VStyleCalendar.ATTR_DAY_INDEX, dayIndex);

                // compute styles for given day
                StringBuilder dayStyle = new StringBuilder();

                if (dayEquals(calendar.getTime(), today)) {
                    dayStyle.append("today");
                }

                if (dayEquals(calendar.getTime(), selectedDate)) {
                    dayStyle.append(" ");
                    dayStyle.append("selected");
                }

                if (monthEquals(calendar.getTime(), showingDate)) {
                    dayStyle.append(" ");
                    dayStyle.append("currentmonth");
                    target.addAttribute(VStyleCalendar.ATTR_DAY_CLICKABLE, true);
                } else {
                    dayStyle.append(" ");
                    dayStyle.append("othermonth");
                    target.addAttribute(VStyleCalendar.ATTR_DAY_CLICKABLE,
                            false);
                }

                if (isWeekend(calendar.getTime())) {
                    dayStyle.append(" ");
                    dayStyle.append("weekend");
                }

                if (getDateOptionsGenerator() != null) {
                    String generatedStyle = getDateOptionsGenerator()
                            .getStyleName(calendar.getTime(), this);
                    if (generatedStyle != null) {
                        dayStyle.append(" ");
                        dayStyle.append(generatedStyle);
                    }

                    String tooltip = getDateOptionsGenerator().getTooltip(
                            calendar.getTime(), this);
                    if (tooltip != null) {
                        target.addAttribute(VStyleCalendar.ATTR_DAY_TOOLTIP,
                                tooltip);
                    }
                }

                if (isDisabledDate(calendar.getTime())) {
                    target.addAttribute(VStyleCalendar.ATTR_DAY_DISABLED, true);
                    disabledRenderedDays.add(dayIndex);
                }

                String dayStyleString = dayStyle.toString();
                if (!dayStyleString.isEmpty()) {
                    target.addAttribute(VStyleCalendar.ATTR_DAY_STYLE,
                            dayStyleString);
                }

                target.endTag(VStyleCalendar.TAG_DAY);

                // move to the next day
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                ++dayIndex;
            }
            target.endTag(VStyleCalendar.TAG_WEEK);
        }
    }

    protected void renderHeader(PaintTarget target) throws PaintException {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(getShowingDate());
        target.startTag(VStyleCalendar.TAG_HEADER);
        target.addAttribute(VStyleCalendar.ATTR_HEADER_CURRENT_YEAR,
                calendar.get(Calendar.YEAR));
        target.addAttribute(VStyleCalendar.ATTR_HEADER_CURRENT_MONTH,
                getMonthCaption(calendar.getTime(), 0, true));

        // render controls
        if (isRenderControls()) {
            target.startTag(VStyleCalendar.TAG_CONTROLS);

            target.addAttribute(VStyleCalendar.ATTR_CONTROLS_PREV_MONTH,
                    getMonthCaption(calendar.getTime(), -1, false));

            if (isDisabledMonth(calendar.getTime(), -1)) {
                target.addAttribute(
                        VStyleCalendar.ATTR_CONTROLS_PREV_MONTH_DISABLED, true);
                prevMonthEnabled = false;
            }

            target.addAttribute(VStyleCalendar.ATTR_CONTROLS_NEXT_MONTH,
                    getMonthCaption(calendar.getTime(), 1, false));

            if (isDisabledMonth(calendar.getTime(), 1)) {
                target.addAttribute(
                        VStyleCalendar.ATTR_CONTROLS_NEXT_MONTH_DISABLED, true);
                nextMonthEnabled = false;
            }

            target.endTag(VStyleCalendar.TAG_CONTROLS);
        }
        target.endTag(VStyleCalendar.TAG_HEADER);
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        // user clicked on a day
        if (variables.containsKey(VStyleCalendar.VAR_CLICKED_DAY)) {
            Integer clickedDay = (Integer) variables
                    .get(VStyleCalendar.VAR_CLICKED_DAY);
            Integer clickedDayIndex = (Integer) variables
                    .get(VStyleCalendar.VAR_DAYINDEX);

            if (!isDisabled(clickedDayIndex)) {
                Date selectedDate = constructNewDateValue(clickedDay);
                setValue(selectedDate);
            } else {
                // Ch-ch-cheater. Do nothing.
            }
        }

        if (variables.containsKey(VStyleCalendar.VAR_PREV_CLICK)) {
            if (isPrevMonthAllowed()) {
                showPreviousMonth();
            } else {
                // Ch-ch-cheater. Do nothing.
            }

        } else if (variables.containsKey(VStyleCalendar.VAR_NEXT_CLICK)) {
            if (isNextMonthAllowed()) {
                showNextMonth();
            } else {
                // Ch-ch-cheater. Do nothing.
            }
        }

    }

    /**
     * Set the style generator used. This is called on every day shown.
     * 
     * @param dateOptionsGenerator
     */
    public void setDateOptionsGenerator(
            DateOptionsGenerator dateOptionsGenerator) {
        this.dateOptionsGenerator = dateOptionsGenerator;
        requestRepaint();
    }

    /**
     * @return the dateOptionsGenerator currently used
     */
    public DateOptionsGenerator getDateOptionsGenerator() {
        return dateOptionsGenerator;
    }

    /**
     * Set if the controls (next/prev month) be rendered.
     * 
     * @param renderControls
     */
    public void setRenderControls(boolean renderControls) {
        this.renderControls = renderControls;
        requestRepaint();
    }

    /**
     * Check if the controls are currently rendered.
     * 
     * @return the renderControls
     */
    public boolean isRenderControls() {
        return renderControls;
    }

    /**
     * Set if the header (current month + controls) should be rendered.
     * 
     * @param renderHeader
     *            the renderHeader to set
     */
    public void setRenderHeader(boolean renderHeader) {
        this.renderHeader = renderHeader;
        requestRepaint();
    }

    /**
     * Check if the header is currently rendered.
     * 
     * @return the renderHeader
     */
    public boolean isRenderHeader() {
        return renderHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractComponent#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        requestRepaint();
    }

    /**
     * Set if week numbers should be rendered.
     * 
     * @param renderWeekNumbers
     */
    public void setRenderWeekNumbers(boolean renderWeekNumbers) {
        this.renderWeekNumbers = renderWeekNumbers;
        requestRepaint();
    }

    /**
     * Check if the week numbers are currently rendered.
     * 
     * @return the renderWeekNumbers
     */
    public boolean isRenderWeekNumbers() {
        return renderWeekNumbers;
    }

    /**
     * Render the previous month.
     */
    public void showPreviousMonth() {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1);

        setShowingDate(calendar.getTime());
    }

    /**
     * Render the next month.
     */
    public void showNextMonth() {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);

        setShowingDate(calendar.getTime());
    }

    /**
     * Render the previous year.
     */
    public void showPreviousYear() {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, -1);

        setShowingDate(calendar.getTime());
    }

    /**
     * Render the next year.
     */
    public void showNextYear() {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, 1);

        setShowingDate(calendar.getTime());
    }

    /**
     * Set the month to be shown.
     * 
     * @param showingDate
     */
    public void setShowingDate(Date monthToShow) {
        showingDate = monthToShow;
        requestRepaint();
    }

    /**
     * @return the month currently shown
     */
    public Date getShowingDate() {
        return showingDate;
    }

    /**
     * Set the date range that the user can select. If either date is null,
     * selection to that direction is not limited.
     * 
     * @param start
     *            start of enabled dates, inclusive
     * @param end
     *            end of enabled dates, inclusive
     */
    public void setEnabledDateRange(Date start, Date end) {
        enabledStartDate = start;
        enabledEndDate = end;
        requestRepaint();
    }

    protected Calendar getCalendarInstance() {
        return Calendar.getInstance(getLocale());
    }

    protected Calendar getResetCalendarInstance(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        resetCalendarTimeFields(calendar);
        return calendar;
    }

    protected String getMonthCaption(Date date, int amount, boolean longCaption) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);

        calendar.roll(Calendar.MONTH, amount);

        int displayLength = longCaption ? Calendar.LONG : Calendar.SHORT;

        return calendar.getDisplayName(Calendar.MONTH, displayLength,
                getLocale());
    }

    protected boolean dayEquals(Date day1, Date day2) {
        if ((day1 == null || day2 == null) && day1 != day2) {
            return false;
        }

        Calendar c1 = getCalendarInstance();
        c1.setTime(day1);

        Calendar c2 = getCalendarInstance();
        c2.setTime(day2);

        resetCalendarTimeFields(c1);

        resetCalendarTimeFields(c2);

        return c1.equals(c2);
    }

    protected boolean monthEquals(Date day1, Date day2) {
        if ((day1 == null || day2 == null) && day1 != day2) {
            return false;
        }

        Calendar c1 = getCalendarInstance();
        c1.setTime(day1);

        Calendar c2 = getCalendarInstance();
        c2.setTime(day2);

        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);

        return month1 == month2;
    }

    protected Date constructNewDateValue(int newDay) {
        Date showingDate = getShowingDate();
        Calendar calendar = getCalendarInstance();
        calendar.setTime(showingDate);

        calendar.set(Calendar.DAY_OF_MONTH, newDay);

        return calendar.getTime();
    }

    protected Calendar constructNewCalendarValue(int newDay) {
        Date showingDate = getShowingDate();
        Calendar calendar = getCalendarInstance();
        calendar.setTime(showingDate);

        calendar.set(Calendar.DAY_OF_MONTH, newDay);

        return calendar;
    }

    protected boolean isWeekend(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
    }

    /**
     * @param clickedDay
     *            the day of month that was clicked
     * @return true if the day is disabled, false otherwise
     */
    protected boolean isDisabled(int clickedDayIndex) {
        return disabledRenderedDays.contains(clickedDayIndex);
    }

    /**
     * @return true if it is allowed to advance the calendar to the next month
     */
    protected boolean isNextMonthAllowed() {
        return isRenderControls() && isRenderHeader() && nextMonthEnabled;
    }

    /**
     * @return true if it is allowed to go back to the next month
     */
    protected boolean isPrevMonthAllowed() {
        return isRenderControls() && isRenderHeader() && prevMonthEnabled;
    }

    /**
     * @param time
     * @param i
     * @return
     */
    protected boolean isDisabledMonth(Date date, int amount) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, amount);
        Date month = calendar.getTime();

        if (enabledStartDate != null) {
            if (month.before(enabledStartDate)
                    && !monthEquals(month, enabledStartDate)) {
                return true;
            }
        }

        if (enabledEndDate != null) {
            if (month.after(enabledEndDate)
                    && !monthEquals(month, enabledEndDate)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param time
     * @return
     */
    protected boolean isDisabledDate(Date date) {
        if (getDateOptionsGenerator() != null
                && getDateOptionsGenerator().isDateDisabled(date, this)) {
            return true;

        } else {
            boolean isAfterStart = true;
            boolean isBeforeEnd = true;

            Calendar today = getResetCalendarInstance(date);

            if (enabledStartDate != null) {
                Calendar enabledStart = getResetCalendarInstance(enabledStartDate);
                isAfterStart = today.after(enabledStart)
                        || dayEquals(date, enabledStartDate);
            }

            if (enabledEndDate != null) {
                Calendar enabledEnd = getResetCalendarInstance(enabledEndDate);
                isBeforeEnd = today.before(enabledEnd)
                        || dayEquals(date, enabledEndDate);
            }

            // if the date is between the enabled range
            return !(isAfterStart && isBeforeEnd);
        }
    }

    protected void resetCalendarTimeFields(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.AM_PM, 0);
    }

    protected Set<Date> getFirsDaysOfWeeks() {
        LinkedHashSet<Date> set = new LinkedHashSet<Date>();
        Calendar calendar = getCalendarInstance();

        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.getTime();// force recalc to overcome some strange behavior of
                           // the java calendar

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        for (int i = 1; i <= daysInMonth; i++) {
            Calendar weekStart = (Calendar) calendar.clone();

            weekStart.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
            set.add(weekStart.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return set;
    }
}
