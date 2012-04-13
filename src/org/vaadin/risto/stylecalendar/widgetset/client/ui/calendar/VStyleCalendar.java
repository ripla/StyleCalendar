package org.vaadin.risto.stylecalendar.widgetset.client.ui.calendar;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
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

public class VStyleCalendar extends SimplePanel {

    public static final String TAG_HEADER = "header";

    public static final String TAG_CONTROLS = "controls";

    public static final String TAG_WEEK = "week";

    public static final String TAG_DAY = "day";

    public static final String ATTR_RENDER_WEEK_NUMBERS = "renderWeekNumbers";

    public static final String ATTR_RENDER_HEADER = "renderHeader";

    public static final String ATTR_RENDER_CONTROLS = "renderControls";

    public static final String ATTR_DAY_CLICKABLE = "clickable";

    public static final String ATTR_DAY_DISABLED = "disabled";

    public static final String ATTR_DAY_NUMBER = "daynumber";

    public static final String ATTR_DAY_STYLE = "style";

    public static final String ATTR_DAY_INDEX = "dayIndex";

    public static final String ATTR_DAY_TOOLTIP = "dayTooltip";

    public static final String ATTR_CONTROLS_PREV_MONTH = "prevMonth";

    public static final String ATTR_CONTROLS_PREV_MONTH_DISABLED = "prevMonthDisabled";

    public static final String ATTR_CONTROLS_NEXT_MONTH = "nextMonth";

    public static final String ATTR_CONTROLS_NEXT_MONTH_DISABLED = "nextMonthDisabled";

    public static final String ATTR_WEEK_DAY_NAMES = "weekDayNames";

    public static final String ATTR_WEEK_NUMBER = "number";

    public static final String ATTR_HEADER_CURRENT_YEAR = "currentYear";

    public static final String ATTR_HEADER_CURRENT_MONTH = "currentMonth";

    public static final String VAR_PREV_CLICK = "prevClick";

    public static final String VAR_CLICKED_DAY = "clickedDay";

    public static final String VAR_DAYINDEX = "dayIndex";

    public static final String VAR_NEXT_CLICK = "nextClick";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-stylecalendar";

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

    private VStyleCalendarControl previousMonthControl;

    private VStyleCalendarControl nextMonthControl;

    private List<String> weekDayNames;

    private List<VStyleCalendarWeek> weeks;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VStyleCalendar() {
        super();

        setStyleName(CLASSNAME);

        handlerRegistrations = new ArrayList<HandlerRegistration>();
    }

    public void redraw() {
        if (!handlerRegistrations.isEmpty()) {
            for (HandlerRegistration hr : handlerRegistrations) {
                hr.removeHandler();
            }
            handlerRegistrations.clear();
        }

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
        for (VStyleCalendarWeek week : getWeeks()) {
            renderWeek(week, calendarBody, weekRow++);
        }

        if (renderHeader) {
            int numberOfColumns = renderWeekNumbers ? 8 : 7;
            calendarBody.getFlexCellFormatter().setColSpan(0, 0,
                    numberOfColumns);
        }
    }

    /**
     * Send day clicked message to server. Immediateness depends on server-side.
     * 
     * @param day
     * @param dayIndex
     */
    public void dayClick(int day, int dayIndex) {
        // TODO
        // client.updateVariable(uidlId, VAR_CLICKED_DAY, day, false);
        // client.updateVariable(uidlId, VAR_DAYINDEX, dayIndex, immediate);
    }

    /**
     * Send previous clicked message to server. Control messages are always
     * immediate.
     */
    public void prevClick() {
        // TODO
        // client.updateVariable(uidlId, VAR_PREV_CLICK, true, true);
    }

    /**
     * Send next clicked message to server. Control messages are always
     * immediate.
     */
    public void nextClick() {
        // TODO
        // client.updateVariable(uidlId, VAR_NEXT_CLICK, true, true);
    }

    /**
     * @param weekDayNames
     */
    private void renderWeekDays(List<String> weekDayNames, FlexTable cb,
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
     * Render the header information from the given UIDL to the given row in the
     * given table.
     * 
     * @param childUIDL
     * @param headerRow
     * @param cb
     */
    private void renderHeader(FlexTable cb) {

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

    private Widget renderPrevControl() {

        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStylePrimaryName("prevcontrol");

        InlineLabel controlSymbol = new InlineLabel(PREVMONTHCONTROL);
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

    private Widget renderNextControl() {
        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStylePrimaryName("nextcontrol");

        InlineLabel controlSymbol = new InlineLabel(NEXTMONTHCONTROL);
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

    private void renderWeek(VStyleCalendarWeek week, FlexTable cb, int weekRow) {

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

    private void renderDay(VStyleCalendarDay day, FlexTable cb, int dayRow,
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

        Label dayLabel = new DayLabel(dayIndex, this);
        dayLabel.setText(Integer.toString(dayNumber));

        if (day.isClickable() && !day.isDisabled()) {
            HandlerRegistration dayClickHandler = dayLabel
                    .addClickHandler(new DayClickHandler(dayNumber, dayIndex));
            handlerRegistrations.add(dayClickHandler);
        } else if (day.isDisabled()) {
            dayStyle += " disabled";
        }

        if (!isNullOrEmpty(day.getTooltip())) {
            // TODO
            // TooltipInfo dayTooltip = new TooltipInfo(day.getTooltip());
            // client.registerTooltip(this, dayIndex, dayTooltip);
        }

        cb.setWidget(dayRow, dayColumn, dayLabel);

        cb.getCellFormatter().setStyleName(dayRow, dayColumn, dayStyle);
        cb.getCellFormatter().setAlignment(dayRow, dayColumn,
                HasHorizontalAlignment.ALIGN_CENTER,
                HasVerticalAlignment.ALIGN_MIDDLE);
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private class DayClickHandler implements ClickHandler {

        private final int day;
        private final int dayIndex;

        public DayClickHandler(int day, int dayIndex) {
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

    public VStyleCalendarControl getPreviousMonthControl() {
        return previousMonthControl;
    }

    public void setPreviousMonthControl(
            VStyleCalendarControl previousMonthControl) {
        this.previousMonthControl = previousMonthControl;
    }

    public void setNextMonthControl(VStyleCalendarControl nextMonthControl) {
        this.nextMonthControl = nextMonthControl;
    }

    public VStyleCalendarControl getNextMonthControl() {
        return nextMonthControl;
    }

    public void setWeekDayNames(List<String> weekDayNames) {
        this.weekDayNames = weekDayNames;
    }

    public List<String> getWeekDayNames() {
        return weekDayNames;
    }

    public void setWeeks(List<VStyleCalendarWeek> weeks) {
        this.weeks = weeks;
    }

    public List<VStyleCalendarWeek> getWeeks() {
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
}
