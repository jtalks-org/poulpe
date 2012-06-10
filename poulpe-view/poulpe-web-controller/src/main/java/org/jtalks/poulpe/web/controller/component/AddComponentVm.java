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

import java.util.List;

import org.jtalks.poulpe.model.entity.BaseComponent;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import com.google.common.collect.Lists;

/**
 * 
 * @author Alexey Grigorev
 */
public class AddComponentVm {
    static final String ADD_COMPONENT_LOCATION = "/WEB-INF/pages/component/add_comp.zul",
            COMPONENTS_WINDOW = "components.zul";
    
    private final ComponentService componentService;
    private final WindowManager windowManager;
    
    public AddComponentVm(ComponentService componentService, WindowManager windowManager) {
        this.componentService = componentService;
        this.windowManager = windowManager;
    }
    
    @Command
    public void createComponent(@BindingParam(value = "title") String title,
            @BindingParam(value = "description") String description,
            @BindingParam(value = "componentType") ComponentType componentType) {

        // TODO: move to service? 
        BaseComponent baseComponent = componentService.baseComponentFor(componentType);
        Component component = baseComponent.newComponent(title, description);
        componentService.saveComponent(component);
        
        switchToComponentsWindow();
    }
    
    @Command
    public void cancelEdit() {
        switchToComponentsWindow();
    }
    
    public List<ComponentType> getAvailableComponentTypes() {
        return Lists.newArrayList(componentService.getAvailableTypes());
    }
    
    private void switchToComponentsWindow() {
        windowManager.open(COMPONENTS_WINDOW);
    }

    // TODO: find out how to get rid of it
    public static void openWindowForAdding(WindowManager windowManager) {
        windowManager.open(ADD_COMPONENT_LOCATION);
    }
    
}
