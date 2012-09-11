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
package org.jtalks.poulpe.web.controller.branch;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.PermissionsService;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Tests for {@link BranchPermissionManagementVm}.
 *
 * @author Vyacheslav Zhivaev
 * @author Maxim Reshetov
 */
public class BranchPermissionManagementVmTest {

    private static final String MANAGE_GROUPS_DIALOG_ZUL = "WEB-INF/pages/forum/EditGroupsForBranchPermission.zul";

    // SUT
    private BranchPermissionManagementVm sut;

    // context related
    @Mock WindowManager windowManager;
    @Mock PermissionsService permissionsService;
    @Mock ZkHelper zkHelper;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        PoulpeBranch branch = TestFixtures.branch();

        SelectedEntity<Object> selectedEntity = new SelectedEntity<Object>();
        selectedEntity.setEntity(branch);

        sut = new BranchPermissionManagementVm(windowManager, permissionsService, selectedEntity);
        sut.setZkHelper(zkHelper);
    }

    /**
     * Check that dialog really opens.
     */
    @Test(dataProvider = "provideTypeOfPermissionsToBranch")
    public void testShowGroupsDialog(BranchPermission permission) {
        sut.showGroupsDialog(permission, "allow");
        sut.showGroupsDialog(permission, "restrict");

        verify(windowManager, times(2)).open(MANAGE_GROUPS_DIALOG_ZUL);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class}, dataProvider = "provideTypeOfPermissionsToBranch")
    public void testShowGroupsDialog_IllegalFormat(BranchPermission permission) {
        sut.showGroupsDialog(permission, "HERE_ILLEGAL_FORMATTED_STRING");
        verify(windowManager, never()).open(MANAGE_GROUPS_DIALOG_ZUL);
    }


    /**
     * Check method for generate data of view.
     */
    @Test(dataProvider = "provideInitDataForView")
    public void testInitDataForView(GroupsPermissions<BranchPermission> groupsPermissions, Group allowedGroup, Group restrictedGroup) {
        PoulpeBranch branch = (PoulpeBranch) sut.getSelectedEntity().getEntity();
        when(permissionsService.getPermissionsFor(branch)).thenReturn(groupsPermissions);
        sut.initDataForView();
        assertEquals(sut.getBranch(), branch);

        List<PermissionManagementBlock> blocks = sut.getBlocks();
        assertTrue(blocks.get(1).getAllowRow().getGroups().contains(allowedGroup));
        assertTrue(blocks.get(0).getRestrictRow().getGroups().contains(restrictedGroup));
        assertTrue(blocks.size() == 2);
    }


    @DataProvider
    public Object[][] provideTypeOfPermissionsToBranch() {
        return new Object[][]{
            { BranchPermission.CREATE_ANNOUNCEMENTS },
            { BranchPermission.CLOSE_TOPICS },
            { BranchPermission.VIEW_TOPICS },
            { BranchPermission.CREATE_STICKED_TOPICS },
            { BranchPermission.MOVE_TOPICS },
            { BranchPermission.EDIT_OWN_POSTS },
            { BranchPermission.CREATE_POSTS },
            { BranchPermission.DELETE_OTHERS_POSTS },
            { BranchPermission.DELETE_OWN_POSTS },
        };
    }

    @DataProvider
    public Object[][] provideInitDataForView() {
        Group allowedGroup = TestFixtures.group();
        Group restrictedGroup = TestFixtures.group();

        List<PermissionManagementBlock> blocks = Lists.newArrayList();
        BranchPermission allowedPermission = BranchPermission.CREATE_POSTS;
        BranchPermission restrictPermission = BranchPermission.CLOSE_TOPICS;

        GroupsPermissions<BranchPermission> groupsPermissions = new GroupsPermissions<BranchPermission>();
        groupsPermissions.addAllowed(allowedPermission, allowedGroup);
        groupsPermissions.addRestricted(restrictPermission, restrictedGroup);

        for (BranchPermission permission : groupsPermissions.getPermissions()) {
            blocks.add(new PermissionManagementBlock(permission, groupsPermissions, "allow", "restrict"));
        }

        return new Object[][] { {groupsPermissions, allowedGroup, restrictedGroup } };
    }

}
