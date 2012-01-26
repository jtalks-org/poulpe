package org.jtalks.poulpe.web.controller.component;

import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.validation.ValidationException;
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
 *
 */
public class EditCompViewModel extends AbstractComponentPresenter {

    public static final String EMPTY_TITLE = "component.error.title_shouldnt_be_empty";
    public static final String EMPTY_NAME = "component.error.name_shouldnt_be_empty";
    public static final String ITEM_ALREADY_EXISTS = "item.already.exist";

    /**
     * Current component we are working with
     */
    private Component currentComponent =
            (Component) Executions.getCurrent().getDesktop().getAttribute("componentToEdit");

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
     *  Type of the component
     */
    private String componentType;
    /**
     * The caption of the forum
     */
    private String caption;
    /**
     * The post preview size of the forum
     */
    private String post_preview_size;

    /**
     * Web-form validation messages
     */
    private Map<String, String> validationMessages =
            new HashMap<String,String>();

    //    constructor
    /**
     * Default constructor. Inits the data on the form.
     * @param componentService service we use to access components
     */
    public EditCompViewModel(@Nonnull ComponentService componentService) {
        this.setComponentService(componentService);
        initData();
    }

    /**
     * Inits the data on the form.
     */
    @NotifyChange({ "componentName", "name", "description", "caption", "post_preview_size" })
    public void initData() {
        currentComponent = (Component) Executions.getCurrent().getDesktop().getAttribute("componentToEdit");

        if (currentComponent.getComponentType().equals(ComponentType.FORUM)) {
            componentType = "jcommune";
        } else {
            componentType = "antarcticle";
        }
        componentName = valueOf(currentComponent.getName());
        name = valueOf(currentComponent.getProperty(componentType + ".name"));
        description = valueOf(currentComponent.getDescription());
        caption = valueOf(currentComponent.getProperty(componentType + ".caption"));
        post_preview_size = valueOf(currentComponent.getProperty(componentType + ".post_preview_size"));
    }

    //    service functions
    /**
     * Returns all components.
     * @return the list of the components
     */
    public List<Component> getComponents() {
        return componentService.getAll();
    }

    //    commands
    /**
     * Saves a component. Shows validation messages, if
     * something is wrong
     */
    @Command()
    @NotifyChange({ "componentName", "name", "description", "caption", "post_preview_size" })
    public void save() {
        boolean correct = true;
        validationMessages.clear();

        if (checkCorrect()) {
            currentComponent.setName(componentName);
            currentComponent.setDescription(description);
            currentComponent.setProperty(componentType + ".name", name);
            currentComponent.setProperty(componentType + ".caption", caption);
            currentComponent.setProperty(componentType + ".post_preview_size", post_preview_size);

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
    @NotifyChange({ "componentName", "name", "description", "caption", "post_preview_size", "validationMessages" })
    public void cancel() {
        initData();
        validationMessages.clear();
        Executions.sendRedirect("");
    }

    //    helpers
    /**
     * Returns string value of the field or
     * empty string if string is null
     *
     * @param value value of the string
     * @return string value of the field or
     * empty string if string is null
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

        if (name.equals("") || name == null) {
            validationMessages.put("name", Labels.getLabel(EMPTY_TITLE));
            correct = false;
        }

        if (componentName.equals("") || componentName == null) {
            validationMessages.put("componentName", Labels.getLabel(EMPTY_NAME));
            correct = false;
        }

        return correct;
    }


    //    getters & setters for web-form
    /**
     * Returns the title for current component
     * @return name value from web-form
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the title for the current component
     * @param name value to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description for the current component
     * @return description value from web-form
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the current component
     * @param description to set on web-form
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the name of the current component
     * @return component name value from web-form
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Sets the name for current component
     * @param componentName new component name value on web-form
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * Returns validation messages for data input
     * @return validation messages
     */
    public Map<String, String> getValidationMessages() {
        return validationMessages;
    }

//    getter and setter for current component we edit
    /**
     * Gets the current component we edit
     * @return current component
     */
    public Component getCurrentComponent() {
        return currentComponent;
    }

    /**
     * Sets the current component we edit
     * @param currentComponent - to set
     */
    public void setCurrentComponent(Component currentComponent) {
        this.currentComponent = currentComponent;
    }

    /**
     * Returns caption from the web-form
     * @return caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the current caption
     * @param caption - to set on web-form
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Returns post preview size from the web-form
     * @return post preview size
     */
    public String getPost_preview_size() {
        return post_preview_size;
    }

    /**
     * Sets the current component we edit
     * @param post_preview_size- to set on web-form
     */
    public void setPost_preview_size(String post_preview_size) {
        this.post_preview_size = post_preview_size;
    }

}
