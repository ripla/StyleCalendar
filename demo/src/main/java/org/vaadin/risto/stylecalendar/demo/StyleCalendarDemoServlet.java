package org.vaadin.risto.stylecalendar.demo;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", initParams = {
        @WebInitParam(name = "UIProvider", value = "org.vaadin.risto.stylecalendar.demo.StyleCalendarDemoUIProvider") })
public class StyleCalendarDemoServlet extends VaadinServlet {
}
