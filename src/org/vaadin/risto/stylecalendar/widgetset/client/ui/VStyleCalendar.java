package org.vaadin.risto.stylecalendar.widgetset.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VStyleCalendar extends SimplePanel implements Paintable {

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

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-stylecalendar";

    private static final String PREVMONTHCONTROL = "<<";
    private static final String NEXTMONTHCONTROL = ">>";

    /** Component identifier in UIDL communications. */
    protected String uidlId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    protected boolean renderWeekNumbers;

    protected boolean renderControls;

    protected boolean renderHeader;

    /**
     * List of click handlers. Handlers are removed on each repaint.
     */
    protected final List<HandlerRegistration> handlerRegistrations;

    protected boolean immediate;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VStyleCalendar() {
        super();

        setStyleName(CLASSNAME);

        handlerRegistrations = new ArrayList<HandlerRegistration>();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first. Ensure correct implementation,
        // and let the containing layout manage caption, etc.
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the UIDL identifier for the component
        uidlId = uidl.getId();

        // Get immediate status
        immediate = uidl.getBooleanAttribute("immediate");

        // set global rendering attributes from base tag
        renderWeekNumbers = uidl.getBooleanAttribute(ATTR_RENDER_WEEK_NUMBERS);
        renderHeader = uidl.getBooleanAttribute(ATTR_RENDER_HEADER);
        renderControls = uidl.getBooleanAttribute(ATTR_RENDER_CONTROLS);

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

        int numberOfRows = uidl.getChildCount();
        int currentRow = 0;
        int firstWeekChild = (renderHeader ? 1 : 0);

        if (renderHeader) {
            UIDL headerUIDL = uidl.getChildUIDL(currentRow);
            renderHeader(headerUIDL, calendarBody, currentRow);
            currentRow++;
        }

        String[] weekDayNames = uidl
                .getStringArrayVariable(ATTR_WEEK_DAY_NAMES);

        renderWeekDays(weekDayNames, calendarBody, currentRow);
        currentRow++;

        for (int i = firstWeekChild; i < numberOfRows; i++) {
            UIDL childUIDL = uidl.getChildUIDL(i);

            renderWeek(childUIDL, calendarBody, currentRow++);
        }

        if (renderHeader) {
            int numCols = calendarBody.getCellCount(currentRow - 1);
            calendarBody.getFlexCellFormatter().setColSpan(0, 0, numCols);

        }
    }

    /**
     * Send day clicked message to server. Immediateness depends on server-side.
     * 
     * @param day
     */
    public void dayClick(int day) {
        client.updateVariable(uidlId, VAR_CLICKED_DAY, day, immediate);
    }

    /**
     * Send previous clicked message to server. Control messages are always
     * immediate.
     */
    public void prevClick() {
        client.updateVariable(uidlId, VAR_PREV_CLICK, true, true);
    }

    /**
     * Send next clicked message to server. Control messages are always
     * immediate.
     */
    public void nextClick() {
        client.updateVariable(uidlId, "nextClick", true, true);
    }

    /**
     * @param weekDayNames
     */
    private void renderWeekDays(String[] weekDayNames, FlexTable cb,
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
    private void renderHeader(UIDL childUIDL, FlexTable cb, int headerRow) {

        HorizontalPanel headerPanel = new HorizontalPanel();

        cb.getRowFormatter().setStyleName(headerRow, "header");
        String currentMonth = childUIDL
                .getStringAttribute(ATTR_HEADER_CURRENT_MONTH);
        int currentYear = childUIDL.getIntAttribute(ATTR_HEADER_CURRENT_YEAR);

        if (renderControls) {
            Widget leftControl = renderPrevControl(childUIDL.getChildUIDL(0));
            headerPanel
                    .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
            headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            headerPanel.add(leftControl);
        }

        // month and year, either side-by-side or on top of each other
        Label month = null;
        Label year = null;
        if (renderControls) {
            month = new Label(currentMonth);
            year = new Label(Integer.toString(currentYear));
        } else {
            month = new InlineLabel(currentMonth);
            year = new InlineLabel(Integer.toString(currentYear));
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
            Widget rightControl = renderNextControl(childUIDL.getChildUIDL(0));
            headerPanel
                    .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            headerPanel.add(rightControl);
        }

        cb.setWidget(headerRow, 0, headerPanel);
    }

    private Widget renderPrevControl(UIDL childUIDL) {

        String controlText = childUIDL
                .getStringAttribute(ATTR_CONTROLS_PREV_MONTH);
        boolean controlEnabled = !childUIDL
                .hasAttribute(ATTR_CONTROLS_PREV_MONTH_DISABLED)
                && !childUIDL
                        .getBooleanAttribute(ATTR_CONTROLS_PREV_MONTH_DISABLED);

        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStylePrimaryName("prevcontrol");

        InlineLabel controlSymbol = new InlineLabel(PREVMONTHCONTROL);
        controlSymbol.setStylePrimaryName("control");

        InlineLabel controlCaption = new InlineLabel(controlText);
        controlCaption.setStylePrimaryName("month");

        controlPanel.add(controlSymbol);
        controlPanel.add(controlCaption);

        if (controlEnabled) {
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

    private Widget renderNextControl(UIDL childUIDL) {
        String controlText = childUIDL
                .getStringAttribute(ATTR_CONTROLS_NEXT_MONTH);
        boolean controlEnabled = !childUIDL
                .hasAttribute(ATTR_CONTROLS_NEXT_MONTH_DISABLED)
                && !childUIDL
                        .getBooleanAttribute(ATTR_CONTROLS_NEXT_MONTH_DISABLED);

        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStylePrimaryName("nextcontrol");

        InlineLabel controlSymbol = new InlineLabel(NEXTMONTHCONTROL);
        controlSymbol.setStylePrimaryName("control");

        InlineLabel controlCaption = new InlineLabel(controlText);
        controlCaption.setStylePrimaryName("month");

        controlPanel.add(controlCaption);
        controlPanel.add(controlSymbol);

        if (controlEnabled) {
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

    private void renderWeek(UIDL childUIDL, FlexTable cb, int weekRow) {

        cb.getRowFormatter().setStylePrimaryName(weekRow, "week");

        if (renderWeekNumbers) {
            int wn = childUIDL.getIntAttribute(ATTR_WEEK_NUMBER);
            cb.addCell(weekRow);

            Label weekNumber = new Label(Integer.toString(wn));
            cb.setWidget(weekRow, cb.getCellCount(weekRow) - 1, weekNumber);

            cb.getCellFormatter().setStyleName(weekRow, 0, "weeknumber");

        }

        int columnAddition = (renderWeekNumbers ? 1 : 0);

        for (int i = 0; i < childUIDL.getChildCount(); i++) {
            cb.addCell(weekRow);
            renderDay(childUIDL.getChildUIDL(i), cb, weekRow, i
                    + columnAddition);
        }

    }

    private void renderDay(UIDL childUIDL, FlexTable cb, int dayRow,
            int dayColumn) {

        int dayNumber = childUIDL.getIntAttribute(ATTR_DAY_NUMBER);

        // Label day = new Label(Integer.toString(dayNumber));

        String dayStyle = null;
        if (childUIDL.hasAttribute("style")) {
            StringBuilder styleBuilder = new StringBuilder();
            styleBuilder.append("day");
            styleBuilder.append(" ");
            styleBuilder.append(childUIDL.getStringAttribute("style"));
            dayStyle = styleBuilder.toString();
        } else {
            dayStyle = "day";
        }

        Label dayLabel = new Label(Integer.toString(dayNumber));

        if (childUIDL.getBooleanAttribute(ATTR_DAY_CLICKABLE)
                && !childUIDL.getBooleanAttribute(ATTR_DAY_DISABLED)) {
            HandlerRegistration dayClickHandler = dayLabel
                    .addClickHandler(new DayClickHandler(dayNumber));
            handlerRegistrations.add(dayClickHandler);
        } else if (childUIDL.getBooleanAttribute(ATTR_DAY_DISABLED)) {
            dayStyle += " disabled";
        }

        cb.setWidget(dayRow, dayColumn, dayLabel);

        cb.getCellFormatter().setStyleName(dayRow, dayColumn, dayStyle);
        cb.getCellFormatter().setAlignment(dayRow, dayColumn,
                HasHorizontalAlignment.ALIGN_CENTER,
                HasVerticalAlignment.ALIGN_MIDDLE);
    }

    private class DayClickHandler implements ClickHandler {

        private final int day;

        public DayClickHandler(int day) {
            this.day = day;
        }

        @Override
        public void onClick(ClickEvent event) {
            dayClick(day);
        }

    }
}
