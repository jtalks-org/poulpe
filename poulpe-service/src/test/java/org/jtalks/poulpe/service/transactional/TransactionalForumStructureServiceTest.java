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

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public class TransactionalForumStructureServiceTest {
    TransactionalForumStructureService sut;
    private SectionDao sectionDao;
    private ComponentDao componentDao;


    @BeforeMethod
    public void setUp() throws Exception {
        componentDao = mock(ComponentDao.class);
        sectionDao = mock(SectionDao.class);
        sut = new TransactionalForumStructureService(sectionDao, componentDao);
    }

    @Test(dataProvider = "provideJcommuneWithSectionsAndBranches")
    public void testDeleteSectionWithBranches(Jcommune jcommune) throws Exception {
        PoulpeSection sectionToRemove = jcommune.getSections().get(0);
        doReturn(jcommune).when(componentDao).getByType(ComponentType.FORUM);
        sut.deleteSectionWithBranches(sectionToRemove);
        assertFalse(jcommune.getSections().contains(sectionToRemove));
        verify(componentDao).saveOrUpdate(jcommune);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testDeleteSectionAndMoveBranches() throws Exception {
        sut.deleteSectionAndMoveBranches(null, null);
    }

    @Test(dataProvider = "provideJcommuneWithSectionsAndBranches")
    public void testMoveBranchInSameSection(Jcommune jcommune) {
        PoulpeSection section = jcommune.getSections().get(0);
        PoulpeBranch branch = section.getBranch(1);
        PoulpeBranch target = section.getBranch(3);
        List<PoulpeBranch> expectedBranches = new ArrayList<PoulpeBranch>(section.getPoulpeBranches());
        expectedBranches.remove(branch);
        expectedBranches.add(section.getPoulpeBranches().indexOf(target), branch);
        sut.moveBranch(branch, target);
        assertEquals(section.getPoulpeBranches(), expectedBranches);
        branch = section.getBranch(4);
        target = section.getBranch(0);
        expectedBranches.remove(branch);
        expectedBranches.add(section.getPoulpeBranches().indexOf(target), branch);
        sut.moveBranch(branch, target);
        assertEquals(section.getPoulpeBranches(), expectedBranches);
    }

    @Test(dataProvider = "provideJcommuneWithSectionsAndBranches")
    public void testMoveBranchToAnotherSection(Jcommune jcommune) {
        PoulpeSection section1 = jcommune.getSections().get(0);
        PoulpeSection section2 = jcommune.getSections().get(1);
        PoulpeBranch branch = section1.getBranch(0);
        PoulpeBranch target = section2.getBranch(0);
        List<PoulpeBranch> expectedBranchesInSection1 = new ArrayList<PoulpeBranch>(section1.getPoulpeBranches());
        List<PoulpeBranch> expectedBranchesInSection2 = new ArrayList<PoulpeBranch>(section2.getPoulpeBranches());
        expectedBranchesInSection1.remove(branch);
        expectedBranchesInSection2.add(0, branch);
        sut.moveBranch(branch, target);
        assertEquals(section1.getBranches(), expectedBranchesInSection1);
        assertEquals(section2.getBranches(), expectedBranchesInSection2);
        target = section1.getBranch(0);
        expectedBranchesInSection2.remove(branch);
        expectedBranchesInSection1.add(0, branch);
        sut.moveBranch(branch, target);
        assertEquals(section1.getBranches(), expectedBranchesInSection1);
        assertEquals(section2.getBranches(), expectedBranchesInSection2);
    }

    @Test(dataProvider = "provideJcommuneWithSectionsAndBranches")
    public void testGetJcommune(Jcommune jcommune) {
        doReturn(jcommune).when(componentDao).getByType(ComponentType.FORUM);
        Jcommune jcommuneFromService = sut.getJcommune();
        assertEquals(jcommuneFromService, jcommune);
        verify(componentDao).getByType(ComponentType.FORUM);
    }

    @Test(dataProvider = "provideJcommuneWithSectionsAndBranches")
    public void testSaveJcommune(Jcommune jcommune) {
        sut.saveJcommune(jcommune);
        verify(componentDao).saveOrUpdate(jcommune);
    }

    @Test(dataProvider = "provideJcommuneWithSectionsAndBranches")
    public void testRemoveBranch(Jcommune jcommune) {
        PoulpeSection section = jcommune.getSections().get(0);
        PoulpeBranch branchToremove = section.getBranch(0);
        sut.removeBranch(branchToremove);
        assertFalse(section.getBranches().contains(branchToremove));
        verify(sectionDao).saveOrUpdate(section);
    }

    @DataProvider
    private Object[][] provideJcommuneWithSectionsAndBranches() {
        Jcommune jcommune = TestFixtures.jcommune();
        
        PoulpeSection sectionA = new PoulpeSection("SectionA");
        for (int i = 0; i < 5; i++) {
            sectionA.addOrUpdateBranch(createBranch(sectionA, "Branch" + i));
        }
        jcommune.addOrUpdateSection(sectionA);
        PoulpeSection sectionB = new PoulpeSection("SectionB");
        for (int i = 5; i < 10; i++) {
            sectionB.addOrUpdateBranch(createBranch(sectionB, "Branch" + i));
        }
        jcommune.addOrUpdateSection(sectionB);
        return new Object[][]{{jcommune}};
    }

    private PoulpeBranch createBranch(PoulpeSection section, String branchName) {
        PoulpeBranch branch = new PoulpeBranch(branchName);
        branch.setId(new Random().nextLong());
        branch.setSection(section);
        return branch;
    }
}
