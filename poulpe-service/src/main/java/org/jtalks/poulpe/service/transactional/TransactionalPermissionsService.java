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
import org.jtalks.poulpe.logic.PermissionManager;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.PermissionsService;

public class TransactionalPermissionsService implements PermissionsService{

    private PermissionManager permissionManager;

    public TransactionalPermissionsService(PermissionManager permissionManager){
        this.permissionManager=permissionManager;
    }

    /**
     * Return access lists for branch.
     *
     * @param branch branch which will be returned access list
     * @return access list
     */
    @Override
    public GroupsPermissions<BranchPermission> getPermissionsFor(PoulpeBranch branch) {
        return permissionManager.getPermissionsMapFor(branch);
    }

    /**
     * Change grants for branch.
     *
     * @param branch  branch to which grants will be changed
     * @param changes grants for branch
     */
    @Override
    public void changeGrants(PoulpeBranch branch, PermissionChanges changes) {
        permissionManager.changeGrants(branch, changes);
    }

    /**
     * Change restriction for branch.
     *
     * @param branch  branch to which restriction will be changed
     * @param changes new restriction for branch
     */
    @Override
    public void changeRestrictions(PoulpeBranch branch, PermissionChanges changes) {
        permissionManager.changeRestrictions(branch, changes);
    }
}
