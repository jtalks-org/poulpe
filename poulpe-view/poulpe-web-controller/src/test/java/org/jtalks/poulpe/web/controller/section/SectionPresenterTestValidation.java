package org.jtalks.poulpe.web.controller.section;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.validation.EntityValidator;
import org.jtalks.poulpe.validation.ValidationError;
import org.jtalks.poulpe.validation.ValidationResult;
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
    @Mock SectionViewImpl view;
    @Mock DialogManager dialogManager;
    @Mock EntityValidator entityValidator;

    private ValidationResult resultWithErrors = resultWithErrors();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new SectionPresenter();

        presenter.setSectionService(service);
        presenter.setDialogManager(dialogManager);
        presenter.setEntityValidator(entityValidator);

        presenter.initView(view);
    }

    private ValidationResult resultWithErrors() {
        return new ValidationResult(new ValidationError("name", Section.SECTION_ALREADY_EXISTS));
    }

    @Test
    public void testValidateSectionEmptyName() {
        givenConstraintViolated();
        presenter.addNewSection("", "description");
        verify(service, never()).saveSection(any(Section.class));
    }

    @Test
    public void testValidateSectionNullName() {
        givenConstraintViolated();
        presenter.addNewSection(null, "description");
        verify(service, never()).saveSection(any(Section.class));
    }

    @Test
    public void testCheckSectionUniquenessOK() {
        givenNoConstraintsViolated();

        presenter.addNewSection("name", "description");

        verify(view, never()).validationFailure(any(ValidationResult.class), anyBoolean());
        verify(dialogManager).confirmCreation(anyString(), any(DialogManager.Performable.class));
    }

    private void givenNoConstraintsViolated() {
        when(entityValidator.validate(any(Section.class))).thenReturn(ValidationResult.EMPTY);
    }

    @Test
    public void testCheckSectionUniquenessNonUnique() {
        givenConstraintViolated();

        presenter.addNewSection("name", "description");

        verify(service, never()).saveSection(any(Section.class));
        verify(dialogManager, never()).confirmCreation(anyString(), any(DialogManager.Performable.class));
    }

    private void givenConstraintViolated() {
        when(entityValidator.validate(any(Section.class))).thenReturn(resultWithErrors);
    }

}
