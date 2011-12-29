package org.jtalks.poulpe.validation.unique;

import static org.testng.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.jtalks.poulpe.model.entity.Component;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
public class UniqueConstraintValidatorTest {
    
    private UniqueConstraintValidator validator;
    private UniquenessViolatorsRetriever uniquenessViolatorsRetriever;
    private EntityWrapper entityWrapper;
    private ConstraintValidatorContext context;
    private Component entity;

    @BeforeMethod
    public void beforeMethod() {
        validator = new UniqueConstraintValidator();
        uniquenessViolatorsRetriever = mock(UniquenessViolatorsRetriever.class);
        validator.setUniquenessViolatorsRetriever(uniquenessViolatorsRetriever);
        
        entity = new Component();
        entityWrapper = new EntityWrapper(entity);
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void retrieverChecked() {
        validator.isValid(entity, context);
        verify(uniquenessViolatorsRetriever).duplicatesFor(entityWrapper);
    }
    
    @Test
    public void isValidWhenEmpty() {
        givenNoDuplicates();
        boolean valid = validator.isValid(entity, context);
        assertTrue(valid);
    }

    private void givenNoDuplicates() {
        List<EntityWrapper> emptyList = Collections.emptyList();
        when(uniquenessViolatorsRetriever.duplicatesFor(entityWrapper)).thenReturn(emptyList);
    }
    
    @Test
    public void isNotValidWhenNotEmpty() {
        givenDuplicates();
        boolean valid = validator.isValid(entity, context);
        assertFalse(valid);
    }

    private void givenDuplicates() {
        List<EntityWrapper> emptyList = Collections.singletonList(entityWrapper);
        when(uniquenessViolatorsRetriever.duplicatesFor(entityWrapper)).thenReturn(emptyList);
    }
}
