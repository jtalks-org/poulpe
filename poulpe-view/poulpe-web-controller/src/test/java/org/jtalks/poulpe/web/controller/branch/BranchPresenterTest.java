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
package org.jtalks.poulpe.web.controller.branch;

import static org.jtalks.poulpe.web.controller.branch.BranchPresenter.GROUP_SUFFIX;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.SectionService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Bekrenev Dmitry
 * */
public class BranchPresenterTest {
    private static final String BRANCH_NAME = "TestBranch";
    private static final String BRANCH_NEW_NAME = "NewTestBranch2";
    private static final String GROUP_NAME = BRANCH_NAME + GROUP_SUFFIX;
    private PoulpeSection section = new PoulpeSection("sectionName", "sectionDescription");
    @InjectMocks
    BranchPresenter presenter = new BranchPresenter();
    @Mock
    SectionService sectionService;
    @Mock
    BranchService branchService;
    @Mock
    GroupService groupService;
    @Mock
    BranchDialogView view;
    @Mock 
    EntityValidator entityValidator;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitView() {
        List<PoulpeSection> sections = Collections.nCopies(4, section);
        when(sectionService.getAll()).thenReturn(sections);

        presenter.initView();

        verify(view).initSectionList(sections);
    }

    @Test
    public void testSaveBranch() {
        givenNoConstraintsViolated();
        PoulpeBranch branch = new PoulpeBranch(BRANCH_NAME);
        branch.setSection(section);
        
        presenter.saveBranch(branch);
        
        Group group = branch.getGroup();
        assertEquals(group.getName(), BRANCH_NAME + GROUP_SUFFIX);
        verify(view, never()).validationFailure(any(ValidationResult.class));
        verify(sectionService).saveSection(any(PoulpeSection.class));
        verify(groupService).getAllMatchedByName(any(String.class));
        verify(branchService, times(3)).changeGrants(any(PoulpeBranch.class), any(PermissionChanges.class));
    }

    @Test
    public void testRenameBranch() {
        PoulpeBranch branch = createNewBranch();
        branch.setName(BRANCH_NEW_NAME);
        
        presenter.saveBranch(branch);
        
        Group group = branch.getGroup();
        assertEquals(group.getName(), BRANCH_NEW_NAME + GROUP_SUFFIX);
        verify(view, never()).validationFailure(any(ValidationResult.class));
        verify(sectionService, times(2)).saveSection(any(PoulpeSection.class));
        verify(branchService, times(6)).changeGrants(any(PoulpeBranch.class), any(PermissionChanges.class));
    }

    @Test
    public void testSaveBranchWithExistingMatchingGroup() {
        givenNoConstraintsViolated();
        Group group = createMatchingGroup();
        PoulpeBranch branch = new PoulpeBranch(BRANCH_NAME);
        branch.setSection(section);
        
        presenter.saveBranch(branch);
        
        Group existGroup = branch.getGroup();
        assertEquals(existGroup.getName(), GROUP_NAME);
        assertEquals(group, existGroup);
        verify(view, never()).validationFailure(any(ValidationResult.class));
        verify(sectionService).saveSection(any(PoulpeSection.class));
        verify(groupService).getAllMatchedByName(any(String.class));
        verify(branchService, times(3)).changeGrants(any(PoulpeBranch.class), any(PermissionChanges.class));
    }

    private Group createMatchingGroup() {
        Group group = new Group(GROUP_NAME, "");
        List<Group> groups = new ArrayList<Group>();
        groups.add(group);
        when(groupService.getAllMatchedByName(GROUP_NAME)).thenReturn(groups);
        return group;
    }

    @Test
    public void testSaveBranchWhenBranchExceptionHappen()  {
        PoulpeBranch branch = new PoulpeBranch();
        givenBranchConstraintViolated();
        
        presenter.saveBranch(branch);
        
        verify(view).validationFailure(any(ValidationResult.class));
        verify(sectionService, never()).saveSection(any(PoulpeSection.class));
        verify(branchService, never()).changeGrants(any(PoulpeBranch.class), any(PermissionChanges.class));
    }
    
    @Test
    public void testSaveBranchWhenGroupExceptionHappen()  {
        PoulpeBranch branch = new PoulpeBranch();
        givenGroupConstraintViolated();
        
        presenter.saveBranch(branch);
        
        verify(view).validationFailure(any(ValidationResult.class));
        verify(sectionService, never()).saveSection(any(PoulpeSection.class));
        verify(branchService, never()).changeGrants(any(PoulpeBranch.class), any(PermissionChanges.class));
    }

    private PoulpeBranch createNewBranch() {
        givenNoConstraintsViolated();
        PoulpeBranch branch = new PoulpeBranch(BRANCH_NAME);
        branch.setSection(section);
        presenter.saveBranch(branch);
        return branch;
    }
    
    private void givenNoConstraintsViolated() {
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(ValidationResult.EMPTY);
    }

    private void givenBranchConstraintViolated() {
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(resultWithBranchErrors);
    }

    private ValidationResult resultWithBranchErrors = resultWithBranchErrors();

    private ValidationResult resultWithBranchErrors() {
        ValidationError error = new ValidationError("name", PoulpeBranch.BRANCH_ALREADY_EXISTS);
        Set<ValidationError> errors = Collections.singleton(error);
        return new ValidationResult(errors);
    }

    private void givenGroupConstraintViolated() {
        when(entityValidator.validate(any(Group.class))).thenReturn(resultWithGroupErrors);
    }

    private ValidationResult resultWithGroupErrors = resultWithGroupErrors();

    private ValidationResult resultWithGroupErrors() {
        ValidationError error = new ValidationError("group", Group.GROUP_ALREADY_EXISTS);
        Set<ValidationError> errors = Collections.singleton(error);
        return new ValidationResult(errors);
    }

}
