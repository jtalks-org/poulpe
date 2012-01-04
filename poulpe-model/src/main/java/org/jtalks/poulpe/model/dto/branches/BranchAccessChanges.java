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
package org.jtalks.poulpe.model.dto.branches;

import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.permissions.JtalksPermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class BranchAccessChanges {
    private final JtalksPermission permission;
    private final List<Group> newlyAddedPermissions = new ArrayList<Group>();
    private final List<Group> removedPermissions = new ArrayList<Group>();

    public BranchAccessChanges(JtalksPermission permission) {
        this.permission = permission;
    }

    public Group[] getNewlyAddedPermissionsAsArray() {
        return newlyAddedPermissions.toArray(new Group[]{});
    }

    public void setNewlyAddedPermissions(Collection<Group> newlyAddedPermissions) {
        this.newlyAddedPermissions.addAll(newlyAddedPermissions);
    }

    public Group[] getRemovedPermissionsAsArray() {
        return removedPermissions.toArray(new Group[]{});
    }


    public void setRemovedPermissions(Collection<Group> removedPermissions) {
        this.removedPermissions.addAll(removedPermissions);
    }

    public JtalksPermission getPermission() {
        return permission;
    }
    public boolean isEmpty(){
        return removedPermissions.isEmpty() && newlyAddedPermissions.isEmpty();
    }
}
