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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class PermissionRowTest {
    PermissionRow row;
    List<Group> groups;

    @BeforeMethod
    public void setUp() {
        groups = new ArrayList(Arrays.asList(new Group("first"), new Group("second")));
        row = new PermissionRow("my row", groups);
    }

    @Test
    public void constructorTest() {
        assertEquals(row.getRowLabel(), "my row");
        assertEquals(row.getGroups(), groups);
    }

    @Test
    public void addGroupTest() {
        Group newGroup = new Group("new group for add");
        PermissionRow row2 = row.addGroup(newGroup);

        assertEquals(row, row2);
        assertEquals(row.getGroups().size(), 3);
        assertEquals(row.getGroups().get(2), newGroup);
    }

    @Test
    public void newAllowRowTest() {
        PermissionRow row = PermissionRow.newAllowRow();

        assertEquals(row.getRowLabel(), PermissionRow.ALLOW_DEFAULT_LABEL);
        assertEquals(row.getGroups().size(), 0);
    }

    @Test
    public void newRestrictRowTest() {
        PermissionRow row = PermissionRow.newRestrictRow();

        assertEquals(row.getRowLabel(), PermissionRow.RESTRICT_DEFAULT_LABEL);
        assertEquals(row.getGroups().size(), 0);
    }

    @Test
    public void newAllowRowWithGroupsTest() {
        PermissionRow row = PermissionRow.newAllowRow(groups);

        assertEquals(row.getRowLabel(), PermissionRow.ALLOW_DEFAULT_LABEL);
        assertEquals(row.getGroups(), groups);
    }

    @Test
    public void newRestrictRowWithGroupsTest() {
        PermissionRow row = PermissionRow.newRestrictRow(groups);

        assertEquals(row.getRowLabel(), PermissionRow.RESTRICT_DEFAULT_LABEL);
        assertEquals(row.getGroups(), groups);
    }

    @Test
    public void newAllowRowWithGroupsAndLabelTest() {
        PermissionRow row = PermissionRow.newAllowRow("my allow", groups);

        assertEquals(row.getRowLabel(), "my allow");
        assertEquals(row.getGroups(), groups);
    }

    @Test
    public void newRestrictRowWithGroupsAndLabelTest() {
        PermissionRow row = PermissionRow.newRestrictRow("my restrict", groups);

        assertEquals(row.getRowLabel(), "my restrict");
        assertEquals(row.getGroups(), groups);
    }
}
