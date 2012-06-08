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

import static org.apache.commons.lang3.Validate.notNull;

import javax.annotation.Nonnull;

import org.jtalks.common.model.entity.Component;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.Command;

/**
 * ViewModel class for EditComponent View
 *
 * @author Vahluev Vyacheslav
 * @author Kazancev Leonid
 */
public class EditComponentVm {
    private static final String EDIT_COMPONENT_LOCATION = "/WEB-INF/pages/component/edit_comp.zul",
            COMPONENTS_WINDOW = "components.zul";
            
    private final ComponentService componentService;
    private final Component component;

    private WindowManager windowManager;

    /**
     * Opens window for editing component.
     *
     * @param windowManager The object which is responsible for creation and closing application windows
     */
    // TODO: get rid of this static method
    public static void openWindowForEdit(WindowManager windowManager) {
        windowManager.open(EDIT_COMPONENT_LOCATION);
    }

    // constructor

    public EditComponentVm(@Nonnull ComponentService componentService, @Nonnull SelectedEntity<Component> component) {
        this.componentService = componentService;
        this.component = notNull(component.getEntity());
    }

    /**
     * Saves a component. Shows validation messages, if something is wrong
     */
    @Command
    public void save() {
        componentService.saveComponent(component);
        switchToComponentsWindow();
    }

    /**
     * Cancels all the actions
     */
    @Command
    public void cancel() {
        switchToComponentsWindow();
    }

    /**
     * Opens component view window.
     */
    private void switchToComponentsWindow() {
        windowManager.open(COMPONENTS_WINDOW);
    }

    /**
     * @param windowManager the new window manager
     */
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public Component getComponent() {
        return component;
    }
}