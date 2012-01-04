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
