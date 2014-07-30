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

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.ProfilePermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.PermissionsService;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link UserPersonalPermissionsVm}.
 *
 * @author Vyacheslav Zhivaev
 * @author Maxim Reshetov
 */
public class UserPersonalPermissionsVmTest {

    private static final String MANAGE_GROUPS_DIALOG_ZUL = "WEB-INF/pages/users/EditGroupsForPersonalPermission.zul";

    // SUT
    private UserPersonalPermissionsVm sut;
    
    // context related
    @Mock WindowManager windowManager;
    @Mock GroupService groupService;
    @Mock
    PermissionsService permissionsService;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        Group group = TestFixtures.group();

        SelectedEntity<Object> selectedEntity = new SelectedEntity<Object>();
        selectedEntity.setEntity(group);

        sut = new UserPersonalPermissionsVm(groupService ,selectedEntity,permissionsService, windowManager);
    }

    /**
     * Check that dialog really opens.
     */
    @Test(dataProvider = "provideTypeOfPermissionsToObjectsesGroup")
    public void testShowGroupsDialog(ProfilePermission permission) {
        sut.showGroupsDialog(permission, "allow");
        sut.showGroupsDialog(permission, "restrict");

        //verify(windowManager, times(2)).open(MANAGE_GROUPS_DIALOG_ZUL);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class}, dataProvider = "provideTypeOfPermissionsToObjectsesGroup")
    public void testShowGroupsDialog_IllegalFormat(ProfilePermission permission) {
        sut.showGroupsDialog(permission, "HERE_ILLEGAL_FORMATTED_STRING");
        //verify(windowManager, never()).open(MANAGE_GROUPS_DIALOG_ZUL);
    }


    /**
     * Check method for generate data of view.
     */
    @Test(dataProvider = "provideInitDataForView")
    public void testInitDataForView(GroupsPermissions<ProfilePermission> groupsPermissions, Group allowedGroup, Group restrictedGroup) {
        Group group = (Group) sut.getSelectedEntity().getEntity();
        when(permissionsService.getPersonalPermissions(groupService.getAll())).thenReturn(groupsPermissions);
        sut.updateView();
    }


    @DataProvider
    public Object[][] provideTypeOfPermissionsToObjectsesGroup() {
        return new Object[][]{
            { ProfilePermission.EDIT_OWN_PROFILE },
            { ProfilePermission.SEND_PRIVATE_MESSAGES },
        };
    }

    @DataProvider
    public Object[][] provideInitDataForView() {
        Group allowedGroup = TestFixtures.group();
        Group restrictedGroup = TestFixtures.group();

        List<PermissionManagementBlock> blocks = Lists.newArrayList();
        ProfilePermission allowedPermission = ProfilePermission.EDIT_OWN_PROFILE;
        ProfilePermission restrictPermission = ProfilePermission.SEND_PRIVATE_MESSAGES;

        GroupsPermissions<ProfilePermission> groupsPermissions = new GroupsPermissions<ProfilePermission>();
        groupsPermissions.addAllowed(allowedPermission, allowedGroup);
        groupsPermissions.addRestricted(restrictPermission, restrictedGroup);

        for (ProfilePermission permission : groupsPermissions.getPermissions()) {
            blocks.add(new PermissionManagementBlock(permission, groupsPermissions, "allow", "restrict"));
        }

        return new Object[][] { {groupsPermissions, allowedGroup, restrictedGroup } };
    }

}
