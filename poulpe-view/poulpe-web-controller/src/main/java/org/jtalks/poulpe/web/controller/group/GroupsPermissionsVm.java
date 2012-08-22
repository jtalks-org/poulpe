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
package org.jtalks.poulpe.web.controller.group;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.PermissionsService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkmacro.EntityPermissionsBlock;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * View-Model for 'Group Permissions'.
 * 
 * @author Vyacheslav Zhivaev
 */
public class GroupsPermissionsVm {

    public static final String MANAGE_GROUPS_DIALOG_ZUL = "components/EditGroupsForComponentPermission.zul";

    // Injected
    private final WindowManager windowManager;
    private final ComponentService componentService;
    private final PermissionsService permissionsService;
    private final SelectedEntity<Object> selectedEntity;

    // Internal state
    private final List<EntityPermissionsBlock> blocks;

    /**
     * Construct View-Model for 'Groups permissions' view.
     * 
     * @param windowManager the window manager instance
     * @param componentService the component service instance
     * @param selectedEntity the selected entity instance, for obtaining group which to be edited
     */
    public GroupsPermissionsVm(@Nonnull WindowManager windowManager,
                               @Nonnull ComponentService componentService,
                               @Nonnull PermissionsService permissionsService,
                               @Nonnull SelectedEntity<Object> selectedEntity) {
        this.windowManager = windowManager;
        this.componentService = componentService;
        this.permissionsService = permissionsService;
        this.selectedEntity = selectedEntity;
        blocks = Lists.newArrayList();

        updateView();
    }

    /**
     * Gets list of {@link EntityPermissionsBlock}.
     * 
     * @return the blocks, list instance is UNMODIFIABLE
     */
    public List<EntityPermissionsBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    /**
     * Shows dialog with editing groups for selected permission.
     * 
     * @param entity the entity to edit permissions for
     * @param permission the permission to edit
     * @param mode the mode of permission, can be only 'allow' or 'restrict'
     */
    @Command
    public void showGroupsDialog(@BindingParam("entity") Entity entity,
            @BindingParam("permission") JtalksPermission permission, @BindingParam("mode") String mode) {
        selectedEntity.setEntity(new PermissionForEntity(entity, mode, permission));
        windowManager.open(MANAGE_GROUPS_DIALOG_ZUL);
    }

    /**
     * Update VM state.
     */
    private void updateView() {
        blocks.clear();

        for (Component component : componentService.getAll()) {
            GroupsPermissions<GeneralPermission> groupsPermissions = permissionsService.getPermissionsMapFor(component);
            List<PermissionManagementBlock> pmBlocks = Lists.newArrayList();

            GeneralPermission permission = GeneralPermission.ADMIN;
            Set<GeneralPermission> permissions = groupsPermissions.getPermissions();

            if (permissions.contains(permission)) {
                pmBlocks.add(new PermissionManagementBlock(permission, groupsPermissions, Labels
                        .getLabel("permissions.allow_label"), Labels.getLabel("permissions.restrict_label")));
            }

            blocks.add(new EntityPermissionsBlock(component, Labels.getLabel("component.title", "") + ": "
                    + component.getName(), pmBlocks));
        }
    }
}
