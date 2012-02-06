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

import org.jtalks.common.model.entity.Component;

/**
 * Used in {@link ItemPresenter} when
 * {@link ItemPresenter#initForEditing(ItemView, Component))} is invoked.<br>
 * <br>
 * 
 * - Updates UI retrieving data from the given component when
 * {@link ItemStrategy#init()} is called.<br>
 * 
 * - Returns the component with the same id and uuid, modifying the rest of the
 * data, when {@link ItemStrategy#itemForSaving()} is called.<br>
 * 
 * @author Alexey Grigorev
 * @see ItemStrategy
 */
class EditItemStrategy implements ItemStrategy {

    private final ItemDataView view;
    private final Component component;

    /**
     * Sets the view which will be filled with components data when
     * {@link #init()} is called.
     * 
     * @param view view to iteract with
     * @see NewItemStrategy
     */
    public EditItemStrategy(ItemDataView view, Component component) {
        this.view = view;
        this.component = component;
    }

    /** {@inheritDoc} */
    @Override
    public void init() {
        view.setComponentId(component.getId());
        view.setName(component.getName());
        view.setDescription(component.getDescription());
        view.setComponentType(component.getComponentType());
    }
    
    /** {@inheritDoc} */
    @Override
    public Component itemForSaving() {
        return ViewToEntityConverter.view2Model(view, component);
    }

}