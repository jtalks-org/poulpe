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

package org.jtalks.poulpe.service;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.ProfilePermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.PoulpeBranch;

import java.util.List;

/**
 * Service for dealing with {@link PermissionChanges} objects
 */
public interface PermissionsService {

    /**
     * Return access lists for branch.
     *
     * @param branch branch which will be returned access list
     * @return access list
     */
    GroupsPermissions<BranchPermission> getPermissionsFor(PoulpeBranch branch);

    /**
     * Change grants for branch.
     *
     * @param branch  branch to which grants will be changed
     * @param changes grants for branch
     */
    void changeGrants(PoulpeBranch branch, PermissionChanges changes);

    /**
     * Change restriction for branch.
     *
     * @param branch  branch to which restriction will be changed
     * @param changes new restriction for branch
     */
    void changeRestrictions(PoulpeBranch branch, PermissionChanges changes);

    /**
     * Gets {@link org.jtalks.poulpe.model.dto.GroupsPermissions} for defined
     * {@link org.jtalks.poulpe.model.entity.Component}.
     *
     * @param component the component to get for
     * @return {@link org.jtalks.poulpe.model.dto.GroupsPermissions} for defined
     *         {@link org.jtalks.poulpe.model.entity.Component}
     */
    GroupsPermissions<GeneralPermission> getPermissionsMapFor(Component component);

    /**
     * Change grants for component.
     *
     * @param component the component to change for
     * @param changes   the {@link PermissionChanges} which needs to be applied
     * @see PermissionChanges
     */
    void changeGrants(Component component, PermissionChanges changes);

    /**
     * Change restrictions for component.
     *
     * @param component the component to change for
     * @param changes   the {@link PermissionChanges} which needs to be applied
     */
    void changeRestrictions(Component component, PermissionChanges changes);

    /**
     * Return PersonalPermissions access lists for all available {@link org.jtalks.common.model.entity.Group}'s.
     *
     * @param groups all groups
     * @return access list
     */
    GroupsPermissions<ProfilePermission> getPersonalPermissions(List<Group> groups);

    /**
     * Change grants for group.
     *
     * @param group   group to which grants will be changed
     * @param changes grants for group
     */
    void changeGrants(Group group, PermissionChanges changes);

    /**
     * Change restriction for group.
     *
     * @param group   group to which restriction will be changed
     * @param changes new restriction for group
     */
    void changeRestrictions(Group group, PermissionChanges changes);
}
