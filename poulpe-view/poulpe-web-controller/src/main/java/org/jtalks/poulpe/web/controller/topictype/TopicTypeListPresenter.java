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

import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;
import org.jtalks.poulpe.web.controller.EditListener;
import org.jtalks.poulpe.web.controller.HighConcurrencyEditListener;
import org.jtalks.poulpe.web.controller.WindowManager;

/**
 * Presenter manager displaying a list of
 * {@link org.jtalks.poulpe.model.entity.TopicType}
 * 
 * @author Vladimir Bukhtoyarov
 * @author Vyacheslav Zhivaev
 * 
 */
public class TopicTypeListPresenter {

    /**
     * View to display a list of
     * {@link org.jtalks.poulpe.model.entity.TopicType}
     * 
     * @author tomcat
     * 
     */
    public interface TopicTypeListView {

        /**
         * Show topic type list.
         * <ul>
         * <li>
         * Each row should show only the title.</li>
         * <li>
         * If user move mouse over the Topic type, a hint should appear with the
         * description of the type.</li>
         * <li>
         * If user double click on the existing type event should be directed to
         * the presenter</li>
         * <li>
         * Multiple selections should be allowed.</li>
         * </ul>
         * 
         * @param list
         *            of #{@link org.jtalks.poulpe.model.entity.TopicType}
         */
        void showTopicTypeList(List<TopicType> list);

        /**
         * Returns the selected topic types or empty list if nothing is selected.
         * 
         * @return a list of selected topic types or empty list
         */
        List<TopicType> getSelectedTopicType();

    }

    private TopicTypeService topicTypeService;
    private WindowManager windowManager;
    private DialogManager dialogManager;
    private TopicTypeListView view;

    private EditListener<TopicType> editListener = new HighConcurrencyEditListener<TopicType>() {
        @Override
        protected void refreshData() {
            refreshTopicTypeList();
        }
    };

    /**
     * Initialize view instance before first rendering
     * 
     * @param view is view for topic type
     */
    public void initView(TopicTypeListView view) {
        this.view = view;
        refreshTopicTypeList();
    }

    /**
     * Action handler on add
     */
    public void onAddAction() {
        windowManager.openTopicTypeWindowForCreate(editListener);
    }

    /**
     * Action handler on edit
     * 
     * @param topicType is topic type that would be edited
     */
    public void onEditAction(TopicType topicType) {
        windowManager.openTopicTypeWindowForEdit(topicType, editListener);
    }

    /**
     * Action handler on delete
     */
    public void onDeleteAction() {
        final List<TopicType> topicTypes = view.getSelectedTopicType();
        if (topicTypes.isEmpty()) {
            dialogManager.notify("item.no.selected.item");
            return;
        }

        List<String> titleList = new ArrayList<String>(topicTypes.size());
        for (TopicType topicType : topicTypes) {
            titleList.add(topicType.getTitle());
        }

        dialogManager.confirmDeletion(titleList, new Performable() {
            @Override
            public void execute() {
                topicTypeService.deleteTopicTypes(topicTypes);
                refreshTopicTypeList();
            }
        });
    }

    /**
     * Sets window manager
     * 
     * @param windowManager is new window manager
     */
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    /**
     * Sets dialog manager
     * 
     * @param dialogManager is new dialog manager
     */
    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    /**
     * Sets topic type service
     * 
     * @param topicTypeService is new topic type service
     */
    public void setTopicTypeService(TopicTypeService topicTypeService) {
        this.topicTypeService = topicTypeService;
    }

    /**
     * Refresh topic type list
     */
    private void refreshTopicTypeList() {
        List<TopicType> list = topicTypeService.getAll();
        view.showTopicTypeList(list);
    }

}
