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

import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.zkoss.zk.ui.Executions;

/**
 * The class for mediating between model and view representation of components.
 *
 * @author Dmitriy Sukharev
 * @author Vahluev Vyacheslav
 * @author Vyacheslav Zhivaev
 */
public class ListPresenter extends AbstractComponentPresenter {

    private static final String NO_SELECTED_ITEM = "item.no.selected.item";
    private static final String NO_AVAILABLE_TYPES = "component.error.no_available_types";
    private static final String EDIT_COMPONENT_LOCATION = "components/edit_comp.zul";

    /** The object that is responsible for updating view of the component list. */
    private ListView view;

    /**
     * Initialises the object that is responsible for updating view of the
     * component list.
     * @param listView the object that is responsible for updating view of the
     * component list
     */
    public void initView(ListView listView) {
        view = listView;
        view.createModel(getComponents());
    }

    /**
     * Returns view representation of all components.
     * @return the list of the components
     */
    public List<Component> getComponents() {
        return getComponentService().getAll();
    }

    public void addComponent() {
        if (getComponentService().getAvailableTypes().size() > 0) {
            view.showEditor();
        } else {
            getDialogManager().notify(NO_AVAILABLE_TYPES);
        }
    }

    /**
     * Removes the selected component from the component list.
     */
    public void deleteComponent() {
        if (view.hasSelectedItem()) {
            // TODO: view.getSelectedItem() is invoked twice, one time here
            // and the second - in DeletePerformable#execute().
            // get rid of it
            Component victim = view.getSelectedItem();
            getDialogManager().confirmDeletion(victim.getName(), new DeletePerformable());
        } else {
            getDialogManager().notify(NO_SELECTED_ITEM);
        }
    }

    /**
     * Opening a new window to configure a component
     * or shows a message if none were selected
     */
    public void configureComponent() {
        if (!view.hasSelectedItem()) {
            dialogManager.notify(NO_SELECTED_ITEM);
        } else {
            showEditWindow();
        }
    }

    /**
     * Shows a component edit window
     */
    private void showEditWindow () {
        Component cm = view.getSelectedItem();
        Executions.getCurrent().getDesktop().setAttribute("componentToEdit", cm);

        windowManager.open(EDIT_COMPONENT_LOCATION);
    }

    /**
     * Updates the list of components.
     */
    public void updateList() {
        view.updateList(getComponents());
    }

    /**
     * The class for executing deletion of the selected item, delegates this
     * task to the component service and view.
     * @author Dmitriy Sukharev
     */
    class DeletePerformable implements DialogManager.Performable {
        /** {@inheritDoc} */
        @Override
        public void execute() {
            Component victim = view.getSelectedItem();
            getComponentService().deleteComponent(victim);
            updateList();
        }
    }

}
