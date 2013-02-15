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
package org.jtalks.poulpe.web.controller.component.dialogs;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.exceptions.EntityUniqueConstraintException;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.component.ComponentList;
import org.jtalks.poulpe.web.controller.component.ComponentsVm;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Messagebox;
import static org.zkoss.util.resource.Labels.getLabel;

import java.util.List;

/**
 * View-Model for adding a selectedEntity. Shown from {@link org.jtalks.poulpe.web.controller.component.ComponentsVm}.
 *
 * @author Alexey Grigorev
 * @author Leonid Kazantcev
 */
public class AddComponentVm {
    public static final String ADD_COMPONENT_LOCATION = "/WEB-INF/pages/component/add_comp.zul";
    public static final String COMPONENT_CREATING_FAILED = "component.error.creating_failed";
    public static final String COMPONENT_CREATING_FAILED_TITLE = "component.error.creating_failed_title";

    private final ComponentService componentService;
    private final WindowManager windowManager;
    private final Component component;
    private final ComponentList componentList;

    /**
     * Constructs new dialog for creating components. It should be used as a prototype, new object for every new
     * selectedEntity.
     *
     * @param componentService service for saving Component
     * @param windowManager    object for opening and closing application windows
     * @param selectedEntity selectedEntity holder of {@link org.jtalks.poulpe.model.entity.Component} instance, witch will be saved to data base,
     * @param componentList
     */
    public AddComponentVm(ComponentService componentService, WindowManager windowManager,
                          SelectedEntity<Component> selectedEntity, ComponentList componentList) {
        this.componentService = componentService;
        this.windowManager = windowManager;
        this.componentList = componentList;
        component = selectedEntity.getEntity();
    }

    /**
     * Saves component, with params seted up at dialog to data base.
     */
    @Command
    public void createComponent() {
        try {
            componentService.addComponent(component);
            componentList.add(component);
        } catch (EntityUniqueConstraintException ex) {
            componentList.renew(componentService.getAll());
            Messagebox.show(getLabel(COMPONENT_CREATING_FAILED),
                            getLabel(COMPONENT_CREATING_FAILED_TITLE), Messagebox.OK, Messagebox.ERROR);
        } finally {
            switchToComponentsWindow();
        }
    }

    /**
     * Cancels edit and switches to components window.
     */
    @Command
    public void cancelEdit() {
        switchToComponentsWindow();
    }

    /**
     * @return list of all available selectedEntity types
     */
    public List<ComponentType> getAvailableComponentTypes() {
        return Lists.newArrayList(componentService.getAvailableTypes());
    }

    /**
     * Opens components view, closing current dialog.
     */
    private void switchToComponentsWindow() {
        ComponentsVm.show(windowManager);
    }

    /**
     * With given window manager, opens this dialog from {@link ComponentsVm}.
     *
     * @param windowManager to be used for opening the dialog
     */
    public static void openWindowForAdding(WindowManager windowManager) {
        windowManager.open(ADD_COMPONENT_LOCATION);
    }

    /**
     * @return selectedEntity for validation
     */
    public Component getComponent() {
        return component;
    }

}
