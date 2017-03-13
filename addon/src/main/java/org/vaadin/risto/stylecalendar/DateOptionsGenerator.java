package org.vaadin.risto.stylecalendar;

import java.util.Date;

/**
 * Interface for for setting options for dates with the StyleCalendar. The
 * methods are called once for every date rendered.
 *
 * @author Risto Yrjänä / Vaadin
 */
public interface DateOptionsGenerator {

    /**
     * @param date
     *         currently rendered date
     * @param context
     *         the calling StyleCalendar instance
     * @return the desired style name, or null
     */
    String getStyleName(Date date, StyleCalendar context);

    /**
     * @param date
     *         currently rendered date
     * @param context
     *         the calling StyleCalendar instance
     * @return the tooltip for a given day, or null
     */
    String getTooltip(Date date, StyleCalendar context);

    /**
     * @param date
     *         currently rendered date
     * @param context
     *         the calling StyleCalendar instance
     * @return true if selecting this date should be disabled, false otherwise
     */
    boolean isDateDisabled(Date date, StyleCalendar context);
}
