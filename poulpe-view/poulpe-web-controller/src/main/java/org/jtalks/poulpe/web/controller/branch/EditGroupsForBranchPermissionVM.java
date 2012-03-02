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

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.AclMode;
import org.jtalks.poulpe.model.dto.AclModePermission;
import org.jtalks.poulpe.model.dto.branches.AclChangeset;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.TwoSideListWithFilterVM;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * Feeds the dialog for adding/removing groups for permissions. The page has 2 lists: available groups & those that are
 * already granted/restricted to the permission. So when the user selects some items from one list and moves them to
 * another, a command is triggered in {@link BranchPermissionManagementVm} which then delegates the actual changing of
 * the lists to this view model.
 * 
 * @see BranchPermissionManagementVm
 * @author Vyacheslav Zhivaev
 */
public class EditGroupsForBranchPermissionVM extends TwoSideListWithFilterVM<PoulpeGroup> {

    public static final String BRANCH_PERMISSION_MANAGEMENT_ZUL = "/sections/BranchPermissionManagement.zul";

    // Injected
    private WindowManager windowManager;
    private BranchService branchService;
    private GroupService groupService;
    private SelectedEntity<Object> selectedEntity;

    private AclModePermission aclModePermission;
    private PoulpeBranch branch;

    public EditGroupsForBranchPermissionVM(@Nonnull WindowManager windowManager, @Nonnull BranchService branchService,
            @Nonnull GroupService groupService, @Nonnull SelectedEntity<Object> selectedEntity) {
        super();

        aclModePermission = (AclModePermission) selectedEntity.getEntity();

        this.windowManager = windowManager;
        this.branchService = branchService;
        this.groupService = groupService;
        this.selectedEntity = selectedEntity;

        branch = (PoulpeBranch) aclModePermission.getTarget();

        consistentState.addAll(getAlreadyAddedGroupsFromPersistence());
    }

    // -- ZK Command bindings --------------------

    /**
     * Closes the dialog.
     */
    @Command
    public void cancel() {
        openBranchPermissionsWindow();
    }

    /**
     * Saves the state.
     */
    @Command
    public void save() {
        AclMode mode = aclModePermission.getMode();
        AclChangeset accessChanges = buildChangeset();

        if (mode == AclMode.ALLOWED && !accessChanges.isEmpty()) {
            branchService.changeGrants(branch, accessChanges);
        } else if (mode == AclMode.RESTRICTED && !accessChanges.isEmpty()) {
            branchService.changeRestrictions(branch, accessChanges);
        }

        openBranchPermissionsWindow();
    }

    /**
     * Dummy command, used only for updating state of view components via binding. It's fired when user select item in
     * any of two list's in window.
     */
    @Command
    @NotifyChange({ "availSelected", "existSelected" })
    public void listSelected() {
        // NOOP
    }

    // -- Utility methods ------------------------

    /**
     * {@inheritDoc}
     */
    @Init
    @Override
    @SuppressWarnings("unchecked")
    public void updateVm() {
        exist.clear();
        exist.addAll(consistentState);

        avail.clear();
        avail.addAll(ListUtils.subtract(groupService.getAll(), consistentState));
    }

    @SuppressWarnings("unchecked")
    private AclChangeset buildChangeset() {
        AclChangeset accessChanges = new AclChangeset(aclModePermission.getPermission());
        List<PoulpeGroup> alreadyAddedGroups = getAlreadyAddedGroupsFromPersistence();
        accessChanges.setNewlyAddedGroups(CollectionUtils.subtract(consistentState, alreadyAddedGroups));
        accessChanges.setRemovedGroups(CollectionUtils.subtract(alreadyAddedGroups, consistentState));
        return accessChanges;
    }

    private List<PoulpeGroup> getAlreadyAddedGroupsFromPersistence() {
        AclMode mode = aclModePermission.getMode();
        BranchAccessList accessList = branchService.getGroupAccessListFor(branch);
        BranchPermission branchPermission = (BranchPermission) aclModePermission.getPermission();

        if (mode == AclMode.ALLOWED) {
            return accessList.getAllowed(branchPermission);
        } else if (mode == AclMode.RESTRICTED) {
            return accessList.getRestricted(branchPermission);
        } else {
            throw new IllegalStateException("Unsupported AclMode value");
        }
    }

    private void openBranchPermissionsWindow() {
        selectedEntity.setEntity(branch);
        windowManager.open(BRANCH_PERMISSION_MANAGEMENT_ZUL);
    }

}
