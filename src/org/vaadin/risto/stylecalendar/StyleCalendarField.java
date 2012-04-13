package org.vaadin.risto.stylecalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import org.vaadin.risto.stylecalendar.widgetset.client.StyleCalendarFieldState;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

/**
 * Date selector component that uses a {@link StyleCalendar} as a popup for date
 * selection.
 * 
 * @author Risto Yrjänä / Vaadin
 */
public class StyleCalendarField extends AbstractField<Date> implements
        HasComponents {

    private static final long serialVersionUID = 7509453410070681818L;

    private StyleCalendar internalCalendar;
    private boolean showPopup;

    private String nullRepresentation;

    public StyleCalendarField(String caption) {
        setCaption(caption);
    }

    @Override
    public StyleCalendarFieldState getState() {
        return (StyleCalendarFieldState) super.getState();
    }

    @Override
    public void updateState() {
        super.updateState();

        String paintValue = getPaintValue();

        getState().setFieldValue(paintValue);

        getState().setShowPopup(isShowPopup());
    }

    @Override
    public boolean isComponentVisible(Component childComponent) {
        return isShowPopup();
    }

    protected String getPaintValue() {
        Object value = getValue();

        if (value == null) {
            if (getNullRepresentation() != null) {
                return getNullRepresentation();

            } else {
                return "null";
            }

        } else {
            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT,
                    getLocale());
            return format.format(value);
        }
    }

    protected StyleCalendar getNewStyleCalendar() {
        StyleCalendar calendar = new StyleCalendar();
        calendar.setValue(getValue());

        if (getValue() != null) {
            calendar.setShowingDate(getValue());
        }

        calendar.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 8127366932646297743L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                StyleCalendarField.this
                        .setValue(event.getProperty().getValue());
            }
        });

        calendar.setParent(this);
        calendar.setImmediate(true);

        return calendar;
    }

    protected void removeStyleCalendar(StyleCalendar calendar) {
        calendar.setParent(null);
        calendar.removeListener((ValueChangeListener) this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractField#getType()
     */
    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractField#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object newValue) {
        super.setValue(newValue);

        if (internalCalendar != null) {
            internalCalendar.setValue(newValue);
            internalCalendar.setShowingDate((Date) newValue);
        }
    }

    // TODO
    // @Override
    // public void changeVariables(Object source, Map<String, Object> variables)
    // {
    // super.changeVariables(source, variables);
    //
    // if (variables.containsKey("showPopup")
    // && (Boolean) variables.get("showPopup") && !isShowPopup()) {
    // setShowPopup(true);
    //
    // } else if (variables.containsKey("showPopup")
    // && !((Boolean) variables.get("showPopup")) && isShowPopup()) {
    // setShowPopup(false);
    //
    // } else if (variables.containsKey("value")) {
    // handleValueFromClient((String) variables.get("value"));
    // }
    // }

    protected void handleValueFromClient(String valueFromClient) {
        try {
            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT,
                    getLocale());
            Date value = format.parse(valueFromClient);
            setValue(value);
        } catch (ParseException e) {

        }
    }

    protected void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
        if (!showPopup) {
            removeStyleCalendar(internalCalendar);
            internalCalendar = null;
        }
        requestRepaint();
    }

    protected boolean isShowPopup() {
        return showPopup;
    }

    @Override
    public Iterator<Component> iterator() {
        return new Iterator<Component>() {

            private boolean first = (internalCalendar == null);

            @Override
            public boolean hasNext() {
                return !first;
            }

            @Override
            public Component next() {
                if (!first) {
                    first = true;
                    return internalCalendar;
                } else {
                    return null;
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void setNullRepresentation(String nullRepresentation) {
        this.nullRepresentation = nullRepresentation;
    }

    public String getNullRepresentation() {
        return nullRepresentation;
    }

    @Override
    public Date getValue() {
        return super.getValue();
    }

    @Override
    public void requestRepaintAll() {
        if (internalCalendar != null) {
            internalCalendar.requestRepaint();
        }
    }

    @Override
    public Iterator<Component> getComponentIterator() {
        return iterator();
    }
}
