package org.vaadin.risto.stylecalendar.widgetset.client.ui.field;

import java.util.Iterator;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

//
//import java.util.Iterator;
//import java.util.NoSuchElementException;
//import java.util.Set;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.event.logical.shared.CloseEvent;
//import com.google.gwt.event.logical.shared.CloseHandler;
//import com.google.gwt.event.logical.shared.ValueChangeEvent;
//import com.google.gwt.event.logical.shared.ValueChangeHandler;
//import com.google.gwt.user.client.Element;
//import com.google.gwt.user.client.Event;
//import com.google.gwt.user.client.ui.Focusable;
//import com.google.gwt.user.client.ui.PopupPanel;
//import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.TextBox;
//import com.google.gwt.user.client.ui.Widget;
//import com.vaadin.terminal.gwt.client.ApplicationConnection;
//import com.vaadin.terminal.gwt.client.Paintable;
//import com.vaadin.terminal.gwt.client.RenderInformation.Size;
//import com.vaadin.terminal.gwt.client.RenderSpace;
//import com.vaadin.terminal.gwt.client.UIDL;
//import com.vaadin.terminal.gwt.client.Util;
//import com.vaadin.terminal.gwt.client.VCaption;
//import com.vaadin.terminal.gwt.client.VCaptionWrapper;
//import com.vaadin.terminal.gwt.client.VTooltip;
//import com.vaadin.terminal.gwt.client.ui.VOverlay;
//
public class VStyleCalendarField extends TextBox implements Iterable<Widget> {

