

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
     * @param branch branch to which grants will be changed
     * @param changes grants for branch
     */
    void changeGrants(PoulpeBranch branch, PermissionChanges changes);

    /**
     * Change restriction for branch.
     *
     * @param branch branch to which restriction will be changed
     * @param changes new restriction for branch
     */
    void changeRestrictions(PoulpeBranch branch, PermissionChanges changes);

    /**
     * Gets {@link org.jtalks.poulpe.model.dto.GroupsPermissions} for defined {@link org.jtalks.poulpe.model.entity.Component}.
     *
     * @param component the component to get for
     * @return {@link org.jtalks.poulpe.model.dto.GroupsPermissions} for defined {@link org.jtalks.poulpe.model.entity.Component}
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
     * @param groups all groups
     * @return access list
     */
    GroupsPermissions<ProfilePermission> getPersonalPermissions(List<Group> groups);

    /**
     * Change grants for group.
     *
     * @param group group to which grants will be changed
     * @param changes grants for group
     */
    void changeGrants(Group group, PermissionChanges changes);

    /**
     * Change restriction for group.
     *
     * @param group group to which restriction will be changed
     * @param changes new restriction for group
     */
    void changeRestrictions(Group group, PermissionChanges changes);
}
