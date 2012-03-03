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

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
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

    // related to internal state
    private AclModePermission aclModePermission;
    private PoulpeBranch branch;

    /**
     * Construct VM for editing group list for selected {@link BranchPermission}.
     * 
     * @param windowManager the window manager instance
     * @param branchService the branch service instance
     * @param groupService the group service instance
     * @param selectedEntity the SelectedEntity contains {@link AclModePermission} with data needed for construction VM
     * state
     */
    public EditGroupsForBranchPermissionVM(@Nonnull WindowManager windowManager, @Nonnull BranchService branchService,
            @Nonnull GroupService groupService, @Nonnull SelectedEntity<Object> selectedEntity) {
        super();

        aclModePermission = (AclModePermission) selectedEntity.getEntity();

        this.windowManager = windowManager;
        this.branchService = branchService;
        this.groupService = groupService;
        this.selectedEntity = selectedEntity;

        branch = (PoulpeBranch) aclModePermission.getTarget();

        consistentState.addAll(getAlreadyAddedGroupsForMode(branch, aclModePermission.getMode()));
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
        List<PoulpeGroup> alreadyAddedGroups = getAlreadyAddedGroupsForMode(branch, mode);

        @SuppressWarnings("unchecked")
        AclChangeset accessChanges = buildChangeset(aclModePermission.getPermission(),
                CollectionUtils.subtract(consistentState, alreadyAddedGroups),
                CollectionUtils.subtract(alreadyAddedGroups, consistentState));

        if (mode == AclMode.ALLOWED && !accessChanges.isEmpty()) {
            branchService.changeGrants(branch, accessChanges);
        } else if (mode == AclMode.RESTRICTED && !accessChanges.isEmpty()) {
            branchService.changeRestrictions(branch, accessChanges);
        }

        openBranchPermissionsWindow();
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

    /**
     * Builds {@link AclChangeset} as result of user edition actions.
     * 
     * @param permission the permission which will be used for creation of {@link AclChangeset}
     * @param newlyAddedGroups the collection of groups which added by user, but NOT EXISTS already
     * @param removedGroups the collection of groups which removed by user AND EXISTS already
     * @return newly created change set
     */
    private AclChangeset buildChangeset(JtalksPermission permission, Collection<PoulpeGroup> newlyAddedGroups,
            Collection<PoulpeGroup> removedGroups) {
        AclChangeset accessChanges = new AclChangeset(permission);
        accessChanges.setNewlyAddedGroups(newlyAddedGroups);
        accessChanges.setRemovedGroups(removedGroups);
        return accessChanges;
    }

    /**
     * Gets list of groups which already added in persistence for current {@link PoulpeBranch} with {@link AclMode}.
     * 
     * @param branch the branch to get for
     * @param mode the ACL permission mode
     * @return list of groups already added for current {@link PoulpeBranch} with {@link AclMode}
     */
    private List<PoulpeGroup> getAlreadyAddedGroupsForMode(PoulpeBranch branch, AclMode mode) {
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

    /**
     * Opens window with BranchPermissions page.
     */
    private void openBranchPermissionsWindow() {
        selectedEntity.setEntity(branch);
        windowManager.open(BRANCH_PERMISSION_MANAGEMENT_ZUL);
    }

}
