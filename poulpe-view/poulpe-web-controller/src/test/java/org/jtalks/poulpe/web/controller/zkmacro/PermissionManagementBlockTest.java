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
package org.jtalks.poulpe.web.controller.zkmacro;


import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class PermissionManagementBlockTest {
    @Mock
    JtalksPermission permission;
    @Mock
    JtalksPermission newPermission;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOneArgumentConstructor() {
        PermissionManagementBlock block = new PermissionManagementBlock(permission);

        assertEquals(block.getPermission(), permission);
        assertEquals(block.getAllowRow().getGroups().size(), 0);
        assertEquals(block.getRestrictRow().getGroups().size(), 0);
    }

    @Test
    public void testConstructorWithPermissionRows() {
        PermissionRow allowRow = new PermissionRow("first", new ArrayList<Group>());
        PermissionRow restrictRow = new PermissionRow("second", new ArrayList<Group>());
        PermissionManagementBlock block = new PermissionManagementBlock(permission, allowRow, restrictRow);

        assertEquals(block.getPermission(), permission);
        assertEquals(block.getAllowRow(), allowRow);
        assertEquals(block.getRestrictRow(), restrictRow);
    }

    @Test
    public void testConstructorFromGroupPermission() {
        List<Group> allowsGroups = Arrays.asList(new Group("First Group"), new Group("Second Group"));
        List<Group> restrictGroups = Arrays.asList(new Group("Third Group"), allowsGroups.get(0));

        GroupsPermissions groupsPermissions = mock(GroupsPermissions.class);
        when(groupsPermissions.getAllowed(permission)).thenReturn(allowsGroups);
        when(groupsPermissions.getRestricted(permission)).thenReturn(restrictGroups);
        PermissionManagementBlock block = new PermissionManagementBlock(permission, groupsPermissions, "my_allow", "my_restrict");

        assertEquals(block.getPermission(), permission);
        assertEquals(block.getAllowRow().getRowLabel(), "my_allow");
        assertEquals(block.getRestrictRow().getRowLabel(), "my_restrict");
        assertEquals(block.getAllowRow().getGroups(), allowsGroups);
        assertEquals(block.getRestrictRow().getGroups(), restrictGroups);
    }

    @Test
    public void testAddPermission() {
        PermissionManagementBlock block = new PermissionManagementBlock(permission);
        PermissionManagementBlock newBlock = block.addPermission(newPermission);

        assertNotSame(block, newBlock);
        assertSame(block.getAllowRow(), newBlock.getAllowRow());
        assertSame(block.getRestrictRow(), newBlock.getRestrictRow());
    }

    @Test
    public void testAddAllowRow() {
        PermissionManagementBlock block = new PermissionManagementBlock(permission);
        PermissionRow newRow = new PermissionRow("first", new ArrayList<Group>());
        PermissionManagementBlock newBlock = block.addAllowRow(newRow);

        assertNotSame(block, newBlock);
        assertSame(newRow, newBlock.getAllowRow());
        assertSame(block.getRestrictRow(), newBlock.getRestrictRow());
    }

    @Test
    public void testAddRestrictRow() {
        PermissionManagementBlock block = new PermissionManagementBlock(permission);
        PermissionRow newRow = new PermissionRow("first", new ArrayList<Group>());
        PermissionManagementBlock newBlock = block.addRestrictRow(newRow);

        assertNotSame(block, newBlock);
        assertSame(block.getAllowRow(), newBlock.getAllowRow());
        assertSame(newRow, newBlock.getRestrictRow());
    }
}
