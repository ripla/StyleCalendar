package org.vaadin.risto.stylecalendar.widgetset.client.shared.field;

import com.vaadin.shared.AbstractFieldState;

public class StyleCalendarFieldState extends AbstractFieldState {

    private static final long serialVersionUID = 4069840486355612786L;

    private String value;
    private boolean showPopup;

    public String getFieldValue() {
        return value;
    }

    public void setFieldValue(String value) {
        this.value = value;
    }

    public boolean isShowPopup() {
        return showPopup;
    }

    public void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
    }
}
