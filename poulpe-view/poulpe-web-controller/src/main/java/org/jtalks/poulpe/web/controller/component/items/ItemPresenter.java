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

import java.util.Set;

import org.jtalks.poulpe.model.dao.DuplicatedField;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.service.exceptions.NotUniqueFieldsException;
import org.jtalks.poulpe.web.controller.component.AbstractPresenter;

/**
 * The class for mediating between model and view representation of components.
 * 
 * @author Dmitriy Sukharev
 * @author Alexey Grigorev
 */
public class ItemPresenter extends AbstractPresenter {

    private ItemView view;
    private ItemStrategy editingStrategy = DoNothingItemStrategy.instance;

    /**
     * Initializes this presenter for creating a new component
     * @param view current {@link ItemView} instance
     */
    public void initForCreating(ItemView view) {
        editingStrategy = new NewItemStrategy(view);
        reinit(view);
    }

    /**
     * Initializes this presenter for editing the new component
     * @param view current {@link ItemView} instance
     * @param component to be edited
     */
    public void initForEditing(ItemView view, Component component) {
        editingStrategy = new EditItemStrategy(view, component);
        reinit(view);
    }

    private void reinit(ItemView view) {
        setView(view);
        editingStrategy.init();
        view.setComponentTypes(getComponentService().getAvailableTypes());
    }

    /**
     * Saves the created or edited component in component list.
     */
    public void saveComponent() {
        Component component = editingStrategy.itemForSaving();

        try {
            getComponentService().saveComponent(component);
            view.hide();
        } catch (NotUniqueFieldsException e) {
            view.wrongFields(e.getDuplicates());
        }
    }

    /**
     * Checks if name of created or edited component is a duplicate. Invokes
     * notification if it is.
     */
    public void checkComponent() {
        Component component = ViewToEntityConverter.view2Model(view);
        Set<DuplicatedField> set = getComponentService().getDuplicateFieldsFor(component);
        if (!set.isEmpty()) {
            view.wrongFields(set);
        }
    }

    /**
     * @return the view
     */
    public ItemView getView() {
        return view;
    }

    /**
     * @param view the view to set
     */
    public void setView(ItemView view) {
        this.view = view;
    }

}
