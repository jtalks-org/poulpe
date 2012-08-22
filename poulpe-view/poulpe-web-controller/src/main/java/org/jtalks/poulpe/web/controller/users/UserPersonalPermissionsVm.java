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
package org.jtalks.poulpe.web.controller.users;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.common.model.permissions.ProfilePermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.PermissionsService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Works with user permissions like editing own profile or sending private
 * messages. These permissions might be restricted the some groups (like Banned
 * Users might not have permissions to send private messages because they may
 * send spam or they can't edit their profile because they can put spam into
 * their signature).
 *
 * @author stanislav bashkirtsev
 * @author Enykey
 */
public class UserPersonalPermissionsVm {

    private static final String 
            PERSONAL_PERMISSION_MANAGEMENT_PAGE = "WEB-INF/pages/users/PersonalPermissions.zul",
            MANAGE_GROUPS_DIALOG_ZUL = "WEB-INF/pages/users/EditGroupsForPersonalPermission.zul";
    
    
    private final GroupService groupService;
    private final PermissionsService permissionsService;
    private final WindowManager windowManager;
    private SelectedEntity<Object> selectedEntity;
    private final List<PermissionManagementBlock> blocks = Lists.newArrayList();
    private Group group;
    /**
     * Construct View-Model for 'User Personal Permissions' view.
     *
     * @param groupService the group service instance
     * @param selectedEntity the selected entity instance
     * @param windowManager the window manager instance
     */
    public UserPersonalPermissionsVm(@Nonnull GroupService groupService,
                                     @Nonnull SelectedEntity<Object> selectedEntity,
                                     @Nonnull PermissionsService permissionsService,
                                     @Nonnull WindowManager windowManager) {
        this.groupService = groupService;
        this.permissionsService = permissionsService;
        this.selectedEntity = selectedEntity;
        this.windowManager = windowManager;
    }

    /**
     * Update VM state.
     */
    @Init
    public void updateView() {
        blocks.clear();
        List<ProfilePermission> permissions = ProfilePermission.getAllAsList();
        GroupsPermissions<ProfilePermission> permissionsMap =
                permissionsService.getPersonalPermissions(groupService.getAll());

        for (ProfilePermission permission : permissions) {
            blocks.add(new PermissionManagementBlock(permission, permissionsMap, Labels
                    .getLabel("permissions.allow_label"), Labels.getLabel("permissions.restrict_label")));
        }
    }

    /**
     * Command for showing dialog with editing groups list for current permission.
     *
     * @param permission the permission for which editing window shows
     * @param mode       the mode for permission, can be only {@code "allow"} or {@code "restrict"}
     */
    @Command
    public void showGroupsDialog(@BindingParam("permission") JtalksPermission permission,
                                 @BindingParam("mode") String mode) {
        selectedEntity.setEntity(new PermissionForEntity(group, mode, permission));
        windowManager.open(MANAGE_GROUPS_DIALOG_ZUL);
    }    

    /**
     * Method opens page with permissions to choosen branch
     *
     * @param windowManager the window manager instance
     */
    public static void showPage(WindowManager windowManager) {
        windowManager.open(PERSONAL_PERMISSION_MANAGEMENT_PAGE);
    }
   
    /**
     * Gets blocks which represents state of each permission.
     *
     * @return all blocks, list instance is UNMODIFIABLE
     */    
    public List<PermissionManagementBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }
    /**
     * Method to get currently selected item
     *
     * @return currently selected entity
     */
    public SelectedEntity<Object> getSelectedEntity() {
        return selectedEntity;
    }    
}
