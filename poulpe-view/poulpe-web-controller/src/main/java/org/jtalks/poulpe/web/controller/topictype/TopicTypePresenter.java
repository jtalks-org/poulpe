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

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.EditListener;
import org.jtalks.poulpe.web.controller.WindowManager;

/**
 * Presenter of TopicType list page.
 * 
 * @author Pavel Vervenko
 * @author Vahluev Vyacheslav
 * @author Vyacheslav Zhivaev
 * @author Alexey Grigorev
 */
public class TopicTypePresenter {

    public static final String TITLE_DOESNT_EXISTS = "topictypes.error.topictype_name_doesnt_exists";

    private TopicTypeService topicTypeService;
    private EntityValidator entityValidator;

    private EditListener<TopicType> listener;
    private WindowManager windowManager;

    private TopicTypeView view;
    private TopicType topicType;


    /**
     * Save and init view.
     */
    public void initView(TopicTypeView view) {
        this.view = view;
    }

    /**
     * Initialize presenter for create topic type
     * 
     * @param view new view
     * @param listener event listener
     */
    public void initializeForCreate(TopicTypeView view, EditListener<TopicType> listener) {
        this.view = view;
        this.listener = listener;
        view.hideEditAction();
        this.topicType = new TopicType();
    }

    /**
     * Initialize presenter for edit topic type
     * @param topicType target topic type
     * @param listener event listener
     */
    public void initializeForEdit(TopicTypeView view, TopicType topicType, EditListener<TopicType> listener) {
        this.view = view;
        this.listener = listener;
        view.hideCreateAction();

        try {
            this.topicType = topicTypeService.get(topicType.getId());
        } catch (NotFoundException e) {
            view.openErrorPopupInTopicTypeDialog(TITLE_DOESNT_EXISTS);
            return;
        }

        view.showTypeTitle(this.topicType.getTitle());
        view.showTypeDescription(this.topicType.getDescription());
    }

    /**
     * Action handler on title loose focus
     */
    public void onTitleLoseFocus() {
        String title = view.getTypeTitle();
        topicType.setTitle(title);
        validate(topicType);
    }

    /**
     * Action handler on create action
     */
    public void onCreateAction() {
        if (save()) {
            closeView();
            listener.onCreate(topicType);
        }
    }

    /**
     * Action handler on update action
     */
    public void onUpdateAction() {
        if (save()) {
            closeView();
            listener.onUpdate(topicType);
        }
    }

    /**
     * Action handler on cancel edit action
     */
    public void onCancelEditAction() {
        closeView();
        listener.onCloseEditorWithoutChanges();
    }

    /**
     * Close view
     */
    public void closeView() {
        windowManager.closeWindow(view);
    }

    /**
     * Save action
     * 
     * @return <code>true</code> if saving done and none errors occurred,
     * <code>false</code> in otherwise
     */
    public boolean save() {
        topicType.setTitle(view.getTypeTitle());
        topicType.setDescription(view.getTypeDescription());

        if (validate(topicType)) {
            topicTypeService.saveOrUpdate(topicType);
            return true;
        } else {
            return false;
        }
    }

    private boolean validate(TopicType topicType) {
        ValidationResult result = entityValidator.validate(topicType);

        if (result.hasErrors()) {
            view.validationFailure(result);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Set the TopicTypeService implementation.
     */
    public void setTopicTypeService(TopicTypeService topicTypeService) {
        this.topicTypeService = topicTypeService;
    }

    /**
     * @param entityValidator the entityValidator to set
     */
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    /**
     * Sets the window manager
     */
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    /**
     * @param listener
     */
    public void setListener(EditListener<TopicType> listener) {
        this.listener = listener;
    }
}