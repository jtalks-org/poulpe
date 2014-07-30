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

import java.util.List;

/**
 * Service for dealing with {@link PermissionChanges} objects
 */
public class TransactionalPermissionsService implements PermissionsService {

    private PermissionManager permissionManager;

    /**
     * Constructor
     * @param permissionManager permission manager
     */
    public TransactionalPermissionsService(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeGrants(PoulpeBranch branch, PermissionChanges changes) {
        permissionManager.changeGrants(branch, changes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeRestrictions(PoulpeBranch branch, PermissionChanges changes) {
        permissionManager.changeRestrictions(branch, changes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupsPermissions<GeneralPermission> getPermissionsMapFor(Component component) {
        return permissionManager.getPermissionsMapFor(component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeGrants(Component component, PermissionChanges changes) {
        permissionManager.changeGrants(component, changes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeRestrictions(Component component, PermissionChanges changes) {
        permissionManager.changeRestrictions(component, changes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupsPermissions<ProfilePermission> getPersonalPermissions(List<Group> groups) {
        return permissionManager.getPermissionsMapFor(groups);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeGrants(Group group, PermissionChanges changes) {
        permissionManager.changeGrants(group, changes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeRestrictions(Group group, PermissionChanges changes) {
        permissionManager.changeRestrictions(group, changes);
    }
}
