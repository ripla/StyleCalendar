package org.vaadin.risto.stylecalendar.widgetset.client;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.risto.stylecalendar.widgetset.client.ui.field.VStyleCalendarField;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.field.event.PopupVisibilityChangeHandler;
import org.vaadin.risto.stylecalendar.widgetset.client.ui.field.event.PopupVisibilityChangedEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.risto.stylecalendar.StyleCalendarField.class)
public class StyleCalendarFieldConnector extends
        AbstractComponentContainerConnector {

    private static final long serialVersionUID = -8816361554030163829L;
    private List<HandlerRegistration> handlerRegistrations;

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

    @Override
    public VStyleCalendarField getWidget() {
        return (VStyleCalendarField) super.getWidget();
    }

    @Override
    protected void init() {
        super.init();
        handlerRegistrations = new ArrayList<HandlerRegistration>();

        final StyleCalendarFieldRpc rpcProxy = RpcProxy.create(
                StyleCalendarFieldRpc.class, this);

        handlerRegistrations.add(getWidget().addPopupVisibilityChangedHandler(
                new PopupVisibilityChangeHandler() {

                    @Override
                    public void handlePopupVisibilityChanged(
                            PopupVisibilityChangedEvent event) {
                        rpcProxy.popupVisibilityChanged(event.isVisible());
                    }
                }));

        handlerRegistrations.add(getWidget().addValueChangeHandler(
                new ValueChangeHandler<String>() {

                    @Override
                    public void onValueChange(ValueChangeEvent<String> event) {
                        rpcProxy.setValue(event.getValue());
                    }
                }));
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {

        getWidget().setValue(getState().getFieldValue());

        if (getState().isShowPopup()) {
            getWidget().showPopup();
        } else {
            getWidget().hidePopup();
        }

        super.onStateChanged(stateChangeEvent);
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        if (getState().isShowPopup()) {
            getWidget().setPopupWidget(
                    ((ComponentConnector) getChildren().get(0)).getWidget());
        } else {
            getWidget().setPopupWidget(null);
        }
    }

}
