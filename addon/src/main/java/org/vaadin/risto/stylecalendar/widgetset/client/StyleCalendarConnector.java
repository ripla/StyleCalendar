package org.vaadin.risto.stylecalendar.widgetset.client;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.risto.stylecalendar.StyleCalendar;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.DayLabel;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.VStyleCalendar;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.DayClickEvent;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.DayClickHandler;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.MonthClickEvent;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.MonthClickHandler;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.PrevMonthClickEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.TooltipInfo;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(StyleCalendar.class)
public class StyleCalendarConnector extends AbstractFieldConnector {

    private static final long serialVersionUID = -5612890528981319173L;
    private List<HandlerRegistration> handlerRegistrations;

    @Override
    public void onUnregister() {
        for (HandlerRegistration hr : handlerRegistrations) {
            hr.removeHandler();
        }
        super.onUnregister();
    }

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

        // set global rendering attributes from base state
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

        for (DayLabel widget : getWidget().getDayWidgets()) {
            getConnection().getVTooltip().connectHandlersToWidget(widget);
        }
        super.onStateChanged(stateChangeEvent);
    }

    @Override
    protected void init() {
        super.init();

        handlerRegistrations = new ArrayList<HandlerRegistration>();

        final StyleCalendarRpc rpcProxy = RpcProxy.create(
                StyleCalendarRpc.class, this);

        handlerRegistrations.add(getWidget().addMonthClickHandler(
                new MonthClickHandler() {

                    @Override
                    public void onMonthClick(MonthClickEvent event) {

                        if (event instanceof PrevMonthClickEvent) {
                            rpcProxy.previousMonthClicked();
                        } else {
                            rpcProxy.nextMonthClicked();
                        }
                    }

                }));

        handlerRegistrations.add(getWidget().addDayClickHandler(
                new DayClickHandler() {

                    @Override
                    public void onDayClick(DayClickEvent event) {
                        rpcProxy.dayClicked(event.getClickedDay(),
                                event.getClickedDayIndex());
                    }

                }));
    }

    @Override
    public TooltipInfo getTooltipInfo(Element element) {
        DayLabel label = getWidget().getDayLabel(element);
        if (label != null) {
            return new TooltipInfo(label.getTooltip());
        } else {
            return super.getTooltipInfo(element);
        }
    }
}
