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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.collections.CollectionUtils;
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
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;

/**
 * Feeds the dialog for adding/removing groups for permissions. The page has 2 lists: available groups & those that are
 * already granted/restricted to the permission. So when the user selects some items from one list and moves them to
 * another, a command is triggered in {@link BranchPermissionManagementVm} which then delegates the actual changing of
 * the lists to this view model.
 * 
 * @see BranchPermissionManagementVm
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
@NotThreadSafe
public class ManageUserGroupsDialogVm {
    private final ListModelList<PoulpeGroup> availableListModel = new BindingListModelList<PoulpeGroup>(
            new ArrayList<PoulpeGroup>(), false);
    private final ListModelList<PoulpeGroup> addedListModel = new BindingListModelList<PoulpeGroup>(
            new ArrayList<PoulpeGroup>(), false);

    private final Comparator<PoulpeGroup> byNameComparator = new PoulpeGroup.ByNameComparator<PoulpeGroup>();
    private final Set<PoulpeGroup> originallyAdded = new HashSet<PoulpeGroup>();
    private final JtalksPermission permission;
    private final boolean allowAccess;

    private boolean addedListAscendingSorting = true;
    private boolean availableListAscendingSorting = true;

    public static final String BRANCH_PERMISSION_MANAGEMENT_ZUL = "/sections/BranchPermissionManagement.zul";

    private WindowManager windowManager;
    private BranchService branchService;

    private SelectedEntity<Object> selectedEntity;
    private PoulpeBranch branch;

    public ManageUserGroupsDialogVm(@Nonnull JtalksPermission permission, boolean allowAccess) {
        this.permission = permission;
        this.allowAccess = allowAccess;
    }

    public ManageUserGroupsDialogVm(@Nonnull WindowManager windowManager, @Nonnull BranchService branchService,
            @Nonnull GroupService groupService, @Nonnull SelectedEntity<Object> selectedEntity) {
        AclModePermission modePermission = (AclModePermission) selectedEntity.getEntity();
        
        this.branch = (PoulpeBranch) modePermission.getDest();
        this.permission = modePermission.getPermission();
        this.allowAccess = (modePermission.getMode() == AclMode.ALLOWED);

        this.windowManager = windowManager;
        this.branchService = branchService;
        this.selectedEntity = selectedEntity;

        BranchAccessList accessList = branchService.getGroupAccessListFor(branch);

        List<PoulpeGroup> addedGroups = (allowAccess) ? accessList.getAllowed((BranchPermission) permission)
                : accessList.getRestricted((BranchPermission) permission);

        List<PoulpeGroup> allGroups = groupService.getAll();
        allGroups.removeAll(addedGroups);

        setAvailableGroups(allGroups);
        setAddedGroups(addedGroups);
    }

    /**
     * Searches for the list items that were selected in the list of available groups and removes them from that list;
     * then it adds those items to the list of already added groups.
     * 
     * @return the groups that were moved from list of available groups to the list of those that were added
     */
    public Set<PoulpeGroup> moveSelectedToAddedGroups() {
        Set<PoulpeGroup> selection = new HashSet<PoulpeGroup>(availableListModel.getSelection());
        for (PoulpeGroup nextSelectedItem : selection) {
            addedListModel.add(nextSelectedItem);
            availableListModel.remove(nextSelectedItem);
        }
        addedListModel.sort(byNameComparator, addedListAscendingSorting);
        return selection;
    }

    /**
     * Searches for the list items that were selected in the list of added groups and removes them from that list; then
     * it adds those items to the list of available groups.
     * 
     * @return those selected groups that were moved
     */
    public Set<PoulpeGroup> moveSelectedFromAddedGroups() {
        Set<PoulpeGroup> selection = new HashSet<PoulpeGroup>(addedListModel.getSelection());
        for (PoulpeGroup nextSelectedItem : selection) {
            availableListModel.add(nextSelectedItem);
            addedListModel.remove(nextSelectedItem);
        }
        availableListModel.sort(byNameComparator, availableListAscendingSorting);
        return selection;
    }

    /**
     * Moves all the list items from the list of available groups to the list of added.
     * 
     * @return the groups that were moved
     */
    public Set<PoulpeGroup> moveAllToAddedGroups() {
        Set<PoulpeGroup> innerAvailableList = new HashSet<PoulpeGroup>(availableListModel.getInnerList());
        for (PoulpeGroup nextSelectedItem : innerAvailableList) {
            addedListModel.add(nextSelectedItem);
            availableListModel.remove(nextSelectedItem);
        }
        addedListModel.sort(byNameComparator, addedListAscendingSorting);
        return innerAvailableList;
    }

    /**
     * Moves all the list items from the list of added to the list of available groups.
     * 
     * @return the groups that were moved
     */
    public Set<PoulpeGroup> moveAllFromAddedGroups() {
        Set<PoulpeGroup> innerAddedList = new HashSet<PoulpeGroup>(addedListModel.getInnerList());
        for (PoulpeGroup nextSelectedItem : innerAddedList) {
            availableListModel.add(nextSelectedItem);
            addedListModel.remove(nextSelectedItem);
        }
        availableListModel.sort(byNameComparator, availableListAscendingSorting);
        return innerAddedList;
    }

    /**
     * Changes the sorting of the list of added groups to the opposite side (e.g. was desc, and will become asc.).
     */
    public void revertSortingOfAddedList() {
        addedListAscendingSorting = !addedListAscendingSorting;
        addedListModel.sort(byNameComparator, addedListAscendingSorting);
    }

    /**
     * Changes the sorting of the list of available groups to the opposite side (e.g. was desc, and will become asc.).
     */
    public void revertSortingOfAvailableList() {
        availableListAscendingSorting = !availableListAscendingSorting;
        availableListModel.sort(byNameComparator, availableListAscendingSorting);
    }

    /**
     * Clears the previous content of the list of available groups and fills it with the new groups.
     * 
     * @param availableGroups new group list to feed the list box of groups that are available for adding
     * @return this
     */
    public ManageUserGroupsDialogVm setAvailableGroups(@Nonnull List<PoulpeGroup> availableGroups) {
        this.availableListModel.clear();
        this.availableListModel.addAll(availableGroups);
        return this;
    }

    /**
     * Clears the previous content of the list those groups that already granted to the permission and fills it with the
     * new groups.
     * 
     * @param addedGroups new group list to feed the list box of groups that are already granted to the permission
     * @return this
     */
    public ManageUserGroupsDialogVm setAddedGroups(@Nonnull List<PoulpeGroup> addedGroups) {
        this.addedListModel.clear();
        this.addedListModel.addAll(addedGroups);
        this.originallyAdded.clear();
        this.originallyAdded.addAll(addedGroups);
        return this;
    }

    /**
     * Figures out what elements were removed after all the actions of the user. Let's say he moved 2 elements from the
     * added list, and then returned one of them back. This will result in 1 element to be removed from the added list.
     * 
     * @return the elements that were removed by the moment from the added list
     */
    @SuppressWarnings("unchecked")
    public Collection<PoulpeGroup> getRemovedFromAdded() {
        return CollectionUtils.subtract(originallyAdded, addedListModel.getInnerList());
    }

    /**
     * Figures out what elements were added after all the actions of the user. Let's say he moved 10 elements from the
     * available list, and then returned 2 of them back. This will result in 8 elements being new to the added list.
     * 
     * @return the elements that were added by the moment to the list of added groups
     */
    @SuppressWarnings("unchecked")
    public Collection<PoulpeGroup> getNewAdded() {
        return CollectionUtils.subtract(addedListModel.getInnerList(), originallyAdded);
    }

    /**
     * Feeds the list box of groups that are available to be granted to the permission.
     * 
     * @return groups that are available to be granted to the permission
     */
    public ListModelList<PoulpeGroup> getAvailableGroups() {
        return availableListModel;
    }

    /**
     * Feeds the list box of groups that are already granted to the permission.
     * 
     * @return groups that are available to be granted to the permission
     */
    public ListModelList<PoulpeGroup> getAddedGroups() {
        return addedListModel;
    }

    public JtalksPermission getPermission() {
        return permission;
    }

    public boolean isAllowAccess() {
        return allowAccess;
    }

    // -------- from BranchPermissionManagementVm -----------------

    /**
     * Changes the sorting of the list of added groups to the opposite side (e.g. was desc, and will become asc.).
     */
    @Command
    public void sortAddedList() {
        revertSortingOfAddedList();
    }

    /**
     * Changes the sorting of the list of available groups to the opposite side (e.g. was desc, and will become asc.).
     */
    @Command
    public void sortAvailableList() {
        revertSortingOfAvailableList();

    }

    /**
     * Closes the dialog
     */
    @Command
    public void dialogClosed() {
// groupsDialogVm = null;
        selectedEntity.setEntity(branch);
        windowManager.open(BRANCH_PERMISSION_MANAGEMENT_ZUL);
    }

    /**
     * Saves the state
     */
    @Command
    public void saveDialogState() {
        AclChangeset accessChanges = new AclChangeset(permission);
        accessChanges.setNewlyAddedGroups(getNewAdded());
        accessChanges.setRemovedGroups(getRemovedFromAdded());
        if (isAllowAccess() && !accessChanges.isEmpty()) {
            branchService.changeGrants(branch, accessChanges);
        } else if (!isAllowAccess() && !accessChanges.isEmpty()) {
            branchService.changeRestrictions(branch, accessChanges);
        }
        dialogClosed();

        // reloading the page, couldn't find a better way yet
// Executions.getCurrent().sendRedirect("");
    }

    /**
     * Searches for the list items that were selected in the list of available groups and removes them from that list;
     * then it adds those items to the list of already added groups.
     */
    @Command
    public void moveSelectedToAdded() {
        moveSelectedToAddedGroups();
    }

    /**
     * Searches for the list items that were selected in the list of added groups and removes them from that list; then
     * it adds those items to the list of available groups.
     */
    @Command
    public void moveSelectedFromAdded() {
        moveSelectedFromAddedGroups();
    }

    /**
     * Moves all the list items from the list of available groups to the list of added.
     */
    @Command
    public void moveAllToAdded() {
        moveAllToAddedGroups();
    }

    /**
     * Moves all the list items from the list of added to the list of available groups.
     */
    @Command
    public void moveAllFromAdded() {
        moveAllFromAddedGroups();
    }

