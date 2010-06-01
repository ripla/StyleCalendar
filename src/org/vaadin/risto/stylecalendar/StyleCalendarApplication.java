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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class StyleCalendarApplication extends Application {

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

        final StyleCalendar sc = new StyleCalendar();
        sc.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = -4914236743301835604L;

            public void valueChange(ValueChangeEvent event) {
                Date selected = (Date) event.getProperty().getValue();
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,
                        sc.getLocale());
                dateLabel.setValue("Date selected " + df.format(selected));
            }
        });

        sc.setDateOptionsGenerator(new StyleCalendar.DateOptionsGenerator() {

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

            public boolean isDateDisabled(Date date, StyleCalendar context) {
                return disabledList.contains(date);
            }

        });

        Button prev = new Button("Previous month");
        prev.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 5302233151902004930L;

            public void buttonClick(ClickEvent event) {
                sc.showPreviousMonth();
            }
        });

        Button next = new Button("Next month");
        next.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 1375120288648378326L;

            public void buttonClick(ClickEvent event) {
                sc.showNextMonth();
            }
        });

        CheckBox renderHeader = new CheckBox("Render header");
        renderHeader.setValue(true);
        renderHeader.setImmediate(true);
        renderHeader.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            public void valueChange(ValueChangeEvent event) {
                sc.setRenderHeader((Boolean) event.getProperty().getValue());

            }
        });

        CheckBox renderControls = new CheckBox("Render controls");
        renderControls.setValue(true);
        renderControls.setImmediate(true);
        renderControls.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            public void valueChange(ValueChangeEvent event) {
                sc.setRenderControls((Boolean) event.getProperty().getValue());

            }
        });

        CheckBox renderWeekNumbers = new CheckBox("Render weeknumbers");
        renderWeekNumbers.setValue(true);
        renderWeekNumbers.setImmediate(true);
        renderWeekNumbers.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            public void valueChange(ValueChangeEvent event) {
                sc.setRenderWeekNumbers((Boolean) event.getProperty()
                        .getValue());

            }
        });

        CheckBox immediate = new CheckBox("Set Immediate");
        immediate.setImmediate(true);
        immediate.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            public void valueChange(ValueChangeEvent event) {
                sc.setImmediate((Boolean) event.getProperty().getValue());

            }
        });
        immediate.setValue(Boolean.TRUE);

        final NativeSelect locales = new NativeSelect("Locale");
        locales.setNullSelectionAllowed(false);
        locales.setImmediate(true);
        locales.addContainerProperty("locale", Locale.class, null);
        locales.addItem("Finnish").getItemProperty("locale").setValue(
                new Locale("fi", "FI"));
        locales.addItem("UK").getItemProperty("locale").setValue(Locale.UK);
        locales.addItem("US").getItemProperty("locale").setValue(Locale.US);
        locales.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 4267830073546299627L;

            public void valueChange(ValueChangeEvent event) {
                Item selected = locales.getItem(event.getProperty().getValue());
                sc.setLocale((Locale) selected.getItemProperty("locale")
                        .getValue());
            }
        });
        locales.select("Finnish");

        Button makeRed = new Button("Style selected red");
        makeRed.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 2751283318694896800L;

            public void buttonClick(ClickEvent event) {
                Date selected = (Date) sc.getValue();
                if (selected != null) {
                    greenList.remove(selected);
                    redList.add(selected);
                    sc.requestRepaint();
                }
            }
        });

        Button makeGreen = new Button("Style selected green");
        makeGreen.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 2751283318694896800L;

            public void buttonClick(ClickEvent event) {
                Date selected = (Date) sc.getValue();
                if (selected != null) {
                    redList.remove(selected);
                    greenList.add(selected);
                    sc.requestRepaint();
                }
            }
        });

        Button makeDisabled = new Button("Enable/disable selected");
        makeDisabled.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 2751283318694896800L;

            public void buttonClick(ClickEvent event) {
                Date selected = (Date) sc.getValue();
                if (selected != null) {
                    if (disabledList.contains(selected)) {
                        disabledList.remove(selected);
                    } else {
                        disabledList.add(selected);
                    }
                    sc.requestRepaint();
                }
            }
        });

        HorizontalLayout hl1 = new HorizontalLayout();
        hl1.addComponent(prev);
        hl1.addComponent(next);
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

        Panel options = new Panel("Component options");
        options.setSizeUndefined();
        ((AbstractOrderedLayout) options.getContent()).setSpacing(true);
        options.addComponent(hl1);
        options.addComponent(hl2);
        options.addComponent(hl3);

        final StyleCalendar startDate = new StyleCalendar("Enabled dates start");
        final StyleCalendar endDate = new StyleCalendar("Enabled dates end");

        startDate.setImmediate(true);
        endDate.setImmediate(true);

        startDate.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                sc.setEnabledDateRange((Date) event.getProperty().getValue(),
                        (Date) endDate.getValue());
            }
        });

        endDate.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                sc.setEnabledDateRange((Date) startDate.getValue(),
                        (Date) event.getProperty().getValue());
            }
        });

        Panel startEnd = new Panel("Enabled date range");
        HorizontalLayout startEndLayout = new HorizontalLayout();
        startEndLayout.setWidth("100%");
        startEndLayout.setSpacing(true);
        startEnd
                .addComponent(new Label(
                        "Use these two calendars to set the range for enabled dates in the main calendar."));
        startEnd.addComponent(startEndLayout);
        startEnd.setWidth("500px");

        startEndLayout.addComponent(startDate);
        startEndLayout.addComponent(endDate);

        Panel mainCalendarPanel = new Panel("Main calendar");
        mainCalendarPanel.setSizeUndefined();
        mainCalendarPanel.addComponent(new Label(
                "All the component options affect this calendar only."));
        mainCalendarPanel.addComponent(sc);
        mainCalendarPanel.addComponent(dateLabel);

        HorizontalLayout optionsLayout = new HorizontalLayout();
        optionsLayout.setSpacing(true);
        optionsLayout.addComponent(mainCalendarPanel);
        optionsLayout.addComponent(startEnd);

        mainLayout.addComponent(options);
        mainLayout.addComponent(optionsLayout);
        mainLayout.addComponent(new StyleCalendarField());

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
