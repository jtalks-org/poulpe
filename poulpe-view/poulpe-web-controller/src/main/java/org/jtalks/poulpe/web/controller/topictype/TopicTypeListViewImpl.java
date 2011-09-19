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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

/**
 * This class is implementation view for managing topic types
 * 
 * @author Vladimir Bukhtoyarov
 */
public class TopicTypeListViewImpl extends Window implements TopicTypeListPresenter.TopicTypeListView, AfterCompose {

    private static final long serialVersionUID = 1L;
    
    private Listbox topicTypeListbox;
    private TopicTypeListPresenter presenter;
    
    public void setPresenter(TopicTypeListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showTopicTypeList(List<TopicType> list) {
        topicTypeListbox.setModel(new ListModelList(list));
    }

    @Override
    public List<TopicType> getSelectedTopicType() {
        Set<Listitem> items = topicTypeListbox.getSelectedItems();
        List<TopicType> selectedTopicTypes = new ArrayList<TopicType>();
        for (Listitem listitem: items) {
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
        topicTypeListbox.setItemRenderer(new ListitemRenderer() {
            @Override
            public void render(Listitem item, Object data) throws Exception {
                final TopicType topicType = (TopicType) data;
                item.setValue(topicType);
                item.setLabel(topicType.getTitle());
                item.setTooltiptext(topicType.getDescription());
                item.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        presenter.onEditAction(topicType);
                    }
                });
            }});
        topicTypeListbox.setMultiple(true);
    }

}
