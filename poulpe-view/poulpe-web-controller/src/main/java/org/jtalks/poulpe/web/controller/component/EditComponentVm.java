/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.web.controller.component;

import org.hibernate.validator.constraints.NotEmpty;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.validation.ValidationException;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel class for EditComponent View
 *
 * @author Vahluev Vyacheslav
 */
public class EditComponentVm {
    public static final String EDIT_COMPONENT_LOCATION = "components/edit_comp.zul";
    public static final String EMPTY_TITLE = "component.error.title_shouldnt_be_empty";
    public static final String EMPTY_NAME = "component.error.name_shouldnt_be_empty";
    public static final String ITEM_ALREADY_EXISTS = "item.already.exist";
    private static final String COMPONENT_NAME_PROP = "componentName", NAME_PROP = "name", CAPTION_PROP = "caption",
            DESCRIPTION_PROP = "description", POST_PREVIEW_SIZE_PROP = "postPreviewSize",
            SESSION_TIMEOUT_PROP = "sessionTimeout", VALIDATION_MESSAGES_PROP = "validationMessages",
            IS_JCOMMUNE ="jcommune";

    /**
     * Current component we are working with
     */
    private Component currentComponent = (Component) Executions.getCurrent().getDesktop()
            .getAttribute("componentToEdit");

    /**
     * The name of the component
     */
    private String name;

    /**
     * The description of the component
     */
    private String description;

    /**
     * The name of the component
     */
    private String componentName;

    /**
     * Type of the component
     */
    private String componentType;
    /**
     * The caption of the forum
     */
    private String caption;
    /**
     * The post preview size of the forum
     */
    private String postPreviewSize;

    private String sessionTimeout;

    private boolean jcommune;

     /**
     * Web-form validation messages
     */
    private Map<String, String> validationMessages = new HashMap<String, String>();

    private ComponentService componentService;
    private DialogManager dialogManager;
    private WindowManager windowManager;

    /**
     * Opens window for editing component.
     *
     * @param windowManager The object which is responsible for creation and closing application windows
     * @param component     component for editing
     */
    public static void openWindowForEdit(WindowManager windowManager, Component component) {
        Executions.getCurrent().getDesktop().setAttribute("componentToEdit", component);
        windowManager.open(EDIT_COMPONENT_LOCATION);
    }

    /**
     * Sets the service instance which is used for manipulating with stored components.
     *
     * @param componentService the new value of the service instance
     */
    public final void setComponentService(ComponentService componentService) {
        this.componentService = componentService;
    }

    /**
     * Sets the dialog manager which is used for showing different types of dialog messages.
     *
     * @param dialogManager the new value of the dialog manager
     */
    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    /**
     * Sets window manager.
     *
     * @param windowManager the new window manager
     */
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    // constructor

    /**
     * Default constructor. Inits the data on the form.
     *
     * @param componentService service we use to access components
     */
    public EditComponentVm(@Nonnull ComponentService componentService) {
        this.setComponentService(componentService);
        initData();
    }

    /**
     * Inits the data on the form.
     */
    @NotifyChange
            ({COMPONENT_NAME_PROP, NAME_PROP, DESCRIPTION_PROP, CAPTION_PROP,
                    POST_PREVIEW_SIZE_PROP, SESSION_TIMEOUT_PROP, IS_JCOMMUNE})
    public final void initData() {
        currentComponent = getSelectedComponent();

        if (currentComponent.getComponentType().equals(ComponentType.FORUM)) {
            componentType = "jcommune";
            jcommune = true;
            name = valueOf(currentComponent.getProperty(componentType + ".name"));
            caption = valueOf(currentComponent.getProperty(componentType + ".caption"));
            postPreviewSize = valueOf(currentComponent.getProperty(componentType + ".postPreviewSize"));
            sessionTimeout = valueOf(currentComponent.getProperty(componentType + ".session_timeout"));
        } else {
            componentType = currentComponent.getComponentType().toString();
            jcommune = false;
            name = "name";
            caption = "caption";
            postPreviewSize = "10";
            sessionTimeout = "10";
        }
        componentName = valueOf(currentComponent.getName());
        description = valueOf(currentComponent.getDescription());

    }

    /**
     * Returns selected component.
     *
     * @return current selected component.
     */
    public Component getSelectedComponent() {
        return (Component) Executions.getCurrent().getDesktop().getAttribute("componentToEdit");
    }

