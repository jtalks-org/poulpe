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
package org.jtalks.poulpe.web.controller.group;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.GroupService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EditGroupDialogPresenterTest {
    private EditGroupDialogPresenter dialogPresenter;
    @Mock
    private GroupService mockGroupService;
    @Mock
    private EditGroupDialogView mockView;
    
    @Mock 
    EntityValidator entityValidator;
    private Group group;

    private ValidationResult resultWithErrors = resultWithErrors();

    private ValidationResult resultWithErrors() {
        ValidationError error = new ValidationError("title", TopicType.TITLE_ALREADY_EXISTS);
        Set<ValidationError> errors = Collections.singleton(error);
        return new ValidationResult(errors);
    }
    
    @BeforeMethod
    public void beforeMethod() {
        dialogPresenter = new EditGroupDialogPresenter();
        MockitoAnnotations.initMocks(this);
        group = new Group();
        dialogPresenter.setGroupService(mockGroupService);
        dialogPresenter.setEntityValidator(entityValidator);
    }

    @Test
    public void testValidateWhenNameIsNull() {
        givenConstraintViolated();
        dialogPresenter.initView(mockView, group);
        dialogPresenter.saveOrUpdateGroup(null, "description");
        verify(mockGroupService, never()).saveGroup(any(Group.class));
    }
    
    @Test
    public void testValidateWhenNameIsEmpty() {
        givenConstraintViolated();
        dialogPresenter.initView(mockView, group);
        dialogPresenter.saveOrUpdateGroup("", "description");
        verify(mockGroupService, never()).saveGroup(any(Group.class));
    }

    @Test
    public void testValidateWhenNameIsLong() {
        givenConstraintViolated();
        String longName = new String(new byte[255]);
        dialogPresenter.initView(mockView, group);
        dialogPresenter.saveOrUpdateGroup(longName, "description");
        verify(mockGroupService, never()).saveGroup(any(Group.class));
    }

    @Test
    public void testSaveOrUpdate() {
        givenNoConstraintsViolated();
        String name = "name";
        String desc = "description";
        dialogPresenter.initView(mockView, group);
        dialogPresenter.saveOrUpdateGroup(name, desc);
        verify(mockView, never()).validationFailure(any(ValidationResult.class));
        verify(mockGroupService).saveGroup(any(Group.class));
    }
    
    private void givenNoConstraintsViolated() {
        when(entityValidator.validate(any(TopicType.class))).thenReturn(ValidationResult.EMPTY);
    }

    @Test
    public void testSaveOrUpdateGroupWithException() {
        givenConstraintViolated();
        Group nullGroup = new Group();
        
        final String name = "";
        final String desc = "description";

        dialogPresenter.initView(mockView, nullGroup);
        dialogPresenter.setGroupService(mockGroupService);
        dialogPresenter.saveOrUpdateGroup(name, desc);
        verify(mockGroupService, never()).saveGroup(any(Group.class));
    }
    
    private void givenConstraintViolated() {
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(resultWithErrors);
    }

}
