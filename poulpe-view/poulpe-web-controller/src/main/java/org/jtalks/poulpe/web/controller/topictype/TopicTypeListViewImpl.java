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
package org.jtalks.poulpe.web.controller.topictype;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jtalks.poulpe.model.entity.TopicType;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

/**
 * This class is implementation view for managing topic types
 * 
 * @author Vladimir Bukhtoyarov
 */
public class TopicTypeListViewImpl extends Window implements TopicTypeListView, AfterCompose {

    private static final long serialVersionUID = 1L;

    private Listbox topicTypeListbox;
    private TopicTypeListPresenter presenter;

    public void setPresenter(TopicTypeListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showTopicTypeList(List<TopicType> list) {
        ListModelList<TopicType> listModelList = new BindingListModelList(list, true);
        topicTypeListbox.setModel(listModelList);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TopicType> getSelectedTopicType() {
        Set<Listitem> items = topicTypeListbox.getSelectedItems();
        List<TopicType> selectedTopicTypes = new ArrayList<TopicType>();
        for (Listitem listitem : items) {
            selectedTopicTypes.add((TopicType) listitem.getValue());
        }
        return selectedTopicTypes;
    }

    @Override
    public void afterCompose() {
        Components.wireVariables(this, this);
        Events.addEventListeners(this, presenter);
        initializeTopicTypeListbox();
        presenter.initView(this);
    }

    private void initializeTopicTypeListbox() {
        topicTypeListbox.setItemRenderer(new ListitemRenderer<TopicType>() {
            @Override
            public void render(Listitem item, final TopicType topicType) throws Exception {
                Listcell cellWithName  = new Listcell();
                Listcell cellWithDesc  = new Listcell();
                cellWithName.setLabel(topicType.getTitle());
                cellWithDesc.setLabel(topicType.getDescription());
                item.setValue(topicType);
                item.appendChild(cellWithName);
                item.appendChild(cellWithDesc);
                item.setTooltiptext(topicType.getDescription());
                item.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        presenter.onEditAction(topicType);
                    }
                });
            }
        });
        topicTypeListbox.setMultiple(true);
    }

}
