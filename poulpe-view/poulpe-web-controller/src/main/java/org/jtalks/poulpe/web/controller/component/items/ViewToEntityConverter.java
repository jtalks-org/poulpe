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
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;

/**
 * It's used for converting {@link ItemDataView} instances to corresponding {@link Component} item.
 * 
 * @author Alexey Grigorev
 * @author Dmitriy Sukharev
 * @author Guram Savinov
 */
class ViewToEntityConverter {

    private ViewToEntityConverter() {
    }

    /**
     * Converts the component from the view representation to the model representation.<br>
     * <br>
     * 
     * <b>Note:</b> the created component is new - this means that it doesn't yet have an id, and it's unique.
     * 
     * @param view view to take instance from
     * @return <i>new</i> {@link Component} converted from view
     */
    public static Component view2Model(ItemDataView view) {
        Component component;
        if (view.getComponentType() == ComponentType.FORUM) {
            component = new Jcommune();
        } else {
            component = new Component();
        }
        component.setId(view.getComponentId());
        return view2Model(view, component);
    }

    /**
     * Converts the component from the view representation to the model representation.<br>
     * <br>
     * 
     * The returning object will have the same id and uuid as the original one.<br>
     * 
     * <b>Note:</b> The operation is performed on the given object, so the passed parameter will be changed.
     * 
     * @param view instance with component's data
     * @param component original entity being edited
     * @return {@link Component} converted from view
     */
    public static Component view2Model(ItemDataView view, Component component) {
        component.setName(view.getName());
        component.setDescription(view.getDescription());
        component.setComponentType(view.getComponentType());
        return component;
    }
}