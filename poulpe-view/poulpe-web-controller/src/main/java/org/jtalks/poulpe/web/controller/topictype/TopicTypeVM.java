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
    private TopicTypeService topicTypeService;
    //dialog manager
    private DialogManager dialogManager;
    //validator JSR-303
    private EntityValidator entityValidator;

    /**
     * Constructor takes TopicTypeService and DialogManager as its arguments
     *
     * @param topicTypeService {@link TopicTypeService} to use
     * @param dialogManager    {@link DialogManager} to use
     */
    public TopicTypeVM(@Nonnull TopicTypeService topicTypeService, @Nonnull DialogManager dialogManager, @Nonnull EntityValidator entityValidator) {
        this.topicTypeService = topicTypeService;
        this.dialogManager = dialogManager;
        this.entityValidator = entityValidator;
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
    @NotifyChange
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
    @NotifyChange
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

    @NotifyChange({"selected", "topicTypes"})
    @Command
    /**
     * Deletes current TopicType selected
     */
    public void deleteTopicType() {
        getTopicTypeService().deleteTopicType(selected);
        deleteFromList();
    }

    @NotifyChange({"selected", "topicTypes"})
    @Command
    /**
     * Deletes from list of TopicTypes on web-form.
     */
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
        ;

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
                TopicType test = new TopicType(title, StringUtils.EMPTY);

                ValidationResult result = entityValidator.validate(test);

                if (result.hasErrors()) {
                    addInvalidMessage(ctx, collectErrors(result));
                }
            }
        };
    }

}
