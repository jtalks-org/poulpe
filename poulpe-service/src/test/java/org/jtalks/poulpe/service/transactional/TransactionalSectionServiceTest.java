/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.service.transactional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The test for {@link TransactionalSectionService}.
 * @author Dmitriy Sukharev
 */
public class TransactionalSectionServiceTest {

    private static final long SECTION_ID = 1L;
    private SectionDao sectionDao;
    private SectionService sectionService;

    @BeforeMethod
    public void setUp() {
        sectionDao = mock(SectionDao.class);
        sectionService = new TransactionalSectionService(sectionDao);
    }

    @Test
    public void deleteRecursivelyTest() {
        boolean expected = true;
        Section victim = mock(Section.class);
        when(victim.getId()).thenReturn(SECTION_ID);
        when(sectionDao.deleteRecursively(SECTION_ID)).thenReturn(expected);
        boolean actual = sectionService.deleteRecursively(victim);
        verify(sectionDao).deleteRecursively(SECTION_ID);
        assertEquals(actual, expected);
    }

    @Test
    public void deleteAndMoveBranchesToTest() {
        final long victimId = SECTION_ID;
        final long recipientId = SECTION_ID + 1;
        Section victim = mock(Section.class);
        Section recipient = mock(Section.class);
        when(victim.getId()).thenReturn(victimId);
        when(recipient.getId()).thenReturn(recipientId);
        boolean expected = true;
        when(sectionDao.deleteAndMoveBranchesTo(victimId, recipientId)).thenReturn(expected);
        boolean actual = sectionService.deleteAndMoveBranchesTo(victim, recipient);
        verify(sectionDao).deleteAndMoveBranchesTo(victimId, recipientId);
        assertEquals(actual, expected);
    }

    @Test (expectedExceptions=IllegalArgumentException.class)
    public void deleteAndMoveBranchesToExceptionTest() {
        final long victimId = SECTION_ID;
        final long recipientId = victimId;
        Section victim = mock(Section.class);
        Section recipient = mock(Section.class);
        when(victim.getId()).thenReturn(victimId);
        when(recipient.getId()).thenReturn(recipientId);
        boolean expected = true;
        when(sectionDao.deleteAndMoveBranchesTo(victimId, recipientId)).thenReturn(expected);
        boolean actual = sectionService.deleteAndMoveBranchesTo(victim, recipient);
        verify(sectionDao).deleteAndMoveBranchesTo(victimId, recipientId);
        assertEquals(actual, expected);
    }
}
