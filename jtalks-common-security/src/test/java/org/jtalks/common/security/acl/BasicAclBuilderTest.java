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
package org.jtalks.common.security.acl;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.*;

/**
 * @author stanislav bashkirtsev
 */
public class BasicAclBuilderTest {
    private BasicAclBuilder builder;
    private AclManager manager;
    private Entity target = new Entity() {
    };

    @BeforeMethod
    public void setUp() throws Exception {
        manager = mock(AclManager.class);
        builder = new BasicAclBuilder(manager);
    }

    @Test(dataProvider = "groupsAndPermissions")
    public void testGrant(Group[] groups, Permission[] permissions) throws Exception {
        builder.setOwner(groups).grant(permissions).on(target).flush();
        verify(manager).grant(groupSids(groups), Lists.newArrayList(permissions), target);
    }


    @Test(dataProvider = "groupsAndPermissions")
    public void testDelete(Group[] groups, Permission[] permissions) throws Exception {
        builder.delete(permissions).setOwner(groups).on(target).flush();
        verify(manager).delete(groupSids(groups), newArrayList(permissions), target);
    }

    @Test(dataProvider = "groupsAndPermissions")
    public void testRestrict(Group[] groups, Permission[] permissions) throws Exception {
        builder.restrict(permissions).setOwner(groups).on(target).flush();
        verify(manager).restrict(groupSids(groups), newArrayList(permissions), target);
    }

    @Test(dataProvider = "onePermissionAndOneGroup")
    public void testRestrictGrantDeleteTogether(Permission permission, Group group) throws Exception {
        builder.restrict(permission).delete(permission).grant(permission).setOwner(group).on(target).flush();
        verify(manager, times(1)).grant(groupSids(group), Lists.newArrayList(permission), target);
        verify(manager, times(1)).restrict(groupSids(group), Lists.newArrayList(permission), target);
        verify(manager, times(1)).delete(groupSids(group), Lists.newArrayList(permission), target);
    }

    @Test(dataProvider = "twoPermissionsAndTwoGroup")
    public void testBuilderReused(Group[] groups, Permission[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            Permission permission = permissions[i];
            Group group = groups[i];
            builder.restrict(permission).delete(permission).grant(permission).setOwner(group).on(target).flush();
            verify(manager, times(1)).grant(groupSids(group), Lists.newArrayList(permission), target);
            verify(manager, times(1)).restrict(groupSids(group), Lists.newArrayList(permission), target);
            verify(manager, times(1)).delete(groupSids(group), Lists.newArrayList(permission), target);
        }
    }


    @Test
    public void testFlushWithoutPermissions() throws Exception {
        Group group = new Group();
        builder.setOwner(group).on(target).flush();
        verify(manager, times(0)).restrict(anyList(), anyList(), same(target));
    }

    @Test
    public void testFlushWithoutSids() throws Exception {
        builder.restrict(BranchPermission.VIEW_TOPICS).on(target).flush();
        verify(manager, times(1)).restrict(groupSids(new Group[]{}),
                Lists.<Permission>newArrayList(BranchPermission.VIEW_TOPICS), target);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFlushWithoutTarget() throws Exception {
        builder.restrict(BranchPermission.VIEW_TOPICS).setOwner(new Group()).flush();
    }

    private List<Sid> groupSids(Group... groups) {
        List<Sid> sids = newArrayList();
        for (Group group : groups) {
            sids.add(new UserGroupSid(group));
        }
        return sids;
    }

    @DataProvider(name = "onePermissionAndOneGroup")
    public Object[][] provideOnePermissionAndOneGroup() {
        return new Object[][]{{BranchPermission.VIEW_TOPICS, new Group()}};
    }

    @DataProvider(name = "twoPermissionsAndTwoGroup")
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    public Object[][] provideTwoPermissionsAndTwoGroup() {
        Group[] arrayWithTwoGroups = Group.createGroupsWithNames("Moderators", "Admins").toArray(new Group[]{});
        Permission[] arrayWithTwoPermissions = {BranchPermission.VIEW_TOPICS, BranchPermission.CREATE_TOPICS};

        return new Object[][]{{arrayWithTwoGroups, arrayWithTwoPermissions}};
    }

    @DataProvider(name = "groupsAndPermissions")
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    public Object[][] provideGroupsAndPermissions() {
        Group[] arrayWithOneGroup = Group.createGroupsWithNames("Trolls").toArray(new Group[]{});
        Permission[] arrayWithOnePermission = {BranchPermission.VIEW_TOPICS};

        Group[] arrayWithTwoGroups = Group.createGroupsWithNames("Moderators", "Admins").toArray(new Group[]{});
        Permission[] arrayWithTwoPermissions = {BranchPermission.VIEW_TOPICS, BranchPermission.CREATE_TOPICS};

        return new Object[][]{
                {arrayWithOneGroup, arrayWithOnePermission},
                {arrayWithTwoGroups, arrayWithTwoPermissions}};
    }
}
