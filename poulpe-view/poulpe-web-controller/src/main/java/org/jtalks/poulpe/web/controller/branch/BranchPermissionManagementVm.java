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

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.dto.branches.BranchPermissions;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementBlock;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionRow;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * A View Model for page that allows user to specify what actions can be done with the specific branch and what user
 * groups can do them.
 * 
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class BranchPermissionManagementVm {

    public static final String MANAGE_GROUPS_DIALOG_ZUL = "/sections/EditGroupsForBranchPermission.zul";

    // Injected
    private final WindowManager windowManager;
    private final BranchService branchService;
    private final SelectedEntity<Object> selectedEntity;

    private final PoulpeBranch branch;
    private final Map<String, BranchPermissionManagementBlock> blocks = Maps.newLinkedHashMap();

    /**
     * Constructs the VM with given dependencies.
     * 
     * @param windowManager the window manager instance
     * @param branchService branch service
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
     * @param params the URL-style formatted parameters from zul
     */
    @Command
    public void showGroupsDialog(@BindingParam("params") String params) {
        Map<String, String> parsedParams = parseParams(params);
        String permissionName = parsedParams.get("permissionName");
        BranchPermissionManagementBlock branchPermissionManagementBlock = blocks.get(permissionName);
        String mode = parsedParams.get("mode");

        boolean allowed = "allow".equalsIgnoreCase(mode);
        if (!allowed && !"restrict".equalsIgnoreCase(mode)) {
            throw new IllegalArgumentException(
                    "Illegal format of parameter 'mode', it can be only 'allow' or 'restrict'");
        }

        PermissionForEntity modePermission = new PermissionForEntity(branch, allowed,
                branchPermissionManagementBlock.getPermission());

        selectedEntity.setEntity(modePermission);
        windowManager.open(MANAGE_GROUPS_DIALOG_ZUL);
    }

    /**
     * Parses URL parameters substring to map.
     * 
     * @param params string with params
     * @return map build from params
     */
    private static Map<String, String> parseParams(String params) {
        Builder<String, String> parsedParams = ImmutableMap.builder();
        String[] paramRows = params.split(Pattern.quote(","));
        for (String nextParam : paramRows) {
            String[] splitParamRow = nextParam.trim().split(Pattern.quote("="));
            parsedParams.put(splitParamRow[0], splitParamRow[1]);
        }
        return parsedParams.build();
    }

    /**
     * Initializes the data for view.
     */
    private void initDataForView() {
        BranchPermissions groupAccessList = branchService.getGroupAccessListFor(branch);
        for (BranchPermission permission : groupAccessList.getPermissions()) {
            BranchPermissionRow allowRow = BranchPermissionRow.newAllowRow(groupAccessList.getAllowed(permission));
            BranchPermissionRow restrictRow = BranchPermissionRow.newRestrictRow(groupAccessList
                    .getRestricted(permission));
            blocks.put(permission.getName(), new BranchPermissionManagementBlock(permission, allowRow, restrictRow));
        }
    }

    /**
     * Gets blocks which represents state of each permission.
     * 
     * @return all blocks
     */
    public List<BranchPermissionManagementBlock> getBlocks() {
        return Lists.newArrayList(blocks.values());
    }

    /**
     * Gets current branch for edit.
     * 
     * @return the branch to edit
     */
    public PoulpeBranch getBranch() {
        return branch;
    }

}
