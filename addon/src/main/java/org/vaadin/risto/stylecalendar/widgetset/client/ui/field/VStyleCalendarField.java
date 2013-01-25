package org.vaadin.risto.stylecalendar.widgetset.client.ui.field;

import java.util.Collections;
import java.util.Iterator;

import org.vaadin.risto.stylecalendar.widgetset.client.ui.field.event.PopupVisibilityChangeHandler;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.field.event.PopupVisibilityChangedEvent;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.field.event.StyleCalendarFieldValueChanged;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VTooltip;
import com.vaadin.client.ui.VOverlay;

public class VStyleCalendarField extends TextBox implements Iterable<Widget> {

    public static final String CLASSNAME = "v-stylecalendarfield";

    private final CustomPopup popup;

    public VStyleCalendarField() {
        super();

        setStyleName(CLASSNAME);

        addValueChangeHandler(new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                valueChange(event.getValue());
            }
        });

        popup = new CustomPopup();
        popup.setStyleName(CLASSNAME + "-popup");
        popup.addStyleName("v-popupview-popup"); // to get font-size etc. from
        // the Vaadin theme

        // When we click to open the popup...
        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popupVisibilityChange(true);
            }
        });

        // ..and when we close it
        popup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                popupVisibilityChange(false);
            }
        });

        popup.setAnimationEnabled(true);
        popup.addAutoHidePartner(getElement());
        sinkEvents(VTooltip.TOOLTIP_EVENTS);
    }

    /**
     * Update popup visibility to server
     * 
     * @param visibility
     */
    private void popupVisibilityChange(boolean visible) {
        fireEvent(new PopupVisibilityChangedEvent(visible));
    }

    private void valueChange(String value) {
        fireEvent(new StyleCalendarFieldValueChanged(value));
    }

    private void preparePopup(final CustomPopup popup) {
        popup.setVisible(false);
        popup.show();
    }

    /**
     * Determines the correct position for a popup and displays the popup at
     * that position.
     * 
     * By default, the popup is shown centered relative to its host component,
     * ensuring it is visible on the screen if possible.
     * 
     * Can be overridden to customize the popup position.
     * 
     * @param popup
     */
    protected void showPopup(final CustomPopup popup) {
        popup.showRelativeTo(this);

        popup.setVisible(true);
    }

    /**
     * Make sure that we remove the popup when the main widget is removed.
     * 
     * @see com.google.gwt.user.client.ui.Widget#onUnload()
     */
    @Override
    protected void onDetach() {
        popup.hide();
        super.onDetach();
    }

    /**
     * This class is only protected to enable overriding showPopup, and is
     * currently not intended to be extended or otherwise used directly. Its API
     * (other than it being a VOverlay) is to be considered private and
     * potentially subject to change.
     */
    protected class CustomPopup extends VOverlay {

        private Widget popupComponentWidget = null;

        private boolean hiding = false;

        public CustomPopup() {
            super(true, false, true); // autoHide, not modal, dropshadow
            setOwner(VStyleCalendarField.this);
        }

        @Override
        public void hide(boolean autoClosed) {
            hiding = true;
            syncChildren();
            if (popupComponentWidget != null) {
                remove(popupComponentWidget);
            }
            super.hide(autoClosed);
        }

        @Override
        public void show() {
            hiding = false;
            super.show();
        }

        /**
         * Try to sync all known active child widgets to server
         */
        public void syncChildren() {
            // Notify children with focus
            if ((popupComponentWidget instanceof Focusable)) {
                ((Focusable) popupComponentWidget).setFocus(false);

            }
        }

        @Override
        public boolean remove(Widget w) {
            popupComponentWidget = null;
            return super.remove(w);
        }

        /*
         * 
         * We need a hack make popup act as a child of VPopupView in Vaadin's
         * component tree, but work in default GWT manner when closing or
         * opening.
         * 
         * (non-Javadoc)
         * 
         * @see com.google.gwt.user.client.ui.Widget#getParent()
         */
        @Override
        public Widget getParent() {
            if (!isAttached() || hiding) {
                return super.getParent();
            } else {
                return VStyleCalendarField.this;
            }
        }

        @Override
        protected void onDetach() {
            super.onDetach();
            hiding = false;
        }

        @Override
        public Element getContainerElement() {
            return super.getContainerElement();
        }

        @Override
        public void updateShadowSizeAndPosition() {
            super.positionOrSizeUpdated();
        }

    }

    @Override
    public Iterator<Widget> iterator() {
        return Collections.<Widget> singletonList(popup).iterator();
    }

    public void setPopupWidget(Widget widget) {
        popup.setWidget(widget);
    }

    public void showPopup() {
        preparePopup(popup);
        showPopup(popup);
    }

    public HandlerRegistration addPopupVisibilityChangedHandler(
            PopupVisibilityChangeHandler handler) {
        return addHandler(handler, PopupVisibilityChangedEvent.getType());
    }

    public void hidePopup() {
        popup.hide();
    }
}
