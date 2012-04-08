package org.vaadin.risto.stylecalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Date selector component that uses a {@link StyleCalendar} as a popup for date
 * selection.
 * 
 * @author Risto Yrjänä / Vaadin
 */
@ClientWidget(org.vaadin.risto.stylecalendar.widgetset.client.ui.VStyleCalendarField.class)
public class StyleCalendarField extends AbstractField implements
        ComponentContainer {

    private static final long serialVersionUID = 7509453410070681818L;

    private StyleCalendar internalCalendar;
    private boolean showPopup;

    private String nullRepresentation;

    public StyleCalendarField(String caption) {
        setCaption(caption);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        String paintValue = getPaintValue();

        target.addAttribute("value", paintValue);

        target.addVariable(this, "showPopup", isShowPopup());
        if (isShowPopup()) {
            target.startTag("calendar");

            if (internalCalendar == null) {
                internalCalendar = getNewStyleCalendar();
            }

            internalCalendar.paint(target);
            target.endTag("calendar");
        }

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

    /**
     * @return
     */
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
    public Class<?> getType() {
        return Date.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractField#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        super.setValue(newValue);

        if (internalCalendar != null) {
            internalCalendar.setValue(newValue);
            internalCalendar.setShowingDate((Date) newValue);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractField#changeVariables(java.lang.Object,
     * java.util.Map)
     */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey("showPopup")
                && (Boolean) variables.get("showPopup") && !isShowPopup()) {
            setShowPopup(true);

        } else if (variables.containsKey("showPopup")
                && !((Boolean) variables.get("showPopup")) && isShowPopup()) {
            setShowPopup(false);

        } else if (variables.containsKey("value")) {
            handleValueFromClient((String) variables.get("value"));
        }
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.ComponentContainer#addComponent(com.vaadin.ui.Component)
     */
    @Override
    public void addComponent(Component c) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.ComponentContainer#addListener(com.vaadin.ui.ComponentContainer
     * .ComponentAttachListener)
     */
    @Override
    public void addListener(ComponentAttachListener listener) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.ComponentContainer#addListener(com.vaadin.ui.ComponentContainer
     * .ComponentDetachListener)
     */
    @Override
    public void addListener(ComponentDetachListener listener) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.ComponentContainer#getComponentIterator()
     */
    @Override
    public Iterator<Component> getComponentIterator() {
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

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.ComponentContainer#moveComponentsFrom(com.vaadin.ui.
     * ComponentContainer)
     */
    @Override
    public void moveComponentsFrom(ComponentContainer source) {
        throw new UnsupportedOperationException();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.ComponentContainer#removeAllComponents()
     */
    @Override
    public void removeAllComponents() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.ComponentContainer#removeComponent(com.vaadin.ui.Component)
     */
    @Override
    public void removeComponent(Component c) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.ComponentContainer#removeListener(com.vaadin.ui.
     * ComponentContainer.ComponentAttachListener)
     */
    @Override
    public void removeListener(ComponentAttachListener listener) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.ComponentContainer#removeListener(com.vaadin.ui.
     * ComponentContainer.ComponentDetachListener)
     */
    @Override
    public void removeListener(ComponentDetachListener listener) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.ComponentContainer#replaceComponent(com.vaadin.ui.Component
     * , com.vaadin.ui.Component)
     */
    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.ComponentContainer#requestRepaintAll()
     */
    @Override
    public void requestRepaintAll() {
        requestRepaint();
        internalCalendar.requestRepaint();
    }

    public void setNullRepresentation(String nullRepresentation) {
        this.nullRepresentation = nullRepresentation;
    }

    public String getNullRepresentation() {
        return nullRepresentation;
    }

    @Override
    public Date getValue() {
        return (Date) super.getValue();
    }
}
