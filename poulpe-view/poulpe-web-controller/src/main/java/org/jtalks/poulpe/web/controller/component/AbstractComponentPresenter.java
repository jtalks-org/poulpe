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

import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.WindowManager;

/**
 * The abstract class which contains common fields and methods for
 * {@link org.jtalks.poulpe.web.controller.component.items.ItemPresenter ItemPresenter} and {@link ListPresenter}
 * classes.
 * 
 * @author Dmitriy Sukharev
 * @author Vyacheslav Zhivaev
 * 
 */
public abstract class AbstractComponentPresenter {

    /** The service instance to manipulate with stored components. */
    protected ComponentService componentService;

    protected DialogManager dialogManager;
    protected WindowManager windowManager;

    /**
     * Returns the service instance which is used for manipulating with stored components.
     * @return the service instance which is used for manipulating with stored components
     */
    public ComponentService getComponentService() {
        return componentService;
    }

    /**
     * Returns the dialog manager which is used for showing different types of dialog messages.
     * @return the dialog manager which is used for showing different types of dialog messages
     */
    public DialogManager getDialogManager() {
        return dialogManager;
    }

    /**
     * Returns the window manager which is used for controlling windows.
     * 
     * @return the current window manager instance
     */
    public WindowManager getWindowManager() {
        return windowManager;
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
