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
package org.jtalks.poulpe.logic;

import com.google.common.collect.Lists;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.UserGroupSid;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.permissions.BranchPermission;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagerTest {
    @Mock
    GroupDao groupDao;
    @Mock
    AclManager aclManager;
    BranchPermissionManager manager;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        manager = new BranchPermissionManager(aclManager, groupDao);
    }

    @Test(dataProvider = "accessChanges")
    public void testChangeGrants(BranchAccessChanges accessChanges) throws Exception {
        Branch branch = new Branch("test branch");
        manager.changeGrants(branch, accessChanges);
        List<? extends Sid> removedGroupSids = UserGroupSid.create(accessChanges.getRemovedPermissionsAsArray());
        List<? extends Sid> grantedGroupSids = UserGroupSid.create(accessChanges.getNewlyAddedPermissionsAsArray());
        verify(aclManager).delete(removedGroupSids, createFromArray(accessChanges.getPermission()), branch);
        verify(aclManager).grant(grantedGroupSids, createFromArray(accessChanges.getPermission()), branch);
    }

    @Test(dataProvider = "accessChanges")
    public void testChangeRestriction(BranchAccessChanges accessChanges) throws Exception {
        Branch branch = new Branch("test branch");
        manager.changeRestrictions(branch, accessChanges);
        List<? extends Sid> removedGroupSids = UserGroupSid.create(accessChanges.getRemovedPermissionsAsArray());
        List<? extends Sid> grantedGroupSids = UserGroupSid.create(accessChanges.getNewlyAddedPermissionsAsArray());
        verify(aclManager).delete(removedGroupSids, createFromArray(accessChanges.getPermission()), branch);
        verify(aclManager).restrict(grantedGroupSids, createFromArray(accessChanges.getPermission()), branch);
    }

    @Test
    public void testGetGroupAccessListFor() throws Exception {
//        when(aclManager.getBranchPermissions(branch)).thenReturn()
    }

    @DataProvider
    public Object[][] accessChanges() {
        BranchAccessChanges accessChanges = new BranchAccessChanges(BranchPermission.CREATE_TOPICS);
        accessChanges.setNewlyAddedGroups(newArrayList(new Group("new1"), new Group("new2")));
        accessChanges.setRemovedGroups(newArrayList(new Group("removed1"), new Group("removed2")));
        return new Object[][]{{accessChanges}};
    }

    @DataProvider
    public Object[][] provide() {
        return new Object[][]{{new Branch()}};
    }

    private List<Permission> createFromArray(Permission... permissions) {
        return Lists.newArrayList(permissions);
    }
}
