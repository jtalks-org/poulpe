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

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.UserGroupSid;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.acls.model.Permission;
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
    PermissionManager manager;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        manager = new PermissionManager(aclManager, groupDao);
    }

    @Test(dataProvider = "accessChanges")
    public void testChangeGrants(PermissionChanges changes) throws Exception {
        PoulpeBranch branch = new PoulpeBranch("test branch", "");
        manager.changeGrants(branch, changes);
        verify(aclManager).delete(getRemovedSids(changes), listFromArray(changes.getPermission()), branch);
        verify(aclManager).grant(getNewlyAddedSids(changes), listFromArray(changes.getPermission()), branch);
    }

    @Test(dataProvider = "accessChanges")
    public void testChangeRestriction(PermissionChanges changes) throws Exception {
        PoulpeBranch branch = new PoulpeBranch("test branch");
        manager.changeRestrictions(branch, changes);
        verify(aclManager).delete(getRemovedSids(changes), listFromArray(changes.getPermission()), branch);
        verify(aclManager).restrict(getNewlyAddedSids(changes), listFromArray(changes.getPermission()), branch);
    }

    @Test
    public void testGetGroupAccessListFor() throws Exception {
        PoulpeBranch branch = new PoulpeBranch();


//        when(aclManager.getBranchPermissions(branch)).thenReturn()
    }

    @DataProvider
    public Object[][] accessChanges() {
        PermissionChanges accessChanges = new PermissionChanges(BranchPermission.CREATE_TOPICS);
        accessChanges.addNewlyAddedGroups(newArrayList(new PoulpeGroup("new1"), new PoulpeGroup("new2")));
        accessChanges.addRemovedGroups(newArrayList(new PoulpeGroup("removed1"), new PoulpeGroup("removed2")));
        return new Object[][]{{accessChanges}};
    }

    @DataProvider
    public Object[][] provide() {
        return new Object[][]{{new PoulpeBranch()}};
    }

    private List<UserGroupSid> getNewlyAddedSids(PermissionChanges accessChanges) {
        return UserGroupSid.create(accessChanges.getNewlyAddedGroupsAsArray());
    }

    private List<UserGroupSid> getRemovedSids(PermissionChanges accessChanges) {
        return UserGroupSid.create(accessChanges.getRemovedGroupsAsArray());
    }

    private List<Permission> listFromArray(Permission... permissions) {
        return Lists.newArrayList(permissions);
    }
}
