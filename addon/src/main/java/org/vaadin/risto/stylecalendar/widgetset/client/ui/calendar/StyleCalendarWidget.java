package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.data.StyleCalendarControl;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.data.StyleCalendarDay;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.data.StyleCalendarWeek;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.DayClickEvent;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.DayClickHandler;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.MonthClickEvent;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.MonthClickHandler;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.NextMonthClickEvent;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar.event.PrevMonthClickEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class StyleCalendarWidget extends SimplePanel {

    public static final String CLASSNAME = "stylecalendar";

    private static final String PREVMONTHCONTROL = "<<";
    private static final String NEXTMONTHCONTROL = ">>";

    private boolean renderWeekNumbers;

    private boolean renderControls;

    private boolean renderHeader;

    /**
     * List of click handlers. Handlers are removed on each redraw.
     */
    protected final List<HandlerRegistration> handlerRegistrations;

    private String currentYear;

    private String currentMonth;

    private StyleCalendarControl previousMonthControl;

    private StyleCalendarControl nextMonthControl;

    private List<String> weekDayNames;

    private List<StyleCalendarWeek> weeks;

    private final HashMap<Element, DayLabel> dayElements;

    public StyleCalendarWidget() {
        setStyleName(CLASSNAME);

        handlerRegistrations = new LinkedList<HandlerRegistration>();

        dayElements = new HashMap<Element, DayLabel>();
    }

    public void redraw() {
        if (!handlerRegistrations.isEmpty()) {
            for (HandlerRegistration hr : handlerRegistrations) {
                hr.removeHandler();
            }
            handlerRegistrations.clear();
        }

        dayElements.clear();

        FlexTable calendarBody = new FlexTable();
        calendarBody.setCellSpacing(0);
        calendarBody.setCellPadding(0);

        setWidget(calendarBody);

        int weekdaysRow = renderHeader ? 1 : 0;
        if (renderHeader) {
            renderHeader(calendarBody);
        }

        renderWeekDays(weekDayNames, calendarBody, weekdaysRow);
        int weekRow = weekdaysRow + 1;
        for (StyleCalendarWeek week : getWeeks()) {
            renderWeek(week, calendarBody, weekRow++);
        }

        if (renderHeader) {
            int numberOfColumns = renderWeekNumbers ? 8 : 7;
            calendarBody.getFlexCellFormatter().setColSpan(0, 0,
                    numberOfColumns);
        }
    }

    /**
     * Send day clicked message to server.
     * 
     * @param day
     * @param dayIndex
     */
    public void dayClick(int day, int dayIndex) {
        fireEvent(new DayClickEvent(day, dayIndex));
    }

    /**
     * Send previous clicked message to server.
     */
    public void prevClick() {
        fireEvent(new PrevMonthClickEvent());
    }

    /**
     * Send next clicked message to server.
     */
    public void nextClick() {
        fireEvent(new NextMonthClickEvent());
    }

    /**
     * @param weekDayNames
     */
    protected void renderWeekDays(List<String> weekDayNames, FlexTable cb,
            int weekDayRow) {
        cb.getRowFormatter().setStylePrimaryName(weekDayRow, "weekdays");

        // add one empty cell for the week number
        if (renderWeekNumbers) {
            cb.addCell(weekDayRow);
        }

        for (String weekDay : weekDayNames) {
            cb.addCell(weekDayRow);
            cb.setText(weekDayRow, cb.getCellCount(weekDayRow) - 1, weekDay);
            cb.getCellFormatter().setAlignment(weekDayRow,
                    cb.getCellCount(weekDayRow) - 1,
                    HasHorizontalAlignment.ALIGN_CENTER,
                    HasVerticalAlignment.ALIGN_MIDDLE);
        }
    }

    /**
     * Render the header information from the current state to the given table.
     * 
     * @param childUIDL
     * @param headerRow
     * @param cb
     */
    protected void renderHeader(FlexTable cb) {

        HorizontalPanel headerPanel = new HorizontalPanel();

        cb.getRowFormatter().setStyleName(0, "header");

        if (renderControls) {
            Widget leftControl = renderPrevControl();
            headerPanel
                    .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
            headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            headerPanel.add(leftControl);
        }

        // month and year, either side-by-side or on top of each other
        Label month = null;
        Label year = null;
        if (renderControls) {
            month = new Label(getCurrentMonth());
            year = new Label(getCurrentYear());
        } else {
            month = new InlineLabel(getCurrentMonth());
            year = new InlineLabel(getCurrentYear());
        }
        month.setStylePrimaryName("currentmonth");
        year.setStylePrimaryName("year");

        headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

        FlowPanel middle = new FlowPanel();
        middle.add(month);
        middle.add(year);

        headerPanel.add(middle);

        if (renderControls) {
            Widget rightControl = renderNextControl();
            headerPanel
                    .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            headerPanel.add(rightControl);
        }

        cb.setWidget(0, 0, headerPanel);
    }

    protected Widget renderPrevControl() {

        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStylePrimaryName("prevcontrol");

        InlineLabel controlSymbol = new InlineLabel(getPrevMonthControlString());
        controlSymbol.setStylePrimaryName("control");

        InlineLabel controlCaption = new InlineLabel(
                previousMonthControl.getText());
        controlCaption.setStylePrimaryName("month");

        controlPanel.add(controlSymbol);
        controlPanel.add(controlCaption);

        if (previousMonthControl.isEnabled()) {
            ClickHandler prevClick = new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    prevClick();
                }
            };

            HandlerRegistration symbolClick = controlSymbol
                    .addClickHandler(prevClick);
            HandlerRegistration captionClick = controlCaption
                    .addClickHandler(prevClick);

            handlerRegistrations.add(symbolClick);
            handlerRegistrations.add(captionClick);

        } else {
            controlPanel.addStyleDependentName("disabled");
        }

        return controlPanel;
    }

    protected String getPrevMonthControlString() {
        return PREVMONTHCONTROL;
    }

    protected Widget renderNextControl() {
        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStylePrimaryName("nextcontrol");

        InlineLabel controlSymbol = new InlineLabel(
                getNexthMonthControlString());
        controlSymbol.setStylePrimaryName("control");

        InlineLabel controlCaption = new InlineLabel(getNextMonthControl()
                .getText());
        controlCaption.setStylePrimaryName("month");

        controlPanel.add(controlCaption);
        controlPanel.add(controlSymbol);

        if (getNextMonthControl().isEnabled()) {
            ClickHandler nextClick = new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    nextClick();
                }
            };

            HandlerRegistration symbolClick = controlSymbol
                    .addClickHandler(nextClick);
            HandlerRegistration captionClick = controlCaption
                    .addClickHandler(nextClick);

            handlerRegistrations.add(symbolClick);
            handlerRegistrations.add(captionClick);

        } else {
            controlPanel.addStyleDependentName("disabled");
        }

        return controlPanel;
    }

    protected String getNexthMonthControlString() {
        return NEXTMONTHCONTROL;
    }

    protected void renderWeek(StyleCalendarWeek week, FlexTable cb, int weekRow) {

        cb.getRowFormatter().setStylePrimaryName(weekRow, "week");

        if (renderWeekNumbers) {
            cb.addCell(weekRow);

            Label weekNumber = new Label(week.getWeekNumber());
            cb.setWidget(weekRow, cb.getCellCount(weekRow) - 1, weekNumber);

            cb.getCellFormatter().setStyleName(weekRow, 0, "weeknumber");

        }

        int columnAddition = (renderWeekNumbers ? 1 : 0);

        for (int i = 0; i < week.getDays().size(); i++) {
            cb.addCell(weekRow);
            renderDay(week.getDays().get(i), cb, weekRow, i + columnAddition);
        }

    }

    protected void renderDay(StyleCalendarDay day, FlexTable cb, int dayRow,
            int dayColumn) {

        Integer dayNumber = day.getNumber();

        Integer dayIndex = day.getIndex();

        String dayStyle = null;
        if (!isNullOrEmpty(day.getStyle())) {
            StringBuilder styleBuilder = new StringBuilder();
            styleBuilder.append("day");
            styleBuilder.append(" ");
            styleBuilder.append(day.getStyle());
            dayStyle = styleBuilder.toString();
        } else {
            dayStyle = "day";
        }

        DayLabel dayLabel = new DayLabel(day.getTooltip());
        dayLabel.setText(Integer.toString(dayNumber));

        if (day.isClickable() && !day.isDisabled()) {
            HandlerRegistration dayClickHandler = dayLabel
                    .addClickHandler(new InternalDayClickHandler(dayNumber,
                            dayIndex));
            handlerRegistrations.add(dayClickHandler);
        } else if (day.isDisabled()) {
            dayStyle += " disabled";
        }

        dayElements.put(dayLabel.getElement(), dayLabel);

        cb.setWidget(dayRow, dayColumn, dayLabel);

        cb.getCellFormatter().setStyleName(dayRow, dayColumn, dayStyle);
        cb.getCellFormatter().setAlignment(dayRow, dayColumn,
                HasHorizontalAlignment.ALIGN_CENTER,
                HasVerticalAlignment.ALIGN_MIDDLE);
    }

    protected boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    protected class InternalDayClickHandler implements ClickHandler {

        protected final int day;
        protected final int dayIndex;

        public InternalDayClickHandler(int day, int dayIndex) {
            this.day = day;
            this.dayIndex = dayIndex;
        }

        @Override
        public void onClick(ClickEvent event) {
            dayClick(day, dayIndex);
        }

    }

    public void handleTooltipEvent(Event event, DayLabel dayLabel) {
        // TODO
        // client.handleTooltipEvent(event, this, dayLabel.getIndex());
    }

    public void setRenderWeekNumbers(boolean renderWeekNumbers) {
        this.renderWeekNumbers = renderWeekNumbers;
    }

    public void setRenderHeader(boolean renderHeader) {
        this.renderHeader = renderHeader;
    }

    public void setRenderControls(boolean renderControls) {
        this.renderControls = renderControls;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public StyleCalendarControl getPreviousMonthControl() {
        return previousMonthControl;
    }

    public void setPreviousMonthControl(
            StyleCalendarControl previousMonthControl) {
        this.previousMonthControl = previousMonthControl;
    }

    public void setNextMonthControl(StyleCalendarControl nextMonthControl) {
        this.nextMonthControl = nextMonthControl;
    }

    public StyleCalendarControl getNextMonthControl() {
        return nextMonthControl;
    }

    public void setWeekDayNames(List<String> weekDayNames) {
        this.weekDayNames = weekDayNames;
    }

    public List<String> getWeekDayNames() {
        return weekDayNames;
    }

    public void setWeeks(List<StyleCalendarWeek> weeks) {
        this.weeks = weeks;
    }

    public List<StyleCalendarWeek> getWeeks() {
        return weeks;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        getWidget().setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        getWidget().setHeight(height);
    }

    public HandlerRegistration addMonthClickHandler(
            MonthClickHandler monthClickHandler) {
        return addHandler(monthClickHandler, MonthClickEvent.getType());
    }

    public HandlerRegistration addDayClickHandler(
            DayClickHandler dayClickHandler) {
        return addHandler(dayClickHandler, DayClickEvent.getType());
    }

    public Collection<DayLabel> getDayWidgets() {
        return dayElements.values();
    }

    public DayLabel getDayLabel(com.google.gwt.dom.client.Element element) {
        return dayElements.get(element);
    }
}
