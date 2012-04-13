package org.vaadin.risto.stylecalendar.widgetset.client;

import org.vaadin.risto.stylecalendar.StyleCalendar;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.VStyleCalendar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.communication.StateChangeEvent;
import com.vaadin.terminal.gwt.client.ui.AbstractFieldConnector;
import com.vaadin.terminal.gwt.client.ui.Connect;

@Connect(StyleCalendar.class)
public class StyleCalendarConnector extends AbstractFieldConnector {

    private static final long serialVersionUID = -5612890528981319173L;

    @Override
    protected Widget createWidget() {
        return GWT.create(VStyleCalendar.class);
    }

    @Override
    public StyleCalendarState getState() {
        return (StyleCalendarState) super.getState();
    }

    @Override
    public VStyleCalendar getWidget() {
        return (VStyleCalendar) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {

        // set global rendering attributes from base tag
        getWidget().setRenderWeekNumbers(getState().isRenderWeekNumbers());
        getWidget().setRenderHeader(getState().isRenderHeader());
        getWidget().setRenderControls(getState().isRenderControls());

        getWidget().setCurrentYear(getState().getCurrentYear());
        getWidget().setCurrentMonth(getState().getCurrentMonth());
        getWidget().setWeekDayNames(getState().getWeekDayNames());

        // controls prev/next
        getWidget().setPreviousMonthControl(
                getState().getPreviousMonthControl());
        getWidget().setNextMonthControl(getState().getNextMonthControl());

        getWidget().setWeeks(getState().getWeeks());

        getWidget().redraw();

        super.onStateChanged(stateChangeEvent);
    }
}
