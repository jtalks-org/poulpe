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

import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import javax.annotation.Nonnull;

/**
 * ViewModel for TopicType page
 *
 * @author Vahluev Vyacheslav
 */
public class TopicTypeVm {
    //list on form
    private ListModelList<TopicType> topicTypes;
    //the selected topicType
    private TopicType selected;
    //service
    private final TopicTypeService topicTypeService;
    //dialog manager
    private final DialogManager dialogManager;
    // boolean to show window for creating or editing
    private Boolean showPopUp;


    /**
     * Constructor takes TopicTypeService and DialogManager as its arguments
     *
     * @param topicTypeService {@link TopicTypeService} to use
     * @param dialogManager    {@link DialogManager} to use
     */
    public TopicTypeVm(@Nonnull TopicTypeService topicTypeService,
                       @Nonnull DialogManager dialogManager) {
        this.topicTypeService = topicTypeService;
        this.dialogManager = dialogManager;
    }


    /**
     * Creates new TopicType and adds it on form
     */
    @NotifyChange({"selected", "showPopUp"})
    @Command
    public void newTopicType() {
        selected = new TopicType();
        selected.setTitle("New Title");
        selected.setDescription("New Description");
        showPopUp = true;
    }

    /**
     * Edits the TopicType selected, shows Dialog Window
     */
    @NotifyChange({"showPopUp"})
    @Command
    public void editTopicType() {
        showPopUp = true;
    }

    /**
     * Saves current TopicType selected. Doesn't save other if changed. Shows warning or error messages if something is
     * wrong
     */
    @NotifyChange({"selected", "topicTypes", "showPopUp"})
    @Command
    public void saveTopicType() {
        getTopicTypeService().saveOrUpdate(selected);
        //check out if selected is just created
        if (!topicTypes.contains(selected)) {
            getTopicTypes().add(selected);
        }
        cancelEditTopicType();
    }

    /**
     * Hides window for editing or creating of TopicType
     */
    @NotifyChange({"selected", "showPopUp"})
    @Command
    public void cancelEditTopicType() {
        showPopUp = null;
        selected = null;
    }

    /**
     * Deletes current TopicType selected
     */
    @NotifyChange({"selected", "topicTypes"})
    @Command
    public void deleteTopicType() {
        getTopicTypeService().deleteTopicType(selected);
        deleteFromList(selected);
    }

    /**
     * Deletes from list of TopicTypes on web-form.
     */
    @NotifyChange({"selected", "topicTypes"})
    @Command
    public void deleteFromList(TopicType selected) {
        getTopicTypes().remove(selected);
        setSelected(null);
    }

    /**
     * Returns current DialogManager to iteract with user
     *
     * @return current DialogManager used
     */
    public DialogManager getDialogManager() {
        return dialogManager;
    }

    /**
     * Returns ListModelList<TopicType>
     *
     * @return ListModelList to use on web-form
     */
    public ListModelList<TopicType> getTopicTypes() {
        if (topicTypes == null) {
            //init the list
            topicTypes = new ListModelList<TopicType>(getTopicTypeService().getAll());
        }
        return topicTypes;
    }

    /**
     * Sets new ListModelList<TopicType> to use on form
     *
     * @param topicTypes ListModelList<TopicType> to use
     */
    public void setTopicTypes(ListModelList<TopicType> topicTypes) {
        this.topicTypes = topicTypes;
    }

    /**
     * Returns current {@link TopicType} selected
     *
     * @return TopicType selected
     */
    public TopicType getSelected() {
        return selected;
    }

    /**
     * Sets {@link TopicType} to select
     *
     * @param selected is TopicType for select
     */
    public void setSelected(TopicType selected) {
        this.selected = selected;
    }

    /**
     * Returns {@link TopicTypeService} used
     *
     * @return {@link TopicTypeService} to use
     */
    public TopicTypeService getTopicTypeService() {
        return topicTypeService;
    }

    public Boolean getShowPopUp() {
        return showPopUp;
    }
}
