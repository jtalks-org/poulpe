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
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.ListModelList;

import javax.annotation.Nonnull;

/**
 * ViewModel for TopicType page
 *
 * @author Vahluev Vyacheslav
 */
public class TopicTypeVM {
    //list on form
    private ListModelList<TopicType> topicTypes;
    //the selected topicType
    private TopicType selected;
    //service
    private TopicTypeService topicTypeService;
    //delete message
    private String deleteMessage;
    //dialog manageer
    private DialogManager dialogManager;

    /**
     * Constructor takes TopicTypeService and DialogManager as its arguments
     *
     * @param topicTypeService {@link TopicTypeService} to use
     * @param dialogManager {@link DialogManager} to use
     */
    public TopicTypeVM(@Nonnull TopicTypeService topicTypeService, @Nonnull DialogManager dialogManager) {
        this.topicTypeService = topicTypeService;
        this.dialogManager = dialogManager;
    }

    // getters & setters (you don't say!)

    /**
     * Returns current DialogManager to iteract with user
     *
     * @return current DialogManager used
     */
    public DialogManager getDialogManager() {
        return dialogManager;
    }

    /**
     * Sets DialogManager to iteract with user
     *
     * @param dialogManager new DialogManager to use
     */
    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
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

    /**
     * Sets {@link TopicTypeService} to use
     *
     * @param topicTypeService is new TopicTypeService to use
     */
    public void setTopicTypeService(TopicTypeService topicTypeService) {
        this.topicTypeService = topicTypeService;
    }

    /**
     * Message, which is used to confirm deletion
     *
     * @return message as {@link String}
     */
    public String getDeleteMessage() {
        return deleteMessage;
    }

    //action command
    @NotifyChange({"selected", "topicTypes"})
    @Command
    /**
     * Creates new TopicType and adds it on form
     */
    public void newTopicType() {
        TopicType topicType = new TopicType();
        //select the new one
        selected = topicType;
        selected.setTitle("New Title");
        selected.setDescription("New Description");
        getTopicTypes().add(topicType);
    }

    @NotifyChange("selected")
    @Command
    /**
     * Saves current TopicType selected. Doesn't save other if changed.
     * Shows warning or error messages if something is wrong
     */
    public void saveTopicType() {
        getTopicTypeService().saveOrUpdate(selected);
    }

    @NotifyChange({"selected", "topicTypes", "deleteMessage"})
    @Command
    /**
     * Deletes current TopicType selected
     */
    public void deleteTopicType() {

        dialogManager.confirmDeletion(selected.getTitle(), new DialogManager.Performable() {
            @Override
            public void execute() {
                getTopicTypeService().deleteTopicType(selected);
                deleteFromList();
            }
        });
    }

    @NotifyChange({"selected", "deleteMessage"})
    @Command
    /**
     * Creates message to ask for confirmation on delete of current TopicType.
     * Shows dialog window.
     */
    public void deleteFromList() {
        getTopicTypes().remove(selected);
        //clean the selected
        selected = null;
        //remove message & window
        deleteMessage = null;
    }

    //validators for prompt
    //in progress
    public Validator getTitleValidator() {
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                String title = (String) ctx.getProperty().getValue();
                if (title == null || title.isEmpty()) {
                    addInvalidMessage(ctx, Labels.getLabel(TopicType.TITLE_CANT_BE_VOID));
                }
            }
        };
    }

}
