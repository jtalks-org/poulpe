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


import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.PermissionsService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class TransactionalPermissionsServiceTest {

    private PermissionsService permissionsService;

    @BeforeMethod
    public void beforeMethod(){
        permissionsService = mock(TransactionalPermissionsService.class);
    }

    @Test
    public void testGetPermissionsFor(){
        PoulpeBranch branch = new PoulpeBranch();
        when(permissionsService.getPermissionsFor(branch)).thenReturn(new GroupsPermissions<BranchPermission>());
        assertNotNull(permissionsService.getPermissionsFor(branch));
    }

}
