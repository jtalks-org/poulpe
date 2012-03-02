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
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.branches.AclChangeset;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementBlock;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionRow;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Window;

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
    private final GroupService groupService;
    private final BranchService branchService;
    private final Map<String, BranchPermissionManagementBlock> blocks = Maps.newLinkedHashMap();

    /**
     * Created each time {@link #showGroupsDialog(String)} is invoked.
     */
    private ManageUserGroupsDialogVm groupsDialogVm;
    private PoulpeBranch branch;

    /**
     * Constructs the VM with given dependencies
     * 
     * @param branchService branch service
     * @param groupService group service
     */
    public BranchPermissionManagementVm(@Nonnull BranchService branchService, @Nonnull GroupService groupService,
            @Nonnull SelectedEntity<PoulpeBranch> selectedEntity) {
        this.groupService = groupService;
        this.branchService = branchService;
        branch = selectedEntity.getEntity();
        initDataForView();
    }

    /**
     * Changes the sorting of the list of added groups to the opposite side (e.g. was desc, and will become asc.).
     */
    @Command
    public void sortAddedList() {
        groupsDialogVm.revertSortingOfAddedList();
    }

    /**
     * Changes the sorting of the list of available groups to the opposite side (e.g. was desc, and will become asc.).
     */
    @Command
    public void sortAvailableList() {
        groupsDialogVm.revertSortingOfAvailableList();

    }

    /**
     * @param params from zul
     */
    @Command
    public void showGroupsDialog(@BindingParam("params") String params) {
        Map<String, String> parsedParams = parseParams(params);
        String permissionName = parsedParams.get("permissionName");
        BranchPermissionManagementBlock branchPermissionManagementBlock = blocks.get(permissionName);
        String mode = parsedParams.get("mode");
        List<PoulpeGroup> toFillAddedGroupsGrid = getGroupsDependingOnMode(mode, branchPermissionManagementBlock);
        Window branchWindow = (Window) getComponent("branchPermissionManagementWindow");
        groupsDialogVm = createDialogData(toFillAddedGroupsGrid, "allow".equalsIgnoreCase(mode),
                branchPermissionManagementBlock.getPermission());
        Executions.createComponents("/sections/ManageGroupsDialog.zul", branchWindow, null);
    }

    /**
     * Parses params from zul to map
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
     * Closes the dialog
     */
    @Command
    public void dialogClosed() {
        groupsDialogVm = null;
    }

    /**
     * Saves the state
     */
    @Command
    public void saveDialogState() {
        AclChangeset accessChanges = new AclChangeset(groupsDialogVm.getPermission());
        accessChanges.setNewlyAddedGroups(groupsDialogVm.getNewAdded());
        accessChanges.setRemovedGroups(groupsDialogVm.getRemovedFromAdded());
        if (groupsDialogVm.isAllowAccess() && !accessChanges.isEmpty()) {
            branchService.changeGrants(branch, accessChanges);
        } else if (!groupsDialogVm.isAllowAccess() && !accessChanges.isEmpty()) {
            branchService.changeRestrictions(branch, accessChanges);
        }
        dialogClosed();

        // reloading the page, couldn't find a better way yet
        Executions.getCurrent().sendRedirect("");
    }

    /**
     * Searches for the list items that were selected in the list of available groups and removes them from that list;
     * then it adds those items to the list of already added groups.
     */
    @Command
    public void moveSelectedToAdded() {
        groupsDialogVm.moveSelectedToAddedGroups();
    }

    /**
     * Searches for the list items that were selected in the list of added groups and removes them from that list; then
     * it adds those items to the list of available groups.
     */
    @Command
    public void moveSelectedFromAdded() {
        groupsDialogVm.moveSelectedFromAddedGroups();
    }

    /**
     * Moves all the list items from the list of available groups to the list of added.
     */
    @Command
    public void moveAllToAdded() {
        groupsDialogVm.moveAllToAddedGroups();
    }

    /**
     * Moves all the list items from the list of added to the list of available groups.
     */
    @Command
    public void moveAllFromAdded() {
        groupsDialogVm.moveAllFromAddedGroups();
    }

    /**
     * Initializes the data for view
     */
    private void initDataForView() {
        BranchAccessList groupAccessList = branchService.getGroupAccessListFor(branch);
        for (BranchPermission permission : groupAccessList.getPermissions()) {
            BranchPermissionRow allowRow = BranchPermissionRow.newAllowRow(groupAccessList.getAllowed(permission));
            BranchPermissionRow restrictRow = BranchPermissionRow.newRestrictRow(groupAccessList
                    .getRestricted(permission));
            blocks.put(permission.getName(), new BranchPermissionManagementBlock(permission, allowRow, restrictRow));
        }
    }

    /**
     * @param mode restriction mode
     * @param branchPermissionManagementBlock from which groups are retrieved
     * @return list of groups
     */
    private List<PoulpeGroup> getGroupsDependingOnMode(String mode,
            BranchPermissionManagementBlock branchPermissionManagementBlock) {
        if ("allow".equalsIgnoreCase(mode)) {
            return branchPermissionManagementBlock.getAllowRow().getGroups();
        } else {
            return branchPermissionManagementBlock.getRestrictRow().getGroups();
        }
    }

    /**
     * Prepares data for the dialog
     * 
     * @param addedGroups
     * @param allowAccess
     * @param permission
     * @return {@link ManageUserGroupsDialogVm} instance
     */
    private ManageUserGroupsDialogVm createDialogData(List<PoulpeGroup> addedGroups, boolean allowAccess,
            JtalksPermission permission) {
        List<PoulpeGroup> allGroups = groupService.getAll();
        allGroups.removeAll(addedGroups);
        return new ManageUserGroupsDialogVm(permission, allowAccess).setAvailableGroups(allGroups).setAddedGroups(
                addedGroups);
    }

    /**
     * @param id of the component
     * @return {@link Component}
     */
    private Component getComponent(String id) {
        return Executions.getCurrent().getDesktop().getFirstPage().getFellow(id);
    }

    /**
     * @return {@link ListModel} with all block elements
     */
    public ListModel<BranchPermissionManagementBlock> getBlocksListModel() {
        return new BindingListModelList<BranchPermissionManagementBlock>(getBlocks(), true);
    }

    /**
     * @return {@link ManageUserGroupsDialogVm} instance
     */
    public ManageUserGroupsDialogVm getGroupsDialogVm() {
        return groupsDialogVm;
    }

    /**
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
