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
}
