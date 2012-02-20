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

import org.jtalks.common.model.entity.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

/**
 * Used in {@link ZkListView} for rendering each row in its table.
 * 
 * See comments on the constructor -
 * {@link ZkListItemRenderer#ZkListItemRenderer(ZkListView)}
 * 
 * @author Dmitriy Sukharev
 * @author Alexey Grigorev
 * 
 * @see ZkListItemRenderer#ZkListItemRenderer(ZkListView)
 */
class ZkListItemRenderer implements ListitemRenderer<Component> {
    private final ZkListView listView;

    /**
     * Adds 3 columns to the table:<br>
     * 1) component's name, bold;<br>
     * 2) component's description;<br>
     * 3) component's type.<br>
     * <br>
     * 
     * Adds {@link Events#ON_DOUBLE_CLICK} handler to the whole item which will
     * edit the given component
     * 
     * @param listViewImpl
     * @see ZkListItemRenderer
     * 
     */
    public ZkListItemRenderer(ZkListView listView) {
        this.listView = listView;
    }

    @Override
    public void render(Listitem listItem, final Component component, int index) {
        listItem.appendChild(boldName(component));
        listItem.appendChild(description(component));
        listItem.appendChild(componentType(component));

        listItem.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) {
                listView.showEditor(component);
            }
        });
    }

    private Listcell boldName(Component component) {
        Listcell cell = new Listcell();
        Label nameLabel = new Label(component.getName());
        nameLabel.setSclass("boldstyle");
        cell.appendChild(nameLabel);
        return cell;
    }

    private Listcell description(Component component) {
        return new Listcell(component.getDescription());
    }

    private Listcell componentType(Component component) {
        return new Listcell(component.getComponentType().toString());
    }
}