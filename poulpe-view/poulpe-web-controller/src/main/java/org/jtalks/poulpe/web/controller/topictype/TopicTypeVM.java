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

import org.apache.commons.lang.StringUtils;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
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
import java.util.Iterator;
import java.util.Set;

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
    private final TopicTypeService topicTypeService;
    //dialog manager
    private final DialogManager dialogManager;
    //validator JSR-303
    private final EntityValidator entityValidator;
    //dialogMessage when new window is opened
    private String editMessage;
    //messages for creating and editing topicType
    public static final String NEW_TOPIC_TYPE = "item.add";
    public static final String EDIT_TOPIC_TYPE = "item.edit";

    /**
     * Constructor takes TopicTypeService and DialogManager as its arguments
     *
     * @param topicTypeService {@link TopicTypeService} to use
     * @param dialogManager    {@link DialogManager} to use
     * @param entityValidator    {@link EntityValidator} to use
     */
    public TopicTypeVM(@Nonnull TopicTypeService topicTypeService, @Nonnull DialogManager dialogManager, @Nonnull EntityValidator entityValidator) {
        this.topicTypeService = topicTypeService;
        this.dialogManager = dialogManager;
        this.entityValidator = entityValidator;
    }

    //action command

    /**
     * Creates new TopicType and adds it on form
     */
    @NotifyChange({"selected", "topicTypes", "editMessage"})
    @Command
    public void newTopicType() {
        selected = new TopicType();
        selected.setTitle("New Title");
        selected.setDescription("New Description");
        editMessage = Labels.getLabel(NEW_TOPIC_TYPE);
    }

    /**
     * Edits the TopicType selected, shows Dialog Window
     */
    @NotifyChange({"selected", "topicTypes", "editMessage"})
    @Command
    public void editTopicType() {
        editMessage = Labels.getLabel(EDIT_TOPIC_TYPE);
    }

    /**
     * Saves current TopicType selected. Doesn't save other if changed.
     * Shows warning or error messages if something is wrong
     */
    @NotifyChange({"selected", "topicTypes", "editMessage"})
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
    @NotifyChange({"selected", "topicTypes", "editMessage"})
    @Command
    public void cancelEditTopicType() {
        editMessage = null;
        selected = null;
    }

    /**
     * Deletes current TopicType selected
     */
    @NotifyChange({"selected", "topicTypes"})
    @Command
    public void deleteTopicType() {
        getTopicTypeService().deleteTopicType(selected);
        deleteFromList();
    }

    /**
     * Deletes from list of TopicTypes on web-form.
     */
    @NotifyChange({"selected", "topicTypes"})
    @Command
    public void deleteFromList() {
        getTopicTypes().remove(selected);
        setSelected(null);
    }

    /**
     * Collects errors obtained from {@link ValidationResult}
     * and represents them as localized String
     *
     * @param result {@link ValidationResult}
     * @return Errors as String
     */
    //TODO: Maybe that should be in some new bean? Like CommonValidator(Entity)
    private String collectErrors(ValidationResult result) {
        StringBuilder errorMessage = new StringBuilder();
        Set<ValidationError> errors = result.getErrors();

        for (Iterator<ValidationError> i = errors.iterator(); i.hasNext(); ) {
            errorMessage.append(Labels.getLabel(i.next().getErrorMessageCode()));
            if (i.hasNext()) {
                errorMessage.append(", ");
            }
        }

        return errorMessage.toString();
    }

    //validators for prompt

    /**
     * Validator for title field on web-form
     *
     * @return Validator for title field
     */
    public Validator getTitleValidator() {
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                String title = (String) ctx.getProperty().getValue();
                Long id = selected.getId();
                //TopicType for test should be similar in meaningful fields
                // to what we're going to save - (id, title)
                TopicType test = new TopicType(title, StringUtils.EMPTY);
                test.setId(id);

                ValidationResult result = entityValidator.validate(test);

                if (result.hasErrors()) {
                    addInvalidMessage(ctx, collectErrors(result));
                }
            }
        };
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
     * Returns message shown when window for create or
     * edit is opened.
     *
     * @return message as {@link String}
     */
    public String getEditMessage() {
        return editMessage;
    }
}
