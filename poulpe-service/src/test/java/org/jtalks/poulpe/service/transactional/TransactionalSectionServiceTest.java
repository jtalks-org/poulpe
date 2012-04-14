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
package org.jtalks.poulpe.service.transactional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationException;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.SectionService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The test for {@link TransactionalSectionService}.
 * @author Dmitriy Sukharev
 * @author Vahluev Vyacheslav
 * @author Vyacheslav Zhivaev
 */
public class TransactionalSectionServiceTest {
    @Mock EntityValidator entityValidator;
    @Mock SectionDao sectionDao;
    private static final long SECTION_ID = 1L;
    private SectionService sectionService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sectionService = new TransactionalSectionService(sectionDao, entityValidator);
    }

    @Test
    public void deleteRecursivelyTest() {
        boolean expected = true;
        PoulpeSection victim = buildFakeSection();
        when(sectionDao.deleteRecursively(victim)).thenReturn(expected);
        boolean actual = sectionService.deleteRecursively(victim);
        verify(sectionDao).deleteRecursively(victim);
        assertEquals(actual, expected);
    }

    @Test
    public void deleteAndMoveBranchesToTest() {
        PoulpeSection victim = buildFakeSection(SECTION_ID);
        PoulpeSection recipient = buildFakeSection(SECTION_ID + 1);
        boolean expected = true;
        when(sectionDao.deleteAndMoveBranchesTo(victim, recipient)).thenReturn(expected);
        boolean actual = sectionService.deleteAndMoveBranchesTo(victim, recipient);
        verify(sectionDao).deleteAndMoveBranchesTo(victim, recipient);
        assertEquals(actual, expected);
    }

    @Test (expectedExceptions=IllegalArgumentException.class)
    public void deleteAndMoveBranchesToExceptionTest() {
        final long victimId = SECTION_ID;
        PoulpeSection victim = buildFakeSection(victimId);
        PoulpeSection recipient = buildFakeSection(victimId);
        boolean expected = true;
        when(sectionDao.deleteAndMoveBranchesTo(victim, recipient)).thenReturn(expected);
        boolean actual = sectionService.deleteAndMoveBranchesTo(victim, recipient);
        verify(sectionDao).deleteAndMoveBranchesTo(victim, recipient);
        assertEquals(actual, expected);
    }

    @Test
    public void testSaveSection(){
         PoulpeSection section = buildFakeSection();
         sectionService.saveSection(section);
         verify(entityValidator).throwOnValidationFailure(section);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void saveSectionWithConstraintsViolations() {
        PoulpeSection section = buildFakeSection();
        givenConstraintsViolations();
        sectionService.saveSection(section);
    }

    @Test
    public void testGetAll() {
        sectionService.getAll();
        verify(sectionDao).getAll();
    }

    private void givenConstraintsViolations() {
        Set<ValidationError> dontCare = Collections.emptySet();
        doThrow(new ValidationException(dontCare)).when(entityValidator).throwOnValidationFailure(any(PoulpeSection.class));
    }

    private PoulpeSection buildFakeSection() {
        return buildFakeSection(SECTION_ID);
    }

    private PoulpeSection buildFakeSection(long id) {
        PoulpeSection section = new PoulpeSection(RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(20));
        section.setId(id);
        return section;
    }

}
