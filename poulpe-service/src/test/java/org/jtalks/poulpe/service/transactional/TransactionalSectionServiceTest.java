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
import static org.mockito.Mockito.eq;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.validation.EntityValidator;
import org.jtalks.poulpe.validation.ValidationError;
import org.jtalks.poulpe.validation.ValidationException;
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
        Section victim = buildFakeSection();
        when(sectionDao.deleteRecursively(victim)).thenReturn(expected);
        boolean actual = sectionService.deleteRecursively(victim);
        verify(sectionDao).deleteRecursively(victim);
        assertEquals(actual, expected);
    }

    @Test
    public void deleteAndMoveBranchesToTest() {
        Section victim = buildFakeSection(SECTION_ID);
        Section recipient = buildFakeSection(SECTION_ID + 1);
        boolean expected = true;
        when(sectionDao.deleteAndMoveBranchesTo(victim, recipient)).thenReturn(expected);
        boolean actual = sectionService.deleteAndMoveBranchesTo(victim, recipient);
        verify(sectionDao).deleteAndMoveBranchesTo(victim, recipient);
        assertEquals(actual, expected);
    }

    @Test (expectedExceptions=IllegalArgumentException.class)
    public void deleteAndMoveBranchesToExceptionTest() {
        final long victimId = SECTION_ID;
        final long recipientId = victimId;
        Section victim = buildFakeSection(victimId);
        Section recipient = buildFakeSection(recipientId);
        boolean expected = true;
        when(sectionDao.deleteAndMoveBranchesTo(victim, recipient)).thenReturn(expected);
        boolean actual = sectionService.deleteAndMoveBranchesTo(victim, recipient);
        verify(sectionDao).deleteAndMoveBranchesTo(victim, recipient);
        assertEquals(actual, expected);
    }

    @Test
    public void testSaveSection(){
         Section section = buildFakeSection();
         sectionService.saveSection(section);
         verify(entityValidator).throwOnValidationFailure(section);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void saveSectionWithConstraintsViolations() {
        Section section = buildFakeSection();
        givenConstraintsViolations();
        sectionService.saveSection(section);
    }

    @Test
    public void testGetAll() {
        sectionService.getAll();
        verify(sectionDao).getAll();
    }

    @Test
    public void testIsSectionExists() {
        Section section = buildFakeSection();
        sectionService.isSectionExists(section);
        verify(sectionDao).isSectionNameExists(section);
    }

    private void givenConstraintsViolations() {
        Set<ValidationError> dontCare = Collections.<ValidationError> emptySet();
        doThrow(new ValidationException(dontCare)).when(entityValidator).throwOnValidationFailure(any(Section.class));
    }

    private Section buildFakeSection() {
        return buildFakeSection(SECTION_ID);
    }

    private Section buildFakeSection(long id) {
        Section section = new Section(RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(20));
        section.setId(id);
        return section;
    }

}
