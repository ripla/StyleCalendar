package org.vaadin.risto.stylecalendar.widgetset.client;

import org.vaadin.risto.stylecalendar.widgetset.client.ui.field.VStyleCalendarField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ComponentConnector;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentContainerConnector;
import com.vaadin.terminal.gwt.client.ui.Connect;

@Connect(org.vaadin.risto.stylecalendar.StyleCalendarField.class)
public class StyleCalendarFieldConnector extends
        AbstractComponentContainerConnector {

    private static final long serialVersionUID = -8816361554030163829L;

    @Override
    public void updateCaption(ComponentConnector connector) {

    }

    @Override
    protected Widget createWidget() {
        return GWT.create(VStyleCalendarField.class);
    }

    @Override
    public StyleCalendarFieldState getState() {
        return (StyleCalendarFieldState) super.getState();
    }
}
