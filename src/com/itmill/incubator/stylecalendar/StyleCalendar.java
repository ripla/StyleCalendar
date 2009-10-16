package com.itmill.incubator.stylecalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.itmill.incubator.stylecalendar.client.ui.VStyleCalendar;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractField;

/**
 * A stylable calendar component. A DateStyleGenerator can be used to generate
 * stylenames externally.
 * 
 * @author Risto Yrjänä / IT Mill Ltd.
 * 
 */
public class StyleCalendar extends AbstractField {

    private static final long serialVersionUID = 7797206568110243067L;

    private DateStyleGenerator dateStyleGenerator;

    private boolean renderControls;

    private boolean renderHeader;

    private boolean renderWeekNumbers;

    private Date showingDate = null;

    public StyleCalendar() {
        super();
        setRenderHeader(true);
        setRenderControls(true);
        setRenderWeekNumbers(true);

        setShowingMonth(new Date());
    }

    @Override
    public String getTag() {
        return VStyleCalendar.TAGNAME;
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

        Calendar calendar = getCalendarInstance();

        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int daysInWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();

        // set main tag attributes
        target.addAttribute("renderWeekNumbers", isRenderWeekNumbers());
        target.addAttribute("renderHeader", isRenderHeader());
        target.addAttribute("renderControls", isRenderControls());

        // render header
        if (isRenderHeader()) {
            target.startTag("header");
            target.addAttribute("currentYear", calendar.get(Calendar.YEAR));
            target.addAttribute("currentMonth", getMonthCaption(calendar
                    .getTime(), 0, true));

            // render controls
            if (isRenderControls()) {
                target.startTag("controls");
                target.addAttribute("prevMonth", getMonthCaption(calendar
                        .getTime(), -1, false));
                target.addAttribute("nextMonth", getMonthCaption(calendar
                        .getTime(), 1, false));
                target.endTag("controls");
            }
            target.endTag("header");
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

        target.addVariable(this, "weekDayNames", weekDaysArray);

        // so we get the right amount of weeks to render
        calendar.setMinimalDaysInFirstWeek(1);

        // render weeks and days

        int numberOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        for (int week = 1; week < numberOfWeeks + 1; week++) {
            calendar.setTime(showingDate);
            calendar.set(Calendar.WEEK_OF_MONTH, week);
            target.startTag("week");

            target.addAttribute("number", calendar.get(Calendar.WEEK_OF_YEAR));

            // reset to the start of the week
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

            for (int day = 0; day < daysInWeek; day++) {
                target.startTag("day");
                target.addAttribute("daynumber", calendar
                        .get(Calendar.DAY_OF_MONTH));

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
                    target.addAttribute("clickable", true);
                } else {
                    dayStyle.append(" ");
                    dayStyle.append("othermonth");
                    target.addAttribute("clickable", false);
                }

                if (isWeekend(calendar.getTime())) {
                    dayStyle.append(" ");
                    dayStyle.append("weekend");
                }

                if (getDateStyleGenerator() != null) {
                    String generatedStyle = getDateStyleGenerator()
                            .getStyleName(calendar.getTime(), this);
                    if (generatedStyle != null) {
                        dayStyle.append(" ");
                        dayStyle.append(generatedStyle);
                    }
                }

                String dayStyleString = dayStyle.toString();
                if (!dayStyleString.isEmpty()) {
                    target.addAttribute("style", dayStyleString);
                }

                target.endTag("day");

                // move to the next day
                calendar.add(Calendar.DAY_OF_WEEK, 1);
            }
            target.endTag("week");
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);

        // user clicked on a day
        if (variables.containsKey("clickedDay")) {
            Date selectedDate = constructNewValue((Integer) variables
                    .get("clickedDay"));

            setValue(selectedDate);
        } else if (variables.containsKey("prevClick")) {
            showPreviousMonth();
        } else if (variables.containsKey("nextClick")) {
            showNextMonth();
        }

    }

    /**
     * Set the style generator used. Ths is called on every day shown.
     * 
     * @param dateStyleGenerator
     */
    public void setDateStyleGenerator(DateStyleGenerator dateStyleGenerator) {
        this.dateStyleGenerator = dateStyleGenerator;
        requestRepaint();
    }

    /**
     * @return the dateStyleGenerator currently used
     */
    public DateStyleGenerator getDateStyleGenerator() {
        return dateStyleGenerator;
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
     * Render the next month.
     */
    public void showPreviousMonth() {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1);

        setShowingMonth(calendar.getTime());
    }

    /**
     * Render the previous month.
     */
    public void showNextMonth() {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(getShowingDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);

        setShowingMonth(calendar.getTime());
    }

    /**
     * Set the month to be shown.
     * 
     * @param showingDate
     */
    public void setShowingMonth(Date monthToShow) {
        showingDate = monthToShow;
        requestRepaint();
    }

    /**
     * @return the month currently shown
     */
    public Date getShowingDate() {
        return showingDate;
    }

    private Calendar getCalendarInstance() {
        return Calendar.getInstance(getLocale());
    }

    private String getMonthCaption(Date date, int amount, boolean longCaption) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);

        calendar.roll(Calendar.MONTH, amount);

        int displayLength = longCaption ? Calendar.LONG : Calendar.SHORT;

        return calendar.getDisplayName(Calendar.MONTH, displayLength,
                getLocale());
    }

    private boolean dayEquals(Date day1, Date day2) {
        if ((day1 == null || day2 == null) && day1 != day2) {
            return false;
        }

        Calendar c1 = getCalendarInstance();
        c1.setTime(day1);

        Calendar c2 = getCalendarInstance();
        c2.setTime(day2);

        c1.set(Calendar.MILLISECOND, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.HOUR, 0);

        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.HOUR, 0);

        return c1.equals(c2);
    }

    private boolean monthEquals(Date day1, Date day2) {
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

    private Date constructNewValue(int newDay) {
        Date showingDate = getShowingDate();
        Calendar calendar = getCalendarInstance();
        calendar.setTime(showingDate);

        calendar.set(Calendar.DAY_OF_MONTH, newDay);

        return calendar.getTime();
    }

    private boolean isWeekend(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
    }

    /**
     * Interface for for styling dates with the StyleCalendar.
     * 
     * @author Risto Yrjänä / IT Mill Ltd.
     * 
     */
    public interface DateStyleGenerator {

        /**
         * This method is called on every date of the currently shown month.
         * 
         * @param date
         *            currently rendered date
         * @param context
         *            the calling StyleCalendar instance
         * @return the desired stylename, or null
         */
        public String getStyleName(Date date, StyleCalendar context);
    }
}
