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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.SectionService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Bekrenev Dmitry
 * */
public class BranchPresenterTest {
    @Mock
    SectionService service;
    @Mock
    BranchDialogView view;
    @Mock
    BranchService branchService;
    @Mock 
    EntityValidator entityValidator;
    private PoulpeSection section = new PoulpeSection("sectionName", "sectionDescription");
    BranchPresenter presenter = new BranchPresenter();
    
    private ValidationResult resultWithErrors = resultWithErrors();

    private ValidationResult resultWithErrors() {
        ValidationError error = new ValidationError("name", PoulpeBranch.BRANCH_ALREADY_EXISTS);
        Set<ValidationError> errors = Collections.singleton(error);
        return new ValidationResult(errors);
    }

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter.setSectionService(service);
        presenter.setView(view);
        presenter.setBranchService(branchService);
        presenter.setEntityValidator(entityValidator);
    }

    @Test
    public void testInitView() {
        List<PoulpeSection> sections = Collections.nCopies(4, section);
        when(service.getAll()).thenReturn(sections);

        presenter.initView();

        verify(view).initSectionList(sections);
    }

    @Test
    public void testSaveBranch() {
        givenNoConstraintsViolated();
        
        PoulpeBranch branch = new PoulpeBranch();
        branch.setSection(new PoulpeSection());
        presenter.saveBranch(branch);
        verify(view, never()).validationFailure(any(ValidationResult.class));
        verify(service).saveSection(any(PoulpeSection.class));
    }
    
    private void givenNoConstraintsViolated() {
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(ValidationResult.EMPTY);
    }

  @Test
    public void testSaveBranchWhenExceptionHappen()  {
       PoulpeBranch branch = new PoulpeBranch();
       givenConstraintViolated();
       
        presenter.saveBranch(branch);
        verify(branchService, never()).saveBranch(any(PoulpeBranch.class));
    }

    private void givenConstraintViolated() {
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(resultWithErrors);
    }

}
