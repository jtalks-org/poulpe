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

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.TopicType;

/**
 * The interface for creation and closing application windows.
 * 
 * @author Dmitriy Sukharev
 * 
 */
public interface WindowManager {

    /**
     * Creates and shows new window which is responsible for editing components.
     * 
     * @param componentId
     *            identifier of the {@link Component} to be edited, or
     *            {@code -1L} to create a new one
     * @param listener
     *            listener to be invoked after window is closed. It's actually
     *            might be the object of any class, writing the implementation
     *            of WindowManager you should document what type it has.
     */
    void showEditComponentWindow(long componentId, Object listener);

    /**
     * Closes the {@code window} window and invokes its "onDetach" listener.
     * 
     * @param window
     *            the window to be closed
     */
    void closeWindow(Object window);
    
    void openTopicTypeWindowForCreate(EditListener<TopicType> listener);
    
    void openTopicTypeWindowForEdit(TopicType topicType, EditListener<TopicType> listener);
    
    void openUserWindowForEdit(User user, EditListener<User> listener);
}
