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
package org.jtalks.poulpe.web.controller.users;

import org.jtalks.poulpe.web.controller.branch.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.SecurityGroupList;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectsFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import org.jtalks.common.model.permissions.ProfilePermission;

/**
 * Tests for {@link EditGroupsForBranchPermissionVm}.
 * 
 * @author Vyacheslav Zhivaev
 * 
 */
public class EditGroupsForPersonalPermissionVmTest {

    // limits dataProvider out
    private final int DATA_PROVIDER_LIMIT = 20;

    // SUT
    private EditGroupsForPersonalPermissionVm viewModel;

    @Mock GroupService groupService;
    @Mock WindowManager windowManager;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "dataProvider")
    public void cancel(PermissionForEntity permissionForEntity, GroupsPermissions<ProfilePermission> groupsPermissions) {
        initTest(permissionForEntity, groupsPermissions);
        viewModel.cancel();
        vefiryNothingChanges();
    }

    @Test(dataProvider = "dataProvider")
    public void testSaveWithChanges(PermissionForEntity permissionForEntity,
            GroupsPermissions<ProfilePermission> groupsPermissions) {
        initTest(permissionForEntity, groupsPermissions);

        viewModel.removeAll();
        viewModel.save();

        //vefiryPermissionsChanged(permissionForEntity);  //how to test this part?

        verify(windowManager).open(anyString());
    }

    @Test(dataProvider = "dataProvider")
    public void testSaveWithoutChanges(PermissionForEntity permissionForEntity,
                                       GroupsPermissions<ProfilePermission> groupsPermissions) {
        initTest(permissionForEntity, groupsPermissions);

        viewModel.save();

        vefiryNothingChanges();
    }

    public void initTest(PermissionForEntity permissionForEntity, GroupsPermissions<ProfilePermission> groupsPermissions) {
        when(groupService.getSecurityGroups()).thenReturn(new SecurityGroupList());
        when(groupService.getPermissionsFor()).thenReturn(groupsPermissions);

        viewModel = new EditGroupsForPersonalPermissionVm(windowManager, groupService,
                ObjectsFactory.createSelectedEntity((Object) permissionForEntity));

        viewModel.updateVm();
    }

    @DataProvider
    public Object[][] dataProvider() {
        // TODO: probably copy-paste
        List<PermissionForEntity> permissionsForEntity = Lists.newArrayList();
        GroupsPermissions<ProfilePermission> groupsPermissions = new GroupsPermissions<ProfilePermission>(
                ProfilePermission.getAllAsList());

        // target entity (Group)
        Group target = TestFixtures.group();
        // permissions to work with
        ProfilePermission[] profilePermissions = ProfilePermission.values();

        // building fixtures
        int count = Math.min(DATA_PROVIDER_LIMIT, profilePermissions.length);
        for (int i = 0; i < count; i++) {
            permissionsForEntity.add(new PermissionForEntity(target, true, profilePermissions[i]));
            permissionsForEntity.add(new PermissionForEntity(target, false, profilePermissions[i]));

            for (int j = 0, countj = RandomUtils.nextInt(4) + 2; j < countj; j++) {
                groupsPermissions.add(profilePermissions[i], TestFixtures.group(), TestFixtures.group());
            }
        }

        // converting fixtures to usable state, adding same PermissionsMap for each PermissionForEntity
        int permissionsCount = permissionsForEntity.size();
        Object[][] result = new Object[permissionsCount][2];

        for (int i = 0; i < permissionsCount; i++) {
            result[i][0] = permissionsForEntity.get(i);
            result[i][1] = groupsPermissions;
        }

        return result;
    }

    private void vefiryPermissionsChanged(PermissionForEntity permissionForEntity) {
        Group target = (Group) permissionForEntity.getTarget();

        if (permissionForEntity.isAllowed()) {
            verify(groupService).changeGrants(eq(target), any(PermissionChanges.class));
        } else {
            verify(groupService).changeRestrictions(eq(target), any(PermissionChanges.class));
        }
    }

    private void vefiryNothingChanges() {
        verify(groupService, never()).changeGrants(any(Group.class), any(PermissionChanges.class));
        verify(groupService, never()).changeRestrictions(any(Group.class), any(PermissionChanges.class));
        //verify(groupService, never()).saveBranch(any(Group.class));

        verify(groupService, never()).saveGroup(any(Group.class));
        verify(groupService, never()).deleteGroup(any(Group.class));
    }
}