    @Override
    public Iterator<Widget> iterator() {
        return new Iterator<Widget>() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Widget next() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void remove() {
                // TODO Auto-generated method stub

            }
        };
    }
    //
    // public static final String CLASSNAME = "v-stylecalendarfield";
    //
    // /** For server-client communication */
    // private String uidlId;
    // private ApplicationConnection client;
    //
    // /** This variable helps to communicate popup visibility to the server */
    // private boolean hostPopupVisible;
    //
    // private final CustomPopup popup;
    //
    // /**
    // * loading constructor
    // */
    // public VStyleCalendarField() {
    // super();
    //
    // setStyleName(CLASSNAME);
    //
    // addValueChangeHandler(new ValueChangeHandler<String>() {
    //
    // @Override
    // public void onValueChange(ValueChangeEvent<String> event) {
    // updateState(event.getValue());
    // }
    // });
    //
    // popup = new CustomPopup();
    // popup.setStyleName(CLASSNAME + "-popup");
    // popup.addStyleName("v-popupview-popup"); // to get font-size etc. from
    // // the
    // // Vaadin theme
    //
    // // When we click to open the popup...
    // addClickHandler(new ClickHandler() {
    // @Override
    // public void onClick(ClickEvent event) {
    // if (!hostPopupVisible) {
    // updateState(true);
    // }
    // }
    // });
    //
    // // ..and when we close it
    // popup.addCloseHandler(new CloseHandler<PopupPanel>() {
    // @Override
    // public void onClose(CloseEvent<PopupPanel> event) {
    // updateState(false);
    // }
    // });
    //
    // popup.setAnimationEnabled(true);
    // popup.addAutoHidePartner(getElement());
    // sinkEvents(VTooltip.TOOLTIP_EVENTS);
    // }
    //
    // /**
    // *
    // *
    // * @see
    // com.vaadin.terminal.gwt.client.Paintable#updateFromUIDL(com.vaadin.terminal.gwt.client.UIDL,
    // * com.vaadin.terminal.gwt.client.ApplicationConnection)
    // */
    // @Override
    // public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
    // // This call should be made first. Ensure correct implementation,
    // // and don't let the containing layout manage caption.
    // if (client.updateComponent(this, uidl, true)) {
    // return;
    // }
    // // These are for future server connections
    // this.client = client;
    // uidlId = uidl.getId();
    //
    // hostPopupVisible = uidl.getBooleanVariable("showPopup");
    //
    // String value = uidl.getStringAttribute("value");
    // if (!value.equals(getValue())) {
    // setValue(value);
    // }
    //
    // // Render the popup if visible and show it.
    // if (hostPopupVisible) {
    // UIDL popupUIDL = uidl.getChildUIDL(0);
    //
    // // showPopupOnTop(popup, hostReference);
    // preparePopup(popup);
    // popup.updateFromUIDL(popupUIDL, client);
    // showPopup(popup);
    //
    // // The popup shouldn't be visible, try to hide it.
    // } else {
    // popup.hide();
    // }
    // }// updateFromUIDL
    //
    // /**
    // * Update popup visibility to server
    // *
    // * @param visibility
    // */
    // private void updateState(boolean visible) {
    // // If we know the server connection
    // // then update the current situation
    // if (uidlId != null && client != null && isAttached()) {
    // client.updateVariable(uidlId, "showPopup", visible, true);
    // }
    // }
    //
    // private void updateState(String value) {
    // // If we know the server connection
    // // then update the current situation
    // if (uidlId != null && client != null && isAttached()) {
    // client.updateVariable(uidlId, "value", value, true);
    // }
    // }
    //
    // private void preparePopup(final CustomPopup popup) {
    // popup.setVisible(false);
    // popup.show();
    // }
    //
    // /**
    // * Determines the correct position for a popup and displays the popup at
    // * that position.
    // *
    // * By default, the popup is shown centered relative to its host component,
    // * ensuring it is visible on the screen if possible.
    // *
    // * Can be overridden to customize the popup position.
    // *
    // * @param popup
    // */
    // protected void showPopup(final CustomPopup popup) {
    // popup.showRelativeTo(this);
    //
    // popup.setVisible(true);
    // }
    //
    // /**
    // * Make sure that we remove the popup when the main widget is removed.
    // *
    // * @see com.google.gwt.user.client.ui.Widget#onUnload()
    // */
    // @Override
    // protected void onDetach() {
    // popup.hide();
    // super.onDetach();
    // }
    //
    // /**
    // * This class is only protected to enable overriding showPopup, and is
    // * currently not intended to be extended or otherwise used directly. Its
    // API
    // * (other than it being a VOverlay) is to be considered private and
    // * potentially subject to change.
    // */
    // protected class CustomPopup extends VOverlay {
    //
    // private Paintable popupComponentPaintable = null;
    // private Widget popupComponentWidget = null;
    // private VCaptionWrapper captionWrapper = null;
    //
    // private boolean hiding = false;
    //
    // public CustomPopup() {
    // super(true, false, true); // autoHide, not modal, dropshadow
    // }
    //
    // @Override
    // public void hide(boolean autoClosed) {
    // hiding = true;
    // syncChildren();
    // unregisterPaintables();
    // if (popupComponentWidget != null) {
    // remove(popupComponentWidget);
    // }
    // super.hide(autoClosed);
    // }
    //
    // @Override
    // public void show() {
    // hiding = false;
    // super.show();
    // }
    //
    // /**
    // * Try to sync all known active child widgets to server
    // */
    // public void syncChildren() {
    // // Notify children with focus
    // if ((popupComponentWidget instanceof Focusable)) {
    // ((Focusable) popupComponentWidget).setFocus(false);
    //
    // }
    // }
    //
    // @Override
    // public boolean remove(Widget w) {
    //
    // popupComponentPaintable = null;
    // popupComponentWidget = null;
    // captionWrapper = null;
    //
    // return super.remove(w);
    // }
    //
    // public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
    //
    // Paintable newPopupComponent = client.getPaintable(uidl
    // .getChildUIDL(0));
    //
    // if (newPopupComponent != popupComponentPaintable) {
    //
    // setWidget((Widget) newPopupComponent);
    //
    // popupComponentWidget = (Widget) newPopupComponent;
    //
    // popupComponentPaintable = newPopupComponent;
    // }
    //
    // popupComponentPaintable
    // .updateFromUIDL(uidl.getChildUIDL(0), client);
    //
    // }
    //
    // public void unregisterPaintables() {
    // if (popupComponentPaintable != null) {
    // client.unregisterPaintable(popupComponentPaintable);
    // }
    // }
    //
    // /*
    // *
    // * We need a hack make popup act as a child of VPopupView in Vaadin's
    // * component tree, but work in default GWT manner when closing or
    // * opening.
    // *
    // * (non-Javadoc)
    // *
    // * @see com.google.gwt.user.client.ui.Widget#getParent()
    // */
    // @Override
    // public Widget getParent() {
    // if (!isAttached() || hiding) {
    // return super.getParent();
    // } else {
    // return VStyleCalendarField.this;
    // }
    // }
    //
    // @Override
    // protected void onDetach() {
    // super.onDetach();
    // hiding = false;
    // }
    //
    // @Override
    // public Element getContainerElement() {
    // return super.getContainerElement();
    // }
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // * com.vaadin.terminal.gwt.client.ui.VOverlay#updateShadowSizeAndPosition
    // * ()
    // */
    // @Override
    // public void updateShadowSizeAndPosition() {
    // super.updateShadowSizeAndPosition();
    // }
    //
    // }// class CustomPopup
    //
    // // Container methods
    //
    // @Override
    // public RenderSpace getAllocatedSpace(Widget child) {
    // Size popupExtra = calculatePopupExtra();
    //
    // return new RenderSpace(RootPanel.get().getOffsetWidth()
    // - popupExtra.getWidth(), RootPanel.get().getOffsetHeight()
    // - popupExtra.getHeight());
    // }
    //
    // /**
    // * Calculate extra space taken by the popup decorations
    // *
    // * @return
    // */
    // protected Size calculatePopupExtra() {
    // Element pe = popup.getElement();
    // Element ipe = popup.getContainerElement();
    //
    // // border + padding
    // int width = Util.getRequiredWidth(pe) - Util.getRequiredWidth(ipe);
    // int height = Util.getRequiredHeight(pe) - Util.getRequiredHeight(ipe);
    //
    // return new Size(width, height);
    // }
    //
    // @Override
    // public boolean hasChildComponent(Widget component) {
    // if (popup.popupComponentWidget != null) {
    // return popup.popupComponentWidget == component;
    // } else {
    // return false;
    // }
    // }
    //
    // @Override
    // public void replaceChildComponent(Widget oldComponent, Widget
    // newComponent) {
    // popup.setWidget(newComponent);
    // popup.popupComponentWidget = newComponent;
    // }
    //
    // @Override
    // public boolean requestLayout(Set<Paintable> child) {
    // popup.updateShadowSizeAndPosition();
    // return true;
    // }
    //
    // @Override
    // public void updateCaption(Paintable component, UIDL uidl) {
    // if (VCaption.isNeeded(uidl)) {
    // if (popup.captionWrapper != null) {
    // popup.captionWrapper.updateCaption(uidl);
    // } else {
    // popup.captionWrapper = new VCaptionWrapper(component, client);
    // popup.setWidget(popup.captionWrapper);
    // popup.captionWrapper.updateCaption(uidl);
    // }
    // } else {
    // if (popup.captionWrapper != null) {
    // popup.setWidget(popup.popupComponentWidget);
    // }
    // }
    //
    // popup.popupComponentWidget = (Widget) component;
    // popup.popupComponentPaintable = component;
    // }
    //
    // @Override
    // public void onBrowserEvent(Event event) {
    // super.onBrowserEvent(event);
    // if (client != null) {
    // client.handleTooltipEvent(event, this);
    // }
    // }
    //
    // @Override
    // public Iterator<Widget> iterator() {
    // return new Iterator<Widget>() {
    //
    // int pos = 0;
    //
    // @Override
    // public boolean hasNext() {
    // // There is a child widget only if next() has not been called.
    // return (pos == 0);
    // }
    //
    // @Override
    // public Widget next() {
    // // Next can be called only once to return the popup.
    // if (pos != 0) {
    // throw new NoSuchElementException();
    // }
    // pos++;
    // return popup;
    // }
    //
    // @Override
    // public void remove() {
    // throw new UnsupportedOperationException();
    // }
    //
    // };
    // }
    //
}// class VStyleCalendarField
