package org.vaadin.risto.stylecalendar.demo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.vaadin.risto.stylecalendar.DateOptionsGenerator;
import org.vaadin.risto.stylecalendar.StyleCalendar;
import org.vaadin.risto.stylecalendar.StyleCalendarField;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("demo")
public class StyleCalendarDemoUI extends UI {

    private static final String CENTERWIDTH = "700px";
    protected List<Date> disabledList = new ArrayList<>();
    private StyleCalendar mainCalendar;
    private StyleCalendarField styleCalendarField;

    @Override
    protected void init(VaadinRequest request) {
        Page.getCurrent().setTitle("StyleCalendar demo");
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        setContent(mainLayout);

        final Label dateLabel = new Label("Nothing selected");

        final List<Date> greenList = new ArrayList<Date>();
        final List<Date> redList = new ArrayList<Date>();

        mainCalendar = new StyleCalendar();
        styleCalendarField = new StyleCalendarField("StyleCalendarField");
        styleCalendarField.setNullRepresentation("");

        mainCalendar.addValueChangeListener(event -> {
            Date selected = (Date) event.getProperty().getValue();
            if (selected != null) {
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,
                        mainCalendar.getLocale());
                dateLabel.setValue("Date selected " + df.format(selected));
            } else {
                dateLabel.setValue("Nothing selected");
            }
        });

        setDateOptionsGenerator(greenList, redList, mainCalendar);

        Layout options = createComponentOptionsLayout(greenList, redList);

        Layout startEnd = createDateRangeLayout(mainCalendar);

        Panel mainCalendarPanel = new Panel("Main calendar");
        VerticalLayout mainCalendarLayout = new VerticalLayout();
        mainCalendarLayout.setMargin(true);
        mainCalendarLayout.setSpacing(true);
        mainCalendarPanel.setContent(mainCalendarLayout);
        mainCalendarPanel.setWidth(CENTERWIDTH);

        mainCalendarLayout.addComponent(new Label(
                "All the component options affect this calendar only."));
        mainCalendarLayout.addComponent(mainCalendar);
        mainCalendarLayout.addComponent(dateLabel);

        mainCalendarLayout.addComponent(styleCalendarField);

        Panel optionsPanel = new Panel("Component options");
        VerticalLayout optionsLayout = new VerticalLayout();
        optionsLayout.setMargin(true);
        optionsLayout.setSpacing(true);
        optionsPanel.setContent(optionsLayout);
        optionsPanel.setWidth(CENTERWIDTH);
        optionsLayout.addComponent(startEnd);
        optionsLayout.addComponent(options);

        mainLayout.addComponent(mainCalendarPanel);
        mainLayout.addComponent(optionsPanel);

