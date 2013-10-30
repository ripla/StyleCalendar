package org.vaadin.risto.stylecalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class StyleCalendarTest {

    private StyleCalendar styleCalendar;

    @Before
    public void init() {
        styleCalendar = new StyleCalendar();
        styleCalendar.setLocale(new Locale("fi", "FI"));
    }

    @Test
    public void weekStartDaysCalculatedCorrectly() {
        Calendar calendar = styleCalendar.getCalendarInstance();
        setDate(calendar, 2012, Calendar.JANUARY, 1);

        styleCalendar.setShowingDate(calendar.getTime());

        List<Date> expected = new LinkedList<Date>();
        setDate(calendar, 2011, Calendar.DECEMBER, 26);
        expected.add(calendar.getTime());
        setDate(calendar, 2012, Calendar.JANUARY, 2);
        expected.add(calendar.getTime());
        setDate(calendar, 2012, Calendar.JANUARY, 9);
        expected.add(calendar.getTime());
        setDate(calendar, 2012, Calendar.JANUARY, 16);
        expected.add(calendar.getTime());
        setDate(calendar, 2012, Calendar.JANUARY, 23);
        expected.add(calendar.getTime());
        setDate(calendar, 2012, Calendar.JANUARY, 30);
        expected.add(calendar.getTime());

        Set<Date> result = styleCalendar.getFirsDaysOfWeeks();

        assertEquals(6, result.size());

        int i = 0;
        for (Date resultDate : result) {
            Date expectedDate = expected.get(i++);
            assertTrue("Expected date " + expectedDate + "didn't match"
                    + resultDate,
                    styleCalendar.dayEquals(resultDate, expectedDate));
        }
    }

    private void setDate(Calendar calendar, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }
}
