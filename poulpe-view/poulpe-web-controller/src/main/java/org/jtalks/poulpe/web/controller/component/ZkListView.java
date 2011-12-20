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
import org.jtalks.poulpe.web.controller.component.items.ItemView;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 * The class which manages actions and represents information about components
 * displayed in administrator panel.
 * 
 * @author Dmitriy Sukharev
 * @author Vyacheslav Zhivaev
 */
public class ZkListView extends Window implements ListView, AfterCompose {

    private static final long serialVersionUID = -5891403019261284676L;

    private transient ListPresenter presenter;
    private ListModelList<Component> model;
    private Listbox listbox;

    /** {@inheritDoc} */
    @Override
    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        presenter.initView(this);

        listbox.setItemRenderer(new ZkListItemRenderer(this));
    }

    private ItemView getComponentEditor() {
        org.zkoss.zk.ui.Component comp = getDesktop().getPage("editCompPage").getFellow("editCompWindow");
        comp.setAttribute("backWin", this);
        return (ItemView) comp;
    }

    // ==== ListView methods implementation ====

    /** {@inheritDoc} */
    @Override
    public void showEditor() {
        ItemView comp = getComponentEditor();
        comp.showEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void showEditor(Component component) {
        ItemView comp = getComponentEditor();
        comp.show(component);
    }

    /** {@inheritDoc} */
    @Override
    public void createModel(List<Component> list) {
        @SuppressWarnings("unchecked") // BindingListModelList has no generic params
        ListModelList<Component> bindingModel = new BindingListModelList(list, true);
        
        model = bindingModel;
        listbox.setModel(model);
    }

    /** {@inheritDoc} */
    @Override
    public void updateList(List<Component> list) {
        model.clear();
        model.addAll(list);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasSelectedItem() {
        return listbox.getSelectedIndex() != -1;
    }

    /** {@inheritDoc} */
    @Override
    public Component getSelectedItem() {
        return model.get(listbox.getSelectedIndex());
    }

    /** {@inheritDoc} */
    @Override
    public void updateList() {
        presenter.updateList();
    }

    // ==== UI events handler ====

    /**
     * Fired when add button is clicked, shows empty editor for creating a new
     * {@link Component}
     */
    public void onClick$addCompButton() {
        presenter.addComponent();
    }

    /**
     * Tells presenter to delete selected component
     * @see ListPresenter
     */
    public void onClick$delCompButton() {
        presenter.deleteComponent();
    }

    // ==== get-set methods ====

    /**
     * Sets the presenter which is linked with this window.
     * 
     * @param presenter new value of the presenter which is linked with this
     * window
     */
    public void setPresenter(ListPresenter presenter) {
        this.presenter = presenter;
    }

}
