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
package org.jtalks.poulpe.web.controller.component;

import com.google.common.collect.Lists;
import org.apache.commons.lang.math.RandomUtils;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.PermissionsService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectsFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link EditGroupsForComponentPermissionVm}.
 * 
 * @author Vyacheslav Zhivaev
 */
public class EditGroupsForComponentPermissionVmTest {

    // limits dataProvider out
    private final int DATA_PROVIDER_LIMIT = 20;

    // SUT
    private EditGroupsForComponentPermissionVm viewModel;

    @Mock WindowManager windowManager;
    @Mock ComponentService componentService;
    @Mock GroupService groupService;
    @Mock
    PermissionsService permissionsService;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "dataProvider")
    public void testCancelWithoutChanges(PermissionForEntity permissionForEntity,
            GroupsPermissions<GeneralPermission> groupsPermissions) {
        initTest(permissionForEntity, groupsPermissions);

        viewModel.cancel();

        vefiryNothingChanges();
    }

    @Test(dataProvider = "dataProvider")
    public void testCancelWithChanges(PermissionForEntity permissionForEntity,
            GroupsPermissions<GeneralPermission> groupsPermissions) {
        initTest(permissionForEntity, groupsPermissions);

        viewModel.addAll();
        viewModel.cancel();

        vefiryNothingChanges();
    }

    @Test(dataProvider = "dataProvider")
    public void testSaveWithoutChanges(PermissionForEntity permissionForEntity,
            GroupsPermissions<GeneralPermission> groupsPermissions) {
        initTest(permissionForEntity, groupsPermissions);

        viewModel.save();

        vefiryNothingChanges();
    }

    @Test(dataProvider = "dataProvider")
    public void testSaveWithChanges(PermissionForEntity permissionForEntity,
            GroupsPermissions<GeneralPermission> groupsPermissions) {
        initTest(permissionForEntity, groupsPermissions);

        viewModel.removeAll();
        viewModel.save();

        vefiryPermissionsChanged(permissionForEntity);

        verify(windowManager).open(anyString());
    }

    @DataProvider
    public Object[][] dataProvider() {
        List<PermissionForEntity> permissionsForEntity = Lists.newArrayList();
        GroupsPermissions<GeneralPermission> groupsPermissions = new GroupsPermissions<GeneralPermission>(
                GeneralPermission.getAllAsList());

        Component target = TestFixtures.randomComponent();

        // permissions to work with
        GeneralPermission[] permissions = GeneralPermission.values();

        // building fixtures
        int count = Math.min(DATA_PROVIDER_LIMIT, permissions.length);
        for (int i = 0; i < count; i++) {
            permissionsForEntity.add(new PermissionForEntity(target, true, permissions[i]));
            permissionsForEntity.add(new PermissionForEntity(target, false, permissions[i]));

            for (int j = 0, countj = RandomUtils.nextInt(4) + 2; j < countj; j++) {
                groupsPermissions.add(permissions[i], TestFixtures.group(), TestFixtures.group());
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

    private void initTest(PermissionForEntity permissionForEntity, GroupsPermissions<GeneralPermission> groupsPermissions) {
        Component component = (Component) permissionForEntity.getTarget();
        when(permissionsService.getPermissionsMapFor(component)).thenReturn(groupsPermissions);

        viewModel = new EditGroupsForComponentPermissionVm(windowManager, permissionsService, groupService,
                ObjectsFactory.createSelectedEntity((Object) permissionForEntity));

        viewModel.initVm();
    }

    private void vefiryPermissionsChanged(PermissionForEntity permissionForEntity) {
        Component target = (Component) permissionForEntity.getTarget();

        if (permissionForEntity.isAllowed()) {
            verify(permissionsService).changeGrants(eq(target), any(PermissionChanges.class));
        } else {
            verify(permissionsService).changeRestrictions(eq(target), any(PermissionChanges.class));
        }
    }

    private void vefiryNothingChanges() {
        verify(permissionsService, never()).changeGrants(any(Component.class), any(PermissionChanges.class));
        verify(permissionsService, never()).changeRestrictions(any(Component.class), any(PermissionChanges.class));
        verify(componentService, never()).saveComponent(any(Component.class));

        verify(groupService, never()).saveGroup(any(Group.class));
        verify(groupService, never()).deleteGroup(any(Group.class));
    }
}
