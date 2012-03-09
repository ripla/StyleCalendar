package org.vaadin.risto.stylecalendar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class StyleCalendarApplication extends Application {

    private static final String CENTERWIDTH = "700px";
    private static final long serialVersionUID = -2802197153513393573L;
    protected List<Date> disabledList = new ArrayList<Date>();

    @Override
    public void init() {
        final Window mainWindow = new Window("Stylecalendar Application");
        setMainWindow(mainWindow);
        setTheme("stylecalendartheme");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);

        mainWindow.setContent(mainLayout);

        final Label dateLabel = new Label("");

        final List<Date> greenList = new ArrayList<Date>();
        final List<Date> redList = new ArrayList<Date>();

        final StyleCalendar mainCalendar = new StyleCalendar();
        mainCalendar.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = -4914236743301835604L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Date selected = (Date) event.getProperty().getValue();
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,
                        mainCalendar.getLocale());
                dateLabel.setValue("Date selected " + df.format(selected));
            }
        });

        setDateOptionsGenerator(greenList, redList, mainCalendar);

        Layout options = createComponentOptionsPanel(greenList, redList,
                mainCalendar);

        Layout startEnd = createDateRangePanel(mainCalendar);

        Panel mainCalendarPanel = new Panel("Main calendar");
        mainCalendarPanel.setWidth(CENTERWIDTH);
        mainCalendarPanel.addComponent(new Label(
                "All the component options affect this calendar only."));
        mainCalendarPanel.addComponent(mainCalendar);
        mainCalendarPanel.addComponent(dateLabel);

        Panel optionsPanel = new Panel("Component options");
        ((AbstractOrderedLayout) optionsPanel.getContent()).setSpacing(true);
        optionsPanel.setWidth(CENTERWIDTH);
        optionsPanel.addComponent(startEnd);
        optionsPanel.addComponent(options);

        mainLayout.addComponent(mainCalendarPanel);
        mainLayout.addComponent(optionsPanel);

        mainLayout.setComponentAlignment(mainCalendarPanel,
                Alignment.TOP_CENTER);
        mainLayout.setComponentAlignment(optionsPanel, Alignment.TOP_CENTER);
    }

    private Layout createDateRangePanel(final StyleCalendar mainCalendar) {
        final StyleCalendar startDate = new StyleCalendar("Enabled dates start");
        final StyleCalendar endDate = new StyleCalendar("Enabled dates end");

        startDate.setImmediate(true);
        endDate.setImmediate(true);

        startDate.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 5451403327381090983L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                mainCalendar.setEnabledDateRange((Date) event.getProperty()
                        .getValue(), (Date) endDate.getValue());
            }
        });

        endDate.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = -6600207899150787566L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                mainCalendar.setEnabledDateRange((Date) startDate.getValue(),
                        (Date) event.getProperty().getValue());
            }
        });

        Button reset = new Button("Reset disabled dates");
        reset.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = -6445668268341479730L;

            @Override
            public void buttonClick(ClickEvent event) {
                startDate.setValue(null);
                endDate.setValue(null);
            }
        });

        VerticalLayout startEnd = new VerticalLayout();
        startEnd.setSpacing(true);
        startEnd.setMargin(false, false, true, false);
        Label caption = new Label("Enabled date range");
        caption.setStyleName(Reindeer.LABEL_H2);
        HorizontalLayout startEndCalendars = new HorizontalLayout();
        startEndCalendars.setSpacing(true);

        startEndCalendars.addComponent(startDate);
        startEndCalendars.addComponent(endDate);

        startEnd.addComponent(caption);
        startEnd.addComponent(new Label(
                "Use these two calendars to set the range for enabled dates in the main calendar."));
        startEnd.addComponent(startEndCalendars);
        startEnd.addComponent(reset);
        startEnd.setWidth(CENTERWIDTH);

        return startEnd;
    }

    private Layout createComponentOptionsPanel(final List<Date> greenList,
            final List<Date> redList, final StyleCalendar mainCalendar) {
        Label caption = new Label("Other options");
        caption.setStyleName(Reindeer.LABEL_H2);

        Button prevMonth = new Button("Previous month");
        prevMonth.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 5302233151902004930L;

            @Override
            public void buttonClick(ClickEvent event) {
                mainCalendar.showPreviousMonth();
            }
        });

        Button nextMonth = new Button("Next month");
        nextMonth.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 1375120288648378326L;

            @Override
            public void buttonClick(ClickEvent event) {
                mainCalendar.showNextMonth();
            }
        });

        Button prevYear = new Button("Previous year");
        prevYear.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 5302233151902004930L;

            @Override
            public void buttonClick(ClickEvent event) {
                mainCalendar.showPreviousYear();
            }
        });

        Button nextYear = new Button("Next year");
        nextYear.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 1375120288648378326L;

            @Override
            public void buttonClick(ClickEvent event) {
                mainCalendar.showNextYear();
            }
        });

        CheckBox renderHeader = new CheckBox("Render header");
        renderHeader.setValue(true);
        renderHeader.setImmediate(true);
        renderHeader.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                mainCalendar.setRenderHeader((Boolean) event.getProperty()
                        .getValue());

            }
        });

        CheckBox renderControls = new CheckBox("Render controls");
        renderControls.setValue(true);
        renderControls.setImmediate(true);
        renderControls.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                mainCalendar.setRenderControls((Boolean) event.getProperty()
                        .getValue());

            }
        });

        CheckBox renderWeekNumbers = new CheckBox("Render weeknumbers");
        renderWeekNumbers.setValue(true);
        renderWeekNumbers.setImmediate(true);
        renderWeekNumbers.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                mainCalendar.setRenderWeekNumbers((Boolean) event.getProperty()
                        .getValue());

            }
        });

        CheckBox immediate = new CheckBox("Set Immediate");
        immediate.setImmediate(true);
        immediate.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                mainCalendar.setImmediate((Boolean) event.getProperty()
                        .getValue());

            }
        });
        immediate.setValue(Boolean.TRUE);

        final NativeSelect locales = new NativeSelect("Locale");
        locales.setNullSelectionAllowed(false);
        locales.setImmediate(true);
        locales.addContainerProperty("locale", Locale.class, null);
        locales.addItem("Finnish").getItemProperty("locale")
                .setValue(new Locale("fi", "FI"));
        locales.addItem("UK").getItemProperty("locale").setValue(Locale.UK);
        locales.addItem("US").getItemProperty("locale").setValue(Locale.US);
        locales.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 4267830073546299627L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Item selected = locales.getItem(event.getProperty().getValue());
                mainCalendar.setLocale((Locale) selected.getItemProperty(
                        "locale").getValue());
            }
        });
        locales.select("Finnish");

        Button makeRed = new Button("Style selected red");
        makeRed.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 2751283318694896800L;

            @Override
            public void buttonClick(ClickEvent event) {
                Date selected = (Date) mainCalendar.getValue();
                if (selected != null) {
                    greenList.remove(selected);
                    redList.add(selected);
                    mainCalendar.requestRepaint();
                }
            }
        });

        Button makeGreen = new Button("Style selected green");
        makeGreen.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 2751283318694896800L;

            @Override
            public void buttonClick(ClickEvent event) {
                Date selected = (Date) mainCalendar.getValue();
                if (selected != null) {
                    redList.remove(selected);
                    greenList.add(selected);
                    mainCalendar.requestRepaint();
                }
            }
        });

        Button makeDisabled = new Button("Enable/disable selected");
        makeDisabled.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 2751283318694896800L;

            @Override
            public void buttonClick(ClickEvent event) {
                Date selected = (Date) mainCalendar.getValue();
                if (selected != null) {
                    if (disabledList.contains(selected)) {
                        disabledList.remove(selected);
                    } else {
                        disabledList.add(selected);
                    }
                    mainCalendar.requestRepaint();
                }
            }
        });

        HorizontalLayout hl1 = new HorizontalLayout();
        hl1.addComponent(prevMonth);
        hl1.addComponent(nextMonth);
        hl1.addComponent(prevYear);
        hl1.addComponent(nextYear);
        hl1.setSpacing(true);

        HorizontalLayout hl2 = new HorizontalLayout();
        hl2.addComponent(renderHeader);
        hl2.addComponent(renderControls);
        hl2.addComponent(renderWeekNumbers);
        hl2.addComponent(immediate);
        hl2.setSpacing(true);

        HorizontalLayout hl3 = new HorizontalLayout();
        hl3.addComponent(locales);
        hl3.addComponent(makeRed);
        hl3.addComponent(makeGreen);
        hl3.addComponent(makeDisabled);
        hl3.setSpacing(true);
        hl3.setComponentAlignment(makeRed, Alignment.BOTTOM_CENTER);
        hl3.setComponentAlignment(makeGreen, Alignment.BOTTOM_CENTER);
        hl3.setComponentAlignment(makeDisabled, Alignment.BOTTOM_CENTER);

        VerticalLayout options = new VerticalLayout();
        options.setSpacing(true);
        options.addComponent(caption);
        options.addComponent(hl1);
        options.addComponent(hl2);
        options.addComponent(hl3);
        return options;
    }

    private void setDateOptionsGenerator(final List<Date> greenList,
            final List<Date> redList, final StyleCalendar mainCalendar) {
        mainCalendar
                .setDateOptionsGenerator(new StyleCalendar.DateOptionsGenerator() {

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
                    public boolean isDateDisabled(Date date,
                            StyleCalendar context) {
                        return disabledList.contains(date);
                    }

                    @Override
                    public String getTooltip(Date date, StyleCalendar context) {
                        Calendar calendar = Calendar.getInstance();
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
