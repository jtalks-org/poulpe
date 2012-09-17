/**
 * Copyright (C) 2012  JTalks.org Team
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
package org.jtalks.poulpe.logic;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.SecurityGroupList;
import org.jtalks.poulpe.service.GroupService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 * @author Evgeny Surovtsev
 */
public class SecurityGroupListForBranchPermissionTest {
	SecurityGroupListForBranchPermission securityGroupListForBranchPermission;
	GroupService groupService = null;
	SecurityGroupList securityGroupList = null;
	
	@BeforeClass
	public void beforeClass() {
		groupService = mock(GroupService.class);
		securityGroupListForBranchPermission = new SecurityGroupListForBranchPermission(groupService);
	}
	
	@BeforeMethod
    public void beforeMethod() {
		securityGroupList = mock(SecurityGroupList.class);
		doReturn(securityGroupList).when(groupService).getSecurityGroups();
		doReturn(null).when(securityGroupList).getAllGroups();
    }

	/**
     * For all of branchPermissionsForAnonymous values an Anonymous group shall be kept
     */
    @Test(dataProvider = "getBranchPermissionsForAnonymous")
    public void permissionsForAnonymousTest(BranchPermission branchPermission) {
    	securityGroupListForBranchPermission.getSecurityGroupList(branchPermission);
    	verify(securityGroupList).getAllGroups();
    	verify(securityGroupList, never()).removeAnonymousGroup();
    }
    
    /**
     * For all of branchPermissionsForNotAnonymous values an Anonymous group shall be removed
     */
    @Test(dataProvider = "getBranchPermissionsForNotAnonymous")
    public void permissionsForNotAnonymousTest(BranchPermission branchPermission) {
    	securityGroupListForBranchPermission.getSecurityGroupList(branchPermission);
    	verify(securityGroupList).getAllGroups();
    	verify(securityGroupList).removeAnonymousGroup();
    }
    
    @DataProvider
    public Object[][] getBranchPermissionsForAnonymous() {
        return new Object[][] {
        		{BranchPermission.VIEW_TOPICS},
        };
    }
    
    /**
     * Get an Object[][] with all BranchPermission values with exception of BranchPermissionsForAnonymous values
     */
    @DataProvider
    public Object[][] getBranchPermissionsForNotAnonymous() { 
    	List<BranchPermission> branchPermissionList = BranchPermission.getAllAsList();
    	for (Object[] branchPermissionForAnonymous: getBranchPermissionsForAnonymous()) {
    		branchPermissionList.remove((BranchPermission) branchPermissionForAnonymous[0]);
    	}
    	
    	Object[][] branchPermissionsForNotAnonymous = new Object[branchPermissionList.size()][];
    	for (int i = 0; i < branchPermissionList.size(); i++) {
    		Object[] branchPermissionArray = new Object[1];
    		branchPermissionArray[0] = branchPermissionList.get(i);
    		branchPermissionsForNotAnonymous[i] = branchPermissionArray;
    	}
    	return branchPermissionsForNotAnonymous;
    }
}
