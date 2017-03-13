package org.vaadin.risto.stylecalendar.demo;

import org.vaadin.risto.stylecalendar.StyleCalendarField;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
public class StyleCalendarFieldFullWidthUI extends UI {

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
