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
package org.jtalks.poulpe.web.controller.branch;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.PermissionsService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * A View Model for page that allows user to specify what actions can be done with the specific branch and what user
 * groups can do them.
 *
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 * @author Maxim Reshetov
 */
public class BranchPermissionManagementVm {
    private static final String
            BRANCH_PERMISSION_MANAGEMENT_PAGE = "WEB-INF/pages/forum/BranchPermissionManagement.zul",
            MANAGE_GROUPS_DIALOG_ZUL = "WEB-INF/pages/forum/EditGroupsForBranchPermission.zul";
    private final WindowManager windowManager;
    private final PermissionsService permissionsService;
    private final SelectedEntity<Object> selectedEntity;
    private PoulpeBranch branch;
    private final List<PermissionManagementBlock> blocks = Lists.newArrayList();
    private ZkHelper zkHelper;

    /**
     * Constructs the VM with given dependencies.
     *
     * @param windowManager  the window manager instance
     * @param permissionsService  the permissions service instance
     * @param selectedEntity the selectedEntity with PoulpeBranch to edit
     */
    public BranchPermissionManagementVm(@Nonnull WindowManager windowManager,
                                        @Nonnull PermissionsService permissionsService,
                                        @Nonnull SelectedEntity<Object> selectedEntity) {
        this.windowManager = windowManager;
        this.permissionsService = permissionsService;
        this.selectedEntity = selectedEntity;
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
        selectedEntity.setEntity(new PermissionForEntity(branch, mode, permission));
        windowManager.open(MANAGE_GROUPS_DIALOG_ZUL);
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
     * Gets current branch for edit.
     *
     * @return the branch to edit
     */
    public PoulpeBranch getBranch() {
        return branch;
    }

    /**
     * Method opens page with permissions to choosen branch
     *
     * @param windowManager the window manager instance
     */
    public static void showPage(WindowManager windowManager) {
        windowManager.open(BRANCH_PERMISSION_MANAGEMENT_PAGE);
    }

    /**
     * Initializes the data for view.
     *
     * @param component Component of View (BranchPermissionManagement.zul)
     */
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component component) {
        zkHelper = new ZkHelper(component);
        zkHelper.wireComponents(component, this);
        initDataForView();
    }

    /**
     * Method calls by init method. It generate permissions block to view
     */
    public void initDataForView() {
        blocks.clear();
        this.branch = (PoulpeBranch) selectedEntity.getEntity();
        GroupsPermissions<BranchPermission> groupsPermissions = permissionsService.getPermissionsFor(branch);
        for (BranchPermission permission : groupsPermissions.getPermissions()) {
            blocks.add(new PermissionManagementBlock(permission, groupsPermissions,
                    zkHelper.getLabel("permissions.allow_label"), zkHelper.getLabel("permissions.restrict_label")));
        }
    }

    /**
     * Method to get currently selected item
     *
     * @return currently selected entity
     */
    public SelectedEntity<Object> getSelectedEntity() {
        return selectedEntity;
    }

    /**
     * @param zkHelper the zkHelper to set
     */
    @VisibleForTesting
    void setZkHelper(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }
}