        mainLayout
                .setComponentAlignment(mainCalendarPanel, Alignment.TOP_CENTER);
        mainLayout.setComponentAlignment(optionsPanel, Alignment.TOP_CENTER);
    }

    private Layout createDateRangeLayout(final StyleCalendar mainCalendar) {
        final StyleCalendar startDate = new StyleCalendar(
                "Enabled dates start");
        final StyleCalendar endDate = new StyleCalendar("Enabled dates end");

        startDate.setImmediate(true);
        endDate.setImmediate(true);

        startDate.setWidth("100%");
        endDate.setWidth("100%");

        startDate.addValueChangeListener(event -> mainCalendar
                .setEnabledDateRange((Date) event.getProperty().getValue(),
                        endDate.getValue()));

        endDate.addValueChangeListener(event -> mainCalendar
                .setEnabledDateRange(startDate.getValue(),
                        (Date) event.getProperty().getValue()));

        Button reset = new Button("Reset disabled dates");
        reset.addClickListener(event -> {
            startDate.setValue(null);
            endDate.setValue(null);
        });

        VerticalLayout startEnd = new VerticalLayout();
        startEnd.setSpacing(true);
        Label caption = new Label("Enabled date range");
        caption.setStyleName(ValoTheme.LABEL_H2);
        HorizontalLayout startEndCalendars = new HorizontalLayout();
        startEndCalendars.setWidth("100%");
        startEndCalendars.setSpacing(true);

        startEndCalendars.addComponent(startDate);
        startEndCalendars.addComponent(endDate);

        startEnd.addComponent(caption);
        startEnd.addComponent(new Label(
                "Use these two calendars to set the range for enabled dates in the main calendar."));
        startEnd.addComponent(startEndCalendars);
        startEnd.addComponent(reset);
        startEnd.setWidth("100%");

        return startEnd;
    }

    @SuppressWarnings("unchecked")
    private Layout createComponentOptionsLayout(final List<Date> greenList,
            final List<Date> redList) {
        Label caption = new Label("Other options");
        caption.setStyleName(ValoTheme.LABEL_H2);

        Button prevMonth = new Button("Previous month");
        prevMonth.addClickListener(event -> mainCalendar.showPreviousMonth());

        Button nextMonth = new Button("Next month");
        nextMonth.addClickListener(event -> mainCalendar.showNextMonth());

        Button prevYear = new Button("Previous year");
        prevYear.addClickListener(event -> mainCalendar.showPreviousYear());

        Button nextYear = new Button("Next year");
        nextYear.addClickListener(event -> mainCalendar.showNextYear());

        CheckBox renderHeader = new CheckBox("Render header");
        renderHeader.setValue(true);
        renderHeader.setImmediate(true);
        renderHeader.addValueChangeListener(event -> mainCalendar
                .setRenderHeader((Boolean) event.getProperty().getValue()));

        CheckBox renderControls = new CheckBox("Render controls");
        renderControls.setValue(true);
        renderControls.setImmediate(true);
        renderControls.addValueChangeListener(event -> mainCalendar
                .setRenderControls((Boolean) event.getProperty().getValue()));

        CheckBox renderWeekNumbers = new CheckBox("Render weeknumbers");
        renderWeekNumbers.setValue(true);
        renderWeekNumbers.setImmediate(true);
        renderWeekNumbers.addValueChangeListener(event -> mainCalendar
                .setRenderWeekNumbers(
                        (Boolean) event.getProperty().getValue()));

        CheckBox deselectOnClick = new CheckBox("Deselect date on click");
        deselectOnClick.setValue(false);
        deselectOnClick.setImmediate(true);
        deselectOnClick.addValueChangeListener(event -> mainCalendar
                .setDeselectOnClick((Boolean) event.getProperty().getValue()));

        final NativeSelect locales = new NativeSelect("Locale");
        locales.setNullSelectionAllowed(false);
        locales.setImmediate(true);
        locales.addContainerProperty("locale", Locale.class, null);
        locales.addItem("Finnish").getItemProperty("locale")
                .setValue(new Locale("fi", "FI"));
        locales.addItem("UK").getItemProperty("locale").setValue(Locale.UK);
        locales.addItem("US").getItemProperty("locale").setValue(Locale.US);
        locales.addValueChangeListener((Property.ValueChangeListener) event -> {
            Item selected = locales.getItem(event.getProperty().getValue());
            mainCalendar.setLocale(
                    (Locale) selected.getItemProperty("locale").getValue());
            styleCalendarField.setLocale(
                    (Locale) selected.getItemProperty("locale").getValue());

        });
        locales.select("Finnish");

        Button makeRed = new Button("Style selected red");
        makeRed.addClickListener(event -> {
            Date selected = mainCalendar.getValue();
            if (selected != null) {
                greenList.remove(selected);
                redList.add(selected);
                mainCalendar.markAsDirty();
            }
        });

        Button makeGreen = new Button("Style selected green");
        makeGreen.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 2751283318694896800L;

            @Override
            public void buttonClick(ClickEvent event) {
                Date selected = mainCalendar.getValue();
                if (selected != null) {
                    redList.remove(selected);
                    greenList.add(selected);
                    mainCalendar.markAsDirty();
                }
            }
        });

        Button makeDisabled = new Button("Enable/disable selected");
        makeDisabled.addClickListener(event -> {
            Date selected = mainCalendar.getValue();
            if (selected != null) {
                if (disabledList.contains(selected)) {
                    disabledList.remove(selected);
                } else {
                    disabledList.add(selected);
                }
                mainCalendar.markAsDirty();
            }
        });

        HorizontalLayout controlButtons = new HorizontalLayout();
        controlButtons.addComponents(prevMonth, nextMonth, prevYear, nextYear);
        controlButtons.setSpacing(true);

        HorizontalLayout optionCheckboxes = new HorizontalLayout();
        optionCheckboxes
                .addComponents(renderHeader, renderControls, renderWeekNumbers,
                        deselectOnClick);
        optionCheckboxes.setSpacing(true);

        HorizontalLayout otherOptions = new HorizontalLayout();
        otherOptions.addComponents(locales, makeRed, makeGreen, makeDisabled);
        otherOptions.setSpacing(true);
        otherOptions.setComponentAlignment(makeRed, Alignment.BOTTOM_CENTER);
        otherOptions.setComponentAlignment(makeGreen, Alignment.BOTTOM_CENTER);
        otherOptions
                .setComponentAlignment(makeDisabled, Alignment.BOTTOM_CENTER);

        VerticalLayout options = new VerticalLayout();
        options.setSpacing(true);
        options.addComponents(caption, controlButtons, optionCheckboxes,
                otherOptions);
        return options;
    }

    private void setDateOptionsGenerator(final List<Date> greenList,
            final List<Date> redList, final StyleCalendar mainCalendar) {
        mainCalendar.setDateOptionsGenerator(new DateOptionsGenerator() {

            @Override
            public String getStyleName(Date date, StyleCalendar context) {

                for (Date redDate : redList) {
                    if (dateEquals(date, redDate)) {
                        return "red";
                    }
                }

                for (Date greenDate : greenList) {
                    if (dateEquals(greenDate, date)) {
                        return "green";
                    }
                }

                return null;
            }

            @Override
            public boolean isDateDisabled(Date date, StyleCalendar context) {
                return disabledList.contains(date);
            }

            @Override
            public String getTooltip(Date date, StyleCalendar context) {
                Calendar calendar = Calendar.getInstance(context.getLocale());
                calendar.setTime(date);
                return String.format("Week %d, day %d",
                        calendar.get(Calendar.WEEK_OF_YEAR),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }

        });
    }

    public boolean dateEquals(Date first, Date second) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(first);
        c2.setTime(second);

        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.HOUR, 0);

        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.HOUR, 0);

        return c1.equals(c2);
    }
}
