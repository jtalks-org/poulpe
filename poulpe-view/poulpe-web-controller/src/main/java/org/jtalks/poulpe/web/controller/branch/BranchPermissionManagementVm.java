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

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Branch;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * A View Model for page that allows user to specify what actions can be done with the specific branch and what user
 * groups can do them.
 *
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class BranchPermissionManagementVm {
    private static final String
            BRANCH_PERMISSION_MANAGEMENT_PAGE = "WEB-INF/pages/forum/BranchPermissionManagement.zul",
            MANAGE_GROUPS_DIALOG_ZUL = "WEB-INF/pages/forum/EditGroupsForBranchPermission.zul";
    private final WindowManager windowManager;
    private final BranchService branchService;
    private final SelectedEntity<Object> selectedEntity;
    private final PoulpeBranch branch;
    private final List<PermissionManagementBlock> blocks = Lists.newArrayList();

    /**
     * Constructs the VM with given dependencies.
     *
     * @param windowManager  the window manager instance
     * @param branchService  branch service
     * @param selectedEntity the selectedEntity with PoulpeBranch to edit
     */
    public BranchPermissionManagementVm(@Nonnull WindowManager windowManager, @Nonnull BranchService branchService,
                                        @Nonnull SelectedEntity<Object> selectedEntity) {
        this.windowManager = windowManager;
        this.branchService = branchService;
        this.selectedEntity = selectedEntity;
        this.branch = (PoulpeBranch) selectedEntity.getEntity();

        initDataForView();
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

    public static void showPage(WindowManager windowManager, Branch branch) {
        windowManager.open(BRANCH_PERMISSION_MANAGEMENT_PAGE);
    }

    /**
     * Initializes the data for view.
     */
    private void initDataForView() {
        PermissionsMap<BranchPermission> permissionsMap = branchService.getPermissionsFor(branch);
        for (BranchPermission permission : permissionsMap.getPermissions()) {
            blocks.add(new PermissionManagementBlock(permission, permissionsMap, Labels
                    .getLabel("permissions.allow_label"), Labels.getLabel("permissions.restrict_label")));
        }
    }

}
