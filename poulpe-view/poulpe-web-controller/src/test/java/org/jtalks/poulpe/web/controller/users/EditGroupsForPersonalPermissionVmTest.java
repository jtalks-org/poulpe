/**
 * Copyright (C) 2011 JTalks.org Team This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version. This library is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
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
import org.jtalks.poulpe.web.controller.SelectedEntity;

/**
 * Tests for {@link EditGroupsForPersonalPermissionVm}.
 *
 * @author Enykey
 *
 */
public class EditGroupsForPersonalPermissionVmTest {


    // SUT
    private EditGroupsForPersonalPermissionVm viewModel;
    @Mock
    GroupService groupService;
    @Mock
    WindowManager windowManager;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "dataProvider")
    public void testUpdateView(PermissionForEntity permissionForEntity, GroupsPermissions<ProfilePermission> groupsPermissions) {
        initTest(permissionForEntity, groupsPermissions);
        viewModel.updateVm();
    }

    public void initTest(PermissionForEntity permissionForEntity, GroupsPermissions<ProfilePermission> groupsPermissions) {

        viewModel = new EditGroupsForPersonalPermissionVm(windowManager, groupService,
                ObjectsFactory.createSelectedEntity((Object) permissionForEntity));

        viewModel.updateVm();
    }

    @DataProvider
    public Object[][] dataProvider() {
        groupService.getAll();
        List<PermissionForEntity> permissionsForEntity = Lists.newArrayList();
        GroupsPermissions<ProfilePermission> groupsPermissions = new GroupsPermissions<ProfilePermission>(
                ProfilePermission.getAllAsList());

        Group target = TestFixtures.group();

        permissionsForEntity.add(new PermissionForEntity(target, true, ProfilePermission.SEND_PRIVATE_MESSAGES));
        permissionsForEntity.add(new PermissionForEntity(target, false, ProfilePermission.EDIT_PROFILE));

        int permissionsCount = permissionsForEntity.size();
        Object[][] result = new Object[permissionsCount][2];

        for (int i = 0; i < permissionsCount; i++) {
            result[i][0] = permissionsForEntity.get(i);
            result[i][1] = groupsPermissions;
        }

        return result;
    }
}
