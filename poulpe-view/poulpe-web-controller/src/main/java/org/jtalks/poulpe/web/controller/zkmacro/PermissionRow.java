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

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.Group;

/**
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class PermissionRow {
    private final String rowLabel;
    private final List<Group> groups;

    protected PermissionRow(String rowLabel, List<Group> groups) {
        this.groups = groups;
        this.rowLabel = rowLabel;
    }

    public List<Group> getGroups() {
        return groups;
    }


    public String getRowLabel() {
        return rowLabel;
    }

    public PermissionRow addGroup(Group group) {
        groups.add(group);
        return this;
    }

    public static PermissionRow newAllowRow() {
        return newAllowRow(new ArrayList<Group>());
    }

    public static PermissionRow newRestrictRow() {
        return newRestrictRow(new ArrayList<Group>());
    }

    public static PermissionRow newAllowRow(List<Group> groups) {
        return newAllowRow("Allow", groups);
    }

    public static PermissionRow newRestrictRow(List<Group> groups) {
        return newRestrictRow("Restrict", groups);
    }

    public static PermissionRow newAllowRow(String rowLabel, List<Group> groups) {
        return new PermissionRow(rowLabel, groups);
    }

    public static PermissionRow newRestrictRow(String rowLabel, List<Group> groups) {
        return new PermissionRow(rowLabel, groups);
    }

}
