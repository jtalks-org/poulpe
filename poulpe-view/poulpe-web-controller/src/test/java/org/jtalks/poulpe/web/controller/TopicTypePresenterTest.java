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
package org.jtalks.poulpe.web.controller;

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.getFakeTopicType;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.topictype.TopicTypePresenter;
import org.jtalks.poulpe.web.controller.topictype.TopicTypeView;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author Pavel Vervenko
 * @author Vahluev Vyacheslav
 */
public class TopicTypePresenterTest {

    private TopicTypePresenter presenter = new TopicTypePresenter();
    
    @Mock TopicTypeView view;
    @Mock TopicTypeService topicTypeService;
    @Mock WindowManager windowManager;
    @Mock EditListener<TopicType> listener;
    @Mock EntityValidator entityValidator;

    private static final String title = "title";
    private static final String desc = "desc";
    private static final int topicTypeId = 12;

    private TopicType activeTopicType = getFakeTopicType(topicTypeId, title, desc);

    private ValidationResult resultWithErrors = resultWithErrors();

    private ValidationResult resultWithErrors() {
        ValidationError error = new ValidationError("title", TopicType.TITLE_ALREADY_EXISTS);
        Set<ValidationError> errors = Collections.singleton(error);
        return new ValidationResult(errors);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        presenter.setTopicTypeService(topicTypeService);
        presenter.initView(view);
        presenter.setWindowManager(windowManager);
        presenter.setListener(listener);
        presenter.setEntityValidator(entityValidator);
    }

    @Test
    public void initializeForCreateTest() {
        presenter.initializeForCreate(view, listener);
        verify(view).hideEditAction();
    }

    @Test
    public void initializeForEditTestOK() throws NotFoundException {
        when(topicTypeService.get(Long.valueOf(topicTypeId))).thenReturn(activeTopicType);

        presenter.initializeForEdit(view, activeTopicType, listener);

        verify(view).hideCreateAction();
        verify(view).showTypeTitle(activeTopicType.getTitle());
        verify(view).showTypeDescription(activeTopicType.getDescription());
    }

    @Test
    public void initializeForEditTestError() throws NotFoundException {
        when(topicTypeService.get(Long.valueOf(topicTypeId))).thenThrow(new NotFoundException());

        presenter.initializeForEdit(view, activeTopicType, listener);

        verify(view).hideCreateAction();
        verify(view, never()).showTypeTitle(activeTopicType.getTitle());
        verify(view, never()).showTypeTitle(activeTopicType.getDescription());
    }

    @Test
    public void closeViewTest() {
        presenter.closeView();

        verify(windowManager).closeWindow(view);
    }

    @Test
    public void onCreateActionTestOK() {
        givenNoConstraintsViolated();

        presenter.onCreateAction();

        verify(windowManager).closeWindow(view);
        verify(listener).onCreate(any(TopicType.class));
    }

    private void givenNoConstraintsViolated() {
        when(entityValidator.validate(any(TopicType.class))).thenReturn(ValidationResult.EMPTY);
    }

    @Test
    public void onCreateActionTestEmptyName() {
        givenConstraintViolated();

        presenter.onCreateAction();

        verify(windowManager, never()).closeWindow(view);
        verify(listener, never()).onCreate(any(TopicType.class));
    }

    @Test
    public void onUpdateActionTestOK() {
        givenNoConstraintsViolated();

        presenter.onUpdateAction();

        verify(windowManager).closeWindow(any(TopicTypeView.class));
        verify(listener).onUpdate(any(TopicType.class));
    }

    @Test
    public void onUpdateActionTestEmptyName() {
        givenConstraintViolated();

        presenter.onUpdateAction();

        verify(windowManager, never()).closeWindow(view);
        verify(listener, never()).onUpdate(any(TopicType.class));
    }

    private void givenConstraintViolated() {
        when(entityValidator.validate(any(TopicType.class))).thenReturn(resultWithErrors);
    }

    @Test
    public void onCancelEditActionTest() {
        presenter.onCancelEditAction();

        verify(windowManager).closeWindow(view);
        verify(listener).onCloseEditorWithoutChanges();
    }

    @Test
    public void saveTestOK() {
        givenNoConstraintsViolated();

        presenter.save();

        verify(view, never()).validationFailure(any(ValidationResult.class));
        verify(topicTypeService).saveOrUpdate(any(TopicType.class));
    }

    @Test
    public void saveTestError() {
        givenConstraintViolated();

        presenter.save();

        verify(topicTypeService, never()).saveOrUpdate(any(TopicType.class));
    }

    @Test
    public void onTitleLoseFocusConstraintsViolated() {
        givenConstraintViolated();

        presenter.onTitleLoseFocus();

        verify(view).validationFailure(resultWithErrors);
    }
    
    @Test
    public void onTitleLoseFocusOk() {
        givenNoConstraintsViolated();
        
        presenter.onTitleLoseFocus();

        verify(view, never()).validationFailure(any(ValidationResult.class));
    }
    
        

}