//    /**
//     * @param mode restriction mode
//     * @param branchPermissionManagementBlock from which groups are retrieved
//     * @return list of groups
//     */
//    private List<PoulpeGroup> getGroupsDependingOnMode(String mode,
//            BranchPermissionManagementBlock branchPermissionManagementBlock) {
//        if ("allow".equalsIgnoreCase(mode)) {
//            return branchPermissionManagementBlock.getAllowRow().getGroups();
//        } else {
//            return branchPermissionManagementBlock.getRestrictRow().getGroups();
//        }
//    }

//  /**
//  * Prepares data for the dialog
//  * 
//  * @param addedGroups
//  * @param allowAccess
//  * @param permission
//  * @return {@link ManageUserGroupsDialogVm} instance
//  */
// private ManageUserGroupsDialogVm createDialogData(List<PoulpeGroup> addedGroups, boolean allowAccess,
//         JtalksPermission permission) {
//     List<PoulpeGroup> allGroups = groupService.getAll();
//     allGroups.removeAll(addedGroups);
//     return new ManageUserGroupsDialogVm(permission, allowAccess).setAvailableGroups(allGroups).setAddedGroups(
//             addedGroups);
// }

// /**
//  * @param id of the component
//  * @return {@link Component}
//  */
// private Component getComponent(String id) {
//     return Executions.getCurrent().getDesktop().getFirstPage().getFellow(id);
// }

}
