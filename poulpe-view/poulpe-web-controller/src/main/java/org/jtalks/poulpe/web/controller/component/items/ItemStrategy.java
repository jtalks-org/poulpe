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
package org.jtalks.poulpe.web.controller.component.items;

import org.jtalks.poulpe.model.entity.Component;

/**
 * Strategy used in {@link ItemPresenter} when showing view.<br><br>
 * 
 * There are two main implementations:<br> 
 * {@link EditItemStrategy} for editing an existent {@link Component} entity;<br>
 * {@link NewItemStrategy} for creating a new entity object.
 * 
 * @author Alexey Grigorev
 */
interface ItemStrategy {
    /**
     * Initializes a displayed view for showing entity
     */
    void init();
    
    /**
     * Retrieves the item from the view
     * @return the item appropriate for saving
     */
    Component itemForSaving();
}