    // service functions

    /**
     * Returns all components.
     *
     * @return the list of the components
     */
    public List<Component> getComponents() {
        return componentService.getAll();
    }

    public boolean isJcommune() {
        return jcommune;
    }

    // commands

    /**
     * Saves a component. Shows validation messages, if something is wrong
     */
    @Command()
    @NotifyChange({COMPONENT_NAME_PROP, NAME_PROP, DESCRIPTION_PROP, CAPTION_PROP, POST_PREVIEW_SIZE_PROP,
            SESSION_TIMEOUT_PROP})
    public void save() {
        boolean correct = true;
        validationMessages.clear();

        if (checkCorrect()) {
            currentComponent.setName(componentName);
            currentComponent.setDescription(description);
            if(jcommune){
            currentComponent.setProperty(componentType + ".name", name);
            currentComponent.setProperty(componentType + ".caption", caption);
            currentComponent.setProperty(componentType + ".postPreviewSize", postPreviewSize);
            currentComponent.setProperty(componentType + ".session_timeout", sessionTimeout);
            }

            try {
                componentService.saveComponent(currentComponent);
            } catch (ValidationException e) {
                validationMessages.put("componentName", Labels.getLabel(ITEM_ALREADY_EXISTS));
                correct = false;
            }

            if (correct) {
                validationMessages.clear();
                Executions.sendRedirect("");
            }
        }
    }

    /**
     * Cancels all the actions
     */
    @Command()
    @NotifyChange({COMPONENT_NAME_PROP, NAME_PROP, DESCRIPTION_PROP, CAPTION_PROP, POST_PREVIEW_SIZE_PROP,
            VALIDATION_MESSAGES_PROP, SESSION_TIMEOUT_PROP})
    public void cancel() {
        initData();
        validationMessages.clear();
        Executions.sendRedirect("");
    }

    // helpers

    /**
     * Returns string value of the field or empty string if string is null
     *
     * @param value value of the string
     * @return string value of the field or empty string if string is null
     */
    public String valueOf(String value) {
        return (value == null) ? "" : value;
    }

    /**
     * Check if input data is correct
     *
     * @return true if input is correct, else otherwise
     */
    public boolean checkCorrect() {
        boolean correct = true;

        if (name == null || name.equals("")) {
            validationMessages.put("name", Labels.getLabel(EMPTY_TITLE));
            correct = false;
        }

        if (componentName == null || componentName.equals("")) {
            validationMessages.put("componentName", Labels.getLabel(EMPTY_NAME));
            correct = false;
        }

        return correct;
    }

    // getters & setters for web-form

    /**
     * Returns the title for current component
     *
     * @return name value from web-form
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the title for the current component
     *
     * @param name value to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description for the current component
     *
     * @return description value from web-form
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the current component
     *
     * @param description to set on web-form
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the name of the current component
     *
     * @return component name value from web-form
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Sets the name for current component
     *
     * @param componentName new component name value on web-form
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * Returns validation messages for data input
     *
     * @return validation messages
     */
    public Map<String, String> getValidationMessages() {
        return validationMessages;
    }

    // getter and setter for current component we edit

    /**
     * Gets the current component we edit
     *
     * @return current component
     */
    public Component getCurrentComponent() {
        return currentComponent;
    }

    /**
     * Sets the current component we edit
     *
     * @param currentComponent - to set
     */
    public void setCurrentComponent(Component currentComponent) {
        this.currentComponent = currentComponent;
    }

    /**
     * Returns caption from the web-form
     *
     * @return caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the current caption
     *
     * @param caption - to set on web-form
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Returns post preview size from the web-form
     *
     * @return post preview size
     */
    public String getPostPreviewSize() {
        return postPreviewSize;
    }

    /**
     * Sets the current component we edit
     *
     * @param postPreviewSize - to set on web-form
     */
    public void setPostPreviewSize(String postPreviewSize) {
        this.postPreviewSize = postPreviewSize;
    }

    /**
     * Returns session timeout value from the web-form
     *
     * @return session timeout
     */
    @NotEmpty
    public String getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Sets session timeout
     *
     * @param sessionTimeout - to set on web-form
     */
    public void setSessionTimeout(String sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

}