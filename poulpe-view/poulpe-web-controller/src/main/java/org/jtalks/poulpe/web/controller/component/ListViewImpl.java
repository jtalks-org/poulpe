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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

/**
 * The class which manages actions and represents information about components
 * displayed in administrator panel.
 * 
 * @author Dmitriy Sukharev
 */
public class ListViewImpl extends Window implements ListView, AfterCompose {

    private static final long serialVersionUID = -5891403019261284676L;

    /*
     * Important! If we are going to serialize/deserialize this class, this
     * field must be initialised explicitly during deserialization
     */
    private transient ListPresenter presenter;
    private ListModelList<Component> model;
    private Listbox listbox;

    /** {@inheritDoc} */
    @Override
    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        presenter.initView(this);
        
        listbox.setItemRenderer(new ListitemRenderer<Component>() {
            /** {@inheritDoc} */
            @Override
            public void render(Listitem listItem, Component component) {
                Listcell cell = new Listcell();
                Label nameLabel = new Label(component.getName());
                cell.appendChild(nameLabel);
                nameLabel.setSclass("boldstyle");
                listItem.appendChild(cell);
                listItem.appendChild(new Listcell(component.getDescription()));
                listItem.appendChild(new Listcell(component.getComponentType().toString()));
                
                listItem.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
                    /** {@inheritDoc} */
                    @Override
                    public void onEvent(Event event) {
                        showEditor(getSelectedItem());
                    }
                });
                
            }
        });
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // Suppressed because BindingListModelList has no generic param
    @Override
    public void createModel(List<Component> list) {
        model = new BindingListModelList(list, true);
        listbox.setModel(model);
    }

    /**
     * Sets the presenter which is linked with this window.
     * 
     * @param presenter
     *            new value of the presenter which is linked with this window
     */
    public void setPresenter(ListPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Tells to presenter that the window for adding new component must be
     * shown.
     * 
     * @see ListPresenter
     */
    public void onClick$addCompButton() {
        showEditor();
    }

    private void showEditor() {
        org.zkoss.zk.ui.Component comp = getDesktop().getPage("editCompPage").getFellow("editCompWindow");
        comp.setAttribute("backWin", this);
        ((ItemView) comp).showEmpty();
    }
    
    
    private void showEditor(Component component) {
        org.zkoss.zk.ui.Component comp = getDesktop().getPage("editCompPage").getFellow("editCompWindow");
        comp.setAttribute("backWin", this);
        ((ItemView) comp).show(component);
    }
    
    /**
     * Tells to presenter to delete selected component (it knows which one it
     * is).
     * 
     * @see ListPresenter
     */
    public void onClick$delCompButton() {
        presenter.deleteComponent();
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

}
