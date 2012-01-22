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

import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.logic.BranchPermissionManager;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.validation.EntityValidator;
import org.jtalks.poulpe.validation.ValidationError;
import org.jtalks.poulpe.validation.ValidationException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.naming.spi.ObjectFactory;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * This test class is intended to test all topic-related forum branch facilities
 * 
 * @author Kravchenko Vitaliy
 * @author Kirill Afonin
 */
public class TransactionalBranchServiceTest {

    @Mock
    BranchDao branchDao;
    @Mock
    EntityValidator entityValidator;
    @Mock
    AclManager aclManager;
    @Mock
    BranchPermissionManager branchPermissionManager;
    private long BRANCH_ID = 1L;
    private BranchService branchService;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        branchService = new TransactionalBranchService(branchDao, branchPermissionManager, entityValidator);
    }

    @Test
    public void testGet() throws NotFoundException {
        Branch expectedBranch = new Branch();
        when(branchDao.isExist(BRANCH_ID)).thenReturn(true);
        when(branchDao.get(BRANCH_ID)).thenReturn(expectedBranch);

        Branch branch = branchService.get(BRANCH_ID);

        assertEquals(branch, expectedBranch, "Branches aren't equals");
        verify(branchDao).isExist(BRANCH_ID);
        verify(branchDao).get(BRANCH_ID);
    }

    @Test
    public void testChangeGrants() {
        BranchAccessChanges accessChanges = new BranchAccessChanges(null);
        Branch branch = new Branch("name");

        branchService.changeGrants(branch, accessChanges);

        verify(branchPermissionManager).changeGrants(branch, accessChanges);
    }

    @Test(expectedExceptions = { NotFoundException.class })
    public void testGetIncorrectId() throws NotFoundException {
        when(branchDao.isExist(BRANCH_ID)).thenReturn(false);
        branchService.get(BRANCH_ID);
    }

    @Test
    public void testGetGroupAccessListFor() {
        Branch branch = new Branch();

        branchService.getGroupAccessListFor(branch);

        verify(branchPermissionManager).getGroupAccessListFor(branch);

    }

    @Test
    public void testChangeRestrictions() {
        BranchAccessChanges accessChanges = new BranchAccessChanges(null);
        Branch branch = new Branch("name");

        branchService.changeRestrictions(branch, accessChanges);
        
        verify(branchPermissionManager).changeRestrictions(branch, accessChanges);
    }

    @Test
    public void testIsDuplicated() {
        Branch branch = new Branch();

        when(branchDao.isBranchDuplicated(branch)).thenReturn(true);

        assertTrue(branchService.isDuplicated(branch));
    }

    @Test
    public void testDeleteBranchMovingTopic() {
        Branch victim = new Branch("Victim");
        Branch recepient = new Branch("Recepient");

        branchService.deleteBranchMovingTopics(victim, recepient);

        verify(branchDao).delete(victim.getId());

    }

    @Test
    public void getAllTest() {
        List<Branch> expectedBranchList = new ArrayList<Branch>();
        expectedBranchList.add(new Branch());
        when(branchDao.getAll()).thenReturn(expectedBranchList);
        List<Branch> actualBranchList = branchService.getAll();
        assertEquals(actualBranchList, expectedBranchList);
        verify(branchDao).getAll();
    }

    @Test
    public void testDeleteBranch() {
        Branch branch = new Branch();
        branchService.deleteBranchRecursively(branch);
        verify(branchDao).delete(branch.getId());
    }

    @Test
    public void testSaveBranchWitoutException() {
        Branch branch = new Branch();
        branch.setName("new branch");
        branch.setDescription("new description");
        ArgumentCaptor<Branch> branchCaptor = ArgumentCaptor.forClass(Branch.class);

        branchService.saveBranch(branch);

        verify(branchDao).saveOrUpdate(branchCaptor.capture());
        assertEquals(branchCaptor.getValue().getName(), "new branch");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testSaveBranchWithException() {
        Branch branch = new Branch();

        givenConstraintsViolations();
        branchService.saveBranch(branch);
    }

    private void givenConstraintsViolations() {
        Set<ValidationError> dontCare = Collections.<ValidationError> emptySet();
        doThrow(new ValidationException(dontCare)).when(entityValidator).throwOnValidationFailure(any(Branch.class));
    }
}
