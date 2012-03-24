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

import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.Validate;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.ComponentPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkmacro.EntityPermissionsBlock;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionRow;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import com.google.common.collect.Lists;

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
    private final SelectedEntity<Object> selectedEntity;

    // Internal state
    private final List<EntityPermissionsBlock> blocks;

    public GroupsPermissionsVm(@Nonnull WindowManager windowManager, @Nonnull ComponentService componentService,
            @Nonnull SelectedEntity<Object> selectedEntity) {
        this.windowManager = windowManager;
        this.componentService = componentService;
        this.selectedEntity = selectedEntity;
        blocks = Lists.newArrayList();

        updateView();
    }

    /**
     * Gets list of {@link EntityPermissionsBlock}.
     * 
     * @return the blocks
     */
    @SuppressWarnings("unchecked")
    public List<EntityPermissionsBlock> getBlocks() {
        return ListUtils.unmodifiableList(blocks);
    }

    private void updateView() {
        blocks.clear();

        for (Component component : componentService.getAll()) {
            PermissionsMap<ComponentPermission> permissions = componentService.getPermissionsMapFor(component);
            List<PermissionManagementBlock> pmBlocks = Lists.newArrayList();

            for (ComponentPermission permission : permissions.getPermissions()) {
                PermissionRow allowRow = PermissionRow.newAllowRow(permissions.getAllowed(permission));
                PermissionRow restrictRow = PermissionRow.newRestrictRow(permissions.getRestricted(permission));
                pmBlocks.add(new PermissionManagementBlock(permission, allowRow, restrictRow));
            }

            blocks.add(new EntityPermissionsBlock(component, "Component: " + component.getName(), pmBlocks));
        }
    }

    @Command
    public void showGroupsDialog(@BindingParam("entity") Entity entity,
            @BindingParam("permission") JtalksPermission permission, @BindingParam("mode") String mode) {
        boolean allowed = "allow".equalsIgnoreCase(mode);
        Validate.isTrue(allowed || "restrict".equalsIgnoreCase(mode),
                "Illegal format of parameter 'mode', it can be only 'allow' or 'restrict'");

        PermissionForEntity permissionForEntity = new PermissionForEntity(entity, allowed, permission);

        selectedEntity.setEntity(permissionForEntity);
        windowManager.open(MANAGE_GROUPS_DIALOG_ZUL);
    }

}
