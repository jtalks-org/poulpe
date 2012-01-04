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

import org.jtalks.poulpe.model.entity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class BranchPermissionRow {
    private List<Group> groups;
    private String rowLabel;

    protected BranchPermissionRow(String rowLabel, List<Group> groups) {
        this.groups = groups;
        this.rowLabel = rowLabel;
    }

    public List<Group> getGroups() {
        return groups;
    }


    public String getRowLabel() {
        return rowLabel;
    }

    public BranchPermissionRow addGroup(Group group) {
        groups.add(group);
        return this;
    }

    public static BranchPermissionRow newAllowRow() {
        return newAllowRow(new ArrayList<Group>());
    }

    public static BranchPermissionRow newRestrictRow() {
        return newRestrictRow(new ArrayList<Group>());
    }

    public static BranchPermissionRow newAllowRow(List<Group> groups) {
        return new BranchPermissionRow("Allow", groups);
    }

    public static BranchPermissionRow newRestrictRow(List<Group> groups) {
        return new BranchPermissionRow("Restrict", groups);
    }

}
