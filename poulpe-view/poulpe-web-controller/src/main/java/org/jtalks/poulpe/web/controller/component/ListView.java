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

import org.jtalks.common.model.entity.Component;

/**
 * The interface for managing actions and represents information about
 * components displayed in administrator panel.
 * 
 * @author Dmitriy Sukharev
 * @author Vyacheslav Zhivaev
 */
public interface ListView {

    /**
     * @return true if an item selected, false otherwise
     */
    boolean hasSelectedItem();
    
    /**
     * Should be called when {@link #hasSelectedItem()} returns {@code true}
     * @return currently selected {@link Component}
     */
    Component getSelectedItem();
    
    /**
     * Asks presenter to update component list. 
     * 
     * TODO: find out what it means
     * and what's the difference between it {@link ListView#updateList(List)}
     */
    void updateList();

    /**
     * Updates the list redrawing it
     * @param list with {@link Component} items
     */
    void updateList(List<Component> list);

    /**
     * Initializes the list model
     * @param list with {@link Component} items to be shown
     */
    void createModel(List<Component> list);

    /**
     * Shows editor for creating a new {@link Component}
     */
    void showEditor();

    /**
     * Shows editor for editing a {@link Component}
     * @param component being edited
     */
    void showEditor(Component component);

}
