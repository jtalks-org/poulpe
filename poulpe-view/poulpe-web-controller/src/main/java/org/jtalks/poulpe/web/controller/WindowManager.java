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
package org.jtalks.poulpe.web.controller;

import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.model.entity.User;
import org.zkoss.zk.ui.Component;

/**
 * The interface for creation and closing application windows.
 * 
 * @author Dmitriy Sukharev
 * @author Vyacheslav Zhivaev
 */
public interface WindowManager {

    /**
     * Creates and shows new window which is responsible for editing components.
     * 
     * @param componentId identifier of the {@link Component} to be edited, or
     * {@code -1L} to create a new one
     * @param listener listener to be invoked after window is closed. It's
     * actually might be the object of any class, writing the implementation of
     * WindowManager you should document what type it has.
     */
    void showEditComponentWindow(long componentId, Object listener);

    /**
     * Closes the {@code window} window and invokes its "onDetach" listener.
     * 
     * @param window the window to be closed
     */
    void closeWindow(Object window);

    /**
     * Opens a window for creating topic types
     * 
     * @param listener edit listener
     */
    void openTopicTypeWindowForCreate(EditListener<TopicType> listener);

    /**
     * Opens a window for editing topic types
     * 
     * @param topicType to edit
     * @param listener edit listener
     */
    void openTopicTypeWindowForEdit(TopicType topicType, EditListener<TopicType> listener);

    /**
     * Opens a window for editing users
     * 
     * @param user to edit
     * @param listener edit listener
     */
    void openUserWindowForEdit(User user, EditListener<User> listener);

    /**
     * Sets work-area component which will be used as parent component for newly
     * created windows.
     * 
     * @param workArea the work area component
     */
    void setWorkArea(Component workArea);

    /**
     * Opens and shows new window in work area. Previous window will be forcibly
     * detached.
     * 
     * @param pathToZulFile the path to *.zul config file which describes new
     * window
     */
    void open(String pathToZulFile);

}
