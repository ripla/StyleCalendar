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

    /** Set the tagname used to statically resolve widget from UIDL. */
    public static final String TAGNAME = "stylecalendar";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-" + TAGNAME;

    private static final String PREVMONTHCONTROL = "<<";
    private static final String NEXTMONTHCONTROL = ">>";

    /** Component identifier in UIDL communications. */
    String uidlId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    private boolean renderWeekNumbers;

    private boolean renderControls;

    private boolean renderHeader;

    /**
     * List of click handlers. Handlers are removed on each repaint.
     */
    private final List<HandlerRegistration> handlerRegistrations;

    private boolean immediate;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VStyleCalendar() {
        super();

        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);

        handlerRegistrations = new ArrayList<HandlerRegistration>();
    }

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
        renderWeekNumbers = uidl.getBooleanAttribute("renderWeekNumbers");
        renderHeader = uidl.getBooleanAttribute("renderHeader");
        renderControls = uidl.getBooleanAttribute("renderControls");

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

        String[] weekDayNames = uidl.getStringArrayVariable("weekDayNames");

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
        client.updateVariable(uidlId, "clickedDay", day, immediate);
    }

    /**
     * Send previous clicked message to server. Control messages are always
     * immediate.
     */
    public void prevClick() {
        client.updateVariable(uidlId, "prevClick", true, true);
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
        String currentMonth = childUIDL.getStringAttribute("currentMonth");
        int currentYear = childUIDL.getIntAttribute("currentYear");

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

        String controlText = childUIDL.getStringAttribute("prevMonth");

        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStylePrimaryName("prevcontrol");

        InlineLabel controlSymbol = new InlineLabel(PREVMONTHCONTROL);
        controlSymbol.setStylePrimaryName("control");

        InlineLabel controlCaption = new InlineLabel(controlText);
        controlCaption.setStylePrimaryName("month");

        controlPanel.add(controlSymbol);
        controlPanel.add(controlCaption);

        ClickHandler prevClick = new ClickHandler() {
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

        return controlPanel;
    }

    private Widget renderNextControl(UIDL childUIDL) {
        String controlText = childUIDL.getStringAttribute("nextMonth");

        FlowPanel controlPanel = new FlowPanel();
        controlPanel.setStylePrimaryName("nextcontrol");

        InlineLabel controlSymbol = new InlineLabel(NEXTMONTHCONTROL);
        controlSymbol.setStylePrimaryName("control");

        InlineLabel controlCaption = new InlineLabel(controlText);
        controlCaption.setStylePrimaryName("month");

        controlPanel.add(controlCaption);
        controlPanel.add(controlSymbol);

        ClickHandler nextClick = new ClickHandler() {
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

        return controlPanel;
    }

    private void renderWeek(UIDL childUIDL, FlexTable cb, int weekRow) {

        cb.getRowFormatter().setStylePrimaryName(weekRow, "week");

        if (renderWeekNumbers) {
            int wn = childUIDL.getIntAttribute("number");
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

        int dayNumber = childUIDL.getIntAttribute("daynumber");

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

        if (childUIDL.getBooleanAttribute("clickable")) {
            HandlerRegistration dayClickHandler = dayLabel
                    .addClickHandler(new DayClickHandler(dayNumber));
            handlerRegistrations.add(dayClickHandler);
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

        public void onClick(ClickEvent event) {
            dayClick(day);
        }

    }
}
