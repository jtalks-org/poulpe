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
package org.jtalks.poulpe.web.controller.section;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Tatiana Birina
 * @author Grigorev Alexey
 */
public class SectionPresenterTestValidation {

    private SectionPresenter presenter;

    @Mock SectionService service;
    @Mock ComponentService componentService;
    @Mock ZkSectionView view;
    @Mock DialogManager dialogManager;
    @Mock EntityValidator entityValidator;

    private ValidationResult resultWithErrors = resultWithErrors();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new SectionPresenter();

        presenter.setSectionService(service);
        presenter.setComponentService(componentService);
        presenter.setDialogManager(dialogManager);
        presenter.setEntityValidator(entityValidator);

        when(componentService.getByType(ComponentType.FORUM)).thenReturn(new Jcommune());
        presenter.initView(view);
    }

    private ValidationResult resultWithErrors() {
        return new ValidationResult(new ValidationError("name", PoulpeSection.SECTION_ALREADY_EXISTS));
    }

    @Test
    public void testValidateSectionEmptyName() {
        givenConstraintViolated();
        presenter.addNewSection("", "description");
        verify(service, never()).saveSection(any(PoulpeSection.class));
    }

    @Test
    public void testValidateSectionNullName() {
        givenConstraintViolated();
        presenter.addNewSection(null, "description");
        verify(service, never()).saveSection(any(PoulpeSection.class));
    }

    @Test
    public void testCheckSectionUniquenessOK() {
        givenNoConstraintsViolated();

        presenter.addNewSection("name", "description");

        verify(view, never()).validationFailure(any(ValidationResult.class));
        verify(dialogManager).confirmCreation(anyString(), any(DialogManager.Performable.class));
    }

    private void givenNoConstraintsViolated() {
        when(entityValidator.validate(any(PoulpeSection.class))).thenReturn(ValidationResult.EMPTY);
    }

    @Test
    public void testCheckSectionUniquenessNonUnique() {
        givenConstraintViolated();

        presenter.addNewSection("name", "description");

        verify(service, never()).saveSection(any(PoulpeSection.class));
        verify(dialogManager, never()).confirmCreation(anyString(), any(DialogManager.Performable.class));
    }

    private void givenConstraintViolated() {
        when(entityValidator.validate(any(PoulpeSection.class))).thenReturn(resultWithErrors);
    }

}
