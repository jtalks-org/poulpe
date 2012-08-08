package org.jtalks.poulpe.web.controller.users;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.ProfilePermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.web.controller.zkmacro.EntityPermissionsBlock;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;

import java.util.List;

/**
 * Works with user permissions like editing own profile or sending private messages. These permissions might be
 * restricted the some groups (like Banned Users might not have permissions to send private messages because they may
 * send spam or they can't edit their profile because they can put spam into their signature).
 *
 * @author stanislav bashkirtsev
 */
public class UserPersonalPermissionsVm {
    private final List<EntityPermissionsBlock> blocks = Lists.newArrayList();

    /**
     * Update VM state.
     */
    @Init
    public void updateView() {
        blocks.clear();
        List<PermissionManagementBlock> blocksIn = Lists.newArrayList();
        List<ProfilePermission> permissions = ProfilePermission.getAllAsList();
        GroupsPermissions<ProfilePermission> groupsPermissions = new GroupsPermissions<ProfilePermission>(permissions);

        for (ProfilePermission permission : permissions) {
            blocksIn.add(new PermissionManagementBlock(permission, groupsPermissions, Labels
                    .getLabel("permissions.allow_label"), Labels.getLabel("permissions.restrict_label")));
        }

        Entity entity = new Group("some");
        blocks.add(new EntityPermissionsBlock(entity, "111", blocksIn));
    }

    public List<EntityPermissionsBlock> getBlocks() {
        return blocks;
    }


}
