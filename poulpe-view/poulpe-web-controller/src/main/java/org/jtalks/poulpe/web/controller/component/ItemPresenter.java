/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.web.controller.component;

import java.util.Set;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.dao.DuplicatedField;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.exceptions.NotUniqueFieldsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class for mediating between model and view representation of components.
 * @author Dmitriy Sukharev
 */
public class ItemPresenter extends AbstractPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemPresenter.class);

    /**
     * The object that is responsible for storing and updating view of the added or edited component
     * item.
     */
    private ItemView view;

    /**
     * Initialises the object that is responsible for storing and updating view of the added or
     * edited component item.
     * @param view the object that is responsible for storing and updating view of the added or
     *            edited component item
     * @param id the edited component id, null if new component wanted
     */
    public void initView(ItemView view, Long id) {
        this.view = view;
        initArgs(id);
    }

    /**
     * Initialises this window using received arguments.
     * @param id the edited component id, null if new component wanted
     */
    private void initArgs(Long id) {
        Component component = obtainComponent(id);
        if (component != null) {
            view.setCid(component.getId());
            view.setName(component.getName());
            view.setDescription(component.getDescription());
            initTypes(component);
        }
    }

    /**
     * Obtains the component by its ID.
     * @param id the ID of the component, {@code null} or {@code -1L} to return new instance
     * @return the component by its ID, new instance if {@code id} is equal to {@code -1L}, and null
     *         if there is no component with such ID
     */
    private Component obtainComponent(Long id) {
        Component component = null;
        if (id == null || id == -1L) {
            component = new Component();
        } else {
            try {
                component = getComponentService().get(id);
            } catch (NotFoundException e) {
                LOGGER.warn("Attempt to change non-existing item.", e);
                getDialogManager().notify("item.doesnt.exist");
                view.hide();
                // component will never be returned here
            }
        }
        return component;
    }

    /**
     * Initialises the list of possible types for the specified component.
     * @param component the component whose types are being determined.
     */
    private void initTypes(Component component) {
        if (component.getComponentType() == null) {
            view.setComponentType(null);
        } else {
            view.setComponentType(component.getComponentType().toString());
        }
        view.setComponentTypes(getComponentService().getAvailableTypes());
    }

    /** Saves the created or edited component in component list. */
    public void saveComponent() {
        Component newbie = view2Model(view);
        try {
            getComponentService().saveComponent(newbie);
            view.hide();
        } catch (NotUniqueFieldsException e) {
            view.wrongFields(e.getDuplicates());
        }
    }

    /** Checks if name of created or edited component is a duplicate. Invokes notification if it is. */
    public void checkComponent() {
        Component component = view2Model(view);
        Set<DuplicatedField> set = getComponentService().getDuplicateFieldsFor(component);
        if (set != null) {
            view.wrongFields(set);
        }
    }

    /**
     * Converts the component from the view representation to the model representation.
     * @param view the view representation of the component
     * @return the component in model representation
     */
    protected Component view2Model(ItemView view) {
        Component model = new Component();
        model.setId(view.getCid());
        model.setName(view.getName());
        model.setDescription(view.getDescription());
        if (view.getComponentType().isEmpty()) {
            model.setComponentType(null);
        } else {
            model.setComponentType(ComponentType.valueOf(view.getComponentType()));
        }
        return model;
    }

}
