package org.vaadin.risto.stylecalendar;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class StyleCalendarFieldFullWidthUI extends UI {

    private static final long serialVersionUID = -6376868946443066319L;

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout main = new VerticalLayout();
        main.setWidth("400px");

        main.addComponent(getPanelForField(new StyleCalendarField()));
        main.addComponent(getPanelForField(new TextField()));

        setContent(main);
    }

    private Component getPanelForField(AbstractField<?> field) {
        Panel panel = new Panel();
        VerticalLayout layout = new VerticalLayout();
        panel.setWidth("400px");
        layout.setWidth("100%");
        field.setWidth("100%");

        layout.addComponent(field);
        panel.setContent(layout);

        return panel;
    }
}
