package org.vaadin.risto.stylecalendar.client.ui.field.event;

import com.google.gwt.event.shared.GwtEvent;

public class PopupVisibilityChangedEvent extends
        GwtEvent<PopupVisibilityChangeHandler> {

    private static Type<PopupVisibilityChangeHandler> TYPE;
    private final boolean visible;

    public PopupVisibilityChangedEvent(boolean visible) {
        this.visible = visible;
    }

    @Override
    public Type<PopupVisibilityChangeHandler> getAssociatedType() {
        return getType();
    }

    public static Type<PopupVisibilityChangeHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(PopupVisibilityChangeHandler handler) {
        handler.handlePopupVisibilityChanged(this);
    }

    public boolean isVisible() {
        return visible;
    }
}
