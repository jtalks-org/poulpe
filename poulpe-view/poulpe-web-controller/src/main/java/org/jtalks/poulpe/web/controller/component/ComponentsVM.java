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

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;


/**
 * The class which manages actions and represents information about components
 * displayed in administrator panel.
 * 
 * @author Vermut
 */
public class ComponentsVM {

    private static final String EDIT_COMPONENT_LOCATION = "components/edit_comp.zul";
    
    private Component selected;
    private boolean editWindowVisible;
    private boolean canCreateNewComponent;
    private List<Component> componentList;
    private List<ComponentType> availableComponentTypes;
    
    // List of injectable properties
    private ComponentService componentService;
    private DialogManager dialogManager;
    private WindowManager windowManager;
    
    /**
     * Inits the data on the form.
     */
    @Init
    public void init() {
    	updateListComponentsData();
    }
    
    /**
     * Creates new TopicType and adds it on form
     */
    @Command @NotifyChange({"editWindowVisible", "availableComponentTypes", "selectedComponentType"})
    public void newComponent() {
        selected = new Component();
        selected.setComponentType(availableComponentTypes.get(0)); // first from avalable types
        selected.setDescription("Description of new component");
        selected.setName("Name of new component");
        editWindowVisible = true;
    }
    
    /**
     * Edits the selected component.
     * 
     * @param component selected component
     */
    @Command @NotifyChange({"selected", "editWindowVisible", "availableComponentTypes", "selectedComponentType"})
    public void editComponent(@BindingParam("component") Component component) {
    	selected = component;
    	availableComponentTypes.add(selected.getComponentType());
    	editWindowVisible = true;
    }
    
    /**
     * Deletes the selected component
     */
    @Command
    public void deleteComponent() {
        DialogManager.Performable dc = new DialogManager.Performable() {
    		/** {@inheritDoc} */
            @Override
            public void execute() {
                componentService.deleteComponent(selected);
                updateListComponentsData();
                selected = null;
                // Because confirmation needed, we need to send notification event programmatically
                BindUtils.postNotifyChange(null, null, this, "selected");
                BindUtils.postNotifyChange(null, null, this, "componentList");
            	BindUtils.postNotifyChange(null, null, this, "canCreateNewComponent");
            }};
        dialogManager.confirmDeletion(selected.getName(), dc);
    }
    
    /**
     * Shows a component edit window
     */
    @Command
    public void configureComponent () {
        Executions.getCurrent().getDesktop().setAttribute("componentToEdit", selected);
        windowManager.open(EDIT_COMPONENT_LOCATION);
    }
    
    /** Saves the created or edited component in component list. */
    @Command @NotifyChange({"componentList", "selected", "canCreateNewComponent", "editWindowVisible"})
    public void saveComponent() {
        componentService.saveComponent(selected);
        editWindowVisible = false;
        updateListComponentsData();
    }
    
    /**
	 * Event which happen when user cansel editing of component.
	 */
    @Command @NotifyChange({"selected", "editWindowVisible"})
	public void cancelEdit() {
		selected = null;
		editWindowVisible = false;
		updateListComponentsData();
	}
    
    /**
     * Returns the list of all components.
     * 
     * @return list of components
     */
    public List<Component> getComponentList() {
		return componentList;
	}
    
    /**
     * @return <code>true</code> if the window for editing component should be visible, unless false.
     * 
     */
    public boolean isEditWindowVisible() {
        return editWindowVisible;
    }
    
    /**
     * Sets the selected component from the list which displays components.
     * 
     * @param selected {@link org.jtalks.common.model.entity.Component}
     */
    public void setSelected(Component selected) {
		this.selected = selected;
	}
    
    /**
     * Returns the component which currently is edited, created or selected in a list which displays components.
     * 
     * @return {@link org.jtalks.common.model.entity.Component}
     */
    public Component getSelected() {
		return selected;
    }
    
    /**
     * @return <code>true</code> only if new component can be created, <code>false</code> in other cases.
     */
    public boolean isCanCreateNewComponent() {
		return canCreateNewComponent;
	}
    
    /**
     * Returns all available component types which available to be set to editable component.
     * 
     * @return list of available component types
     */
    public List<ComponentType> getAvailableComponentTypes() {
		return availableComponentTypes;
	}
	
    private void updateListComponentsData() {
    	availableComponentTypes = new ArrayList(componentService.getAvailableTypes());
    	componentList = componentService.getAll();
    	canCreateNewComponent = !availableComponentTypes.isEmpty();
    }

	/**
	 * Sets the service instance which is used for manipulating with stored components.
	 * @param componentService the new value of the service instance
	 */
	public void setComponentService(ComponentService componentService) {
	    this.componentService = componentService;
	}
	
	/**
	 * Sets the dialog manager which is used for showing different types of dialog messages.
	 * @param dialogManager the new value of the dialog manager
	 */
	public void setDialogManager(DialogManager dialogManager) {
	    this.dialogManager = dialogManager;
	}
	
	/**
	 * Sets window manager.
	 * @param windowManager the new window manager
	 */
	public void setWindowManager(WindowManager windowManager) {
	    this.windowManager = windowManager;
	}
    
}
