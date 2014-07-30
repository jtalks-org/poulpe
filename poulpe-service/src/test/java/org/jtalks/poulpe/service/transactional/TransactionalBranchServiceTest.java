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

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * This test class is intended to test all topic-related forum branch facilities
 *
 * @author Kravchenko Vitaliy
 * @author Kirill Afonin
 */
public class TransactionalBranchServiceTest {

    private BranchService branchService;

    @Mock
    BranchDao branchDao;

    private long BRANCH_ID = 1L;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        branchService = new TransactionalBranchService(branchDao);
    }

    @Test
    public void testGet() throws NotFoundException {
        PoulpeBranch expectedBranch = new PoulpeBranch();
        when(branchDao.isExist(BRANCH_ID)).thenReturn(true);
        when(branchDao.get(BRANCH_ID)).thenReturn(expectedBranch);

        PoulpeBranch branch = branchService.get(BRANCH_ID);

        assertEquals(branch, expectedBranch, "Branches aren't equals");
        verify(branchDao).isExist(BRANCH_ID);
        verify(branchDao).get(BRANCH_ID);
    }

    @Test(expectedExceptions = {NotFoundException.class})
    public void testGetIncorrectId() throws NotFoundException {
        when(branchDao.isExist(BRANCH_ID)).thenReturn(false);
        branchService.get(BRANCH_ID);
    }


    @Test
    public void testDeleteBranchMovingTopic() {
        PoulpeBranch victim = new PoulpeBranch("Victim");
        PoulpeBranch recepient = new PoulpeBranch("Recepient");

        branchService.deleteBranchMovingTopics(victim, recepient);

        verify(branchDao).delete(victim.getId());

    }

    @Test
    public void getAllTest() {
        List<PoulpeBranch> expectedBranchList = new ArrayList<PoulpeBranch>();
        expectedBranchList.add(new PoulpeBranch());
        when(branchDao.getAll()).thenReturn(expectedBranchList);
        List<PoulpeBranch> actualBranchList = branchService.getAll();
        assertEquals(actualBranchList, expectedBranchList);
        verify(branchDao).getAll();
    }

    @Test
    public void testDeleteBranch() {
        PoulpeBranch branch = new PoulpeBranch();
        branchService.deleteBranchRecursively(branch);
        verify(branchDao).delete(branch.getId());
    }

    @Test
    public void testSaveBranchWitoutException() {
        PoulpeBranch branch = new PoulpeBranch();
        branch.setName("new branch");
        branch.setDescription("new description");
        ArgumentCaptor<PoulpeBranch> branchCaptor = ArgumentCaptor.forClass(PoulpeBranch.class);

        branchService.saveBranch(branch);

        verify(branchDao).saveOrUpdate(branchCaptor.capture());
        assertEquals(branchCaptor.getValue().getName(), "new branch");
    }

}







