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


import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.ProfilePermission;
import org.jtalks.poulpe.logic.PermissionManager;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.common.model.entity.Component;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.PermissionsService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;

public class TransactionalPermissionsServiceTest {

    private PermissionsService permissionsService;
    private PermissionManager permissionManager;

    @BeforeMethod
    public void beforeMethod() {
        permissionManager = mock(PermissionManager.class);
        permissionsService = spy(new TransactionalPermissionsService(permissionManager));
    }

    @Test
    public void testChangeGrants() {
        PermissionChanges changes = mock(PermissionChanges.class);

        Component component = mock(Component.class);
        permissionsService.changeGrants(component, changes);

        Group group = mock(Group.class);
        permissionsService.changeGrants(group, changes);
    }

    @Test
    public void testChangeRestrictions() {
        PermissionChanges changes = mock(PermissionChanges.class);

        Component component = mock(Component.class);
        permissionsService.changeRestrictions(component, changes);

        Group group = mock(Group.class);
        permissionsService.changeRestrictions(group, changes);
    }

    @Test
    public void testGetPermissionsMapFor() {
        Component component = mock(Component.class);
        when(permissionManager.getPermissionsMapFor(component)).thenReturn(new GroupsPermissions<GeneralPermission>());
        assertNotNull(permissionsService.getPermissionsMapFor(component));
    }

    @Test
    public void testGetPersonalPermissions() {
        List<Group> groups = mock(List.class);
        when(permissionManager.getPermissionsMapFor(groups)).thenReturn(new GroupsPermissions<ProfilePermission>());
        assertNotNull(permissionsService.getPersonalPermissions(groups));
    }


}
