package org.jtalks.poulpe.web.controller.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.validation.ValidationException;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

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
    private Component currentComponent = (Component) Executions.getCurrent().getDesktop().getAttribute("componentToEdit");;

    /**
     * The title of the component
     */
    private String title;

    /**
     * The description of the component
     */
    private String description;

    /**
     * The name of the component
     */
    private String name;

    /**
     *  Type of the component
     */
    private String componentType;

    /**
     * Web-form validation messages
     */
    Map<String, String> validationMessages =
            new HashMap<String,String>();

//	constructor
    /**
     * Default constructor. Inits the data on the form.
     * @param componentService
     */
    public EditCompViewModel(@Nonnull ComponentService componentService) {
        this.setComponentService(componentService);
        initData();
    }

    /**
     * Inits the data on the form.
     */
    @NotifyChange({ "name", "title", "description" })
    public void initData() {
        currentComponent = (Component) Executions.getCurrent().getDesktop().getAttribute("componentToEdit");

        if (currentComponent.getComponentType().equals(ComponentType.FORUM)) {
            componentType = "jcommune";
        } else {
            componentType = "antarcticle";
        }
        name = valueOf(currentComponent.getName());
        title = valueOf(currentComponent.getProperty(componentType + ".title"));
        description = valueOf(currentComponent.getDescription());
    }

//	service functions	
    /**
     * Returns all components.
     * @return the list of the components
     */
    public List<Component> getComponents() {
        return componentService.getAll();
    }

//	commands
    /**
     * Saves a component. Shows validation messages, if
     * something is wrong
     */
    @Command()
    @NotifyChange({ "name", "title", "description", "validationMessages" })
    public void save() {
        boolean correct = true;
        validationMessages.clear();

        if (checkCorrect()) {
            currentComponent.setName(name);
            currentComponent.setDescription(description);
            currentComponent.setProperty(componentType + ".title", title);

            try {
                componentService.saveComponent(currentComponent);
            } catch (ValidationException e) {
                validationMessages.put("name", Labels.getLabel(ITEM_ALREADY_EXISTS));
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
    @NotifyChange({ "name", "title", "description", "validationMessages" })
    public void cancel() {
        initData();
        validationMessages.clear();
        Executions.sendRedirect("");
    }

//	helpers
    /**
     * Returns string value of the field or
     * empty string if string is null
     * @param value
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

        if (title.equals("") || title == null) {
            validationMessages.put("title", Labels.getLabel(EMPTY_TITLE));
            correct = false;
        }

        if (name.equals("") || name == null) {
            validationMessages.put("name", Labels.getLabel(EMPTY_NAME));
            correct = false;
        }

        return correct;
    }


//	getters & setters for web-form
    /**
     * Returns the title for current component
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title for the current component
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description for the current component
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the current component
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the name of the current component
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for current component
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns validation messages for data input
     * @return validation messages
     */
    public Map<String, String> getValidationMessages() {
        return validationMessages;
    }

//	getter and setter for current component we edit
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

}
