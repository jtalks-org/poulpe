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
import org.jtalks.poulpe.validation.ValidationResult;

/**
 * Controls the component editing window. <br>
 * Extends {@link ItemDataView} providing getters and setters to data entered in
 * UI components
 * 
 * @author Dmitriy Sukharev
 * @author Alexey Grigorev
 */
public interface ItemView extends ItemDataView {

    /**
     * Shows the window for editing a component.
     * @param component item to be edited
     */
    void show(Component component);

    /**
     * Shows the window for creating a new component.
     */
    void showEmpty();

    /**
     * Hides the window.
     */
    void hide();

    /**
     * @param result
     */
    void validaionFailure(ValidationResult result);

}
