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

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import javax.annotation.Nonnull;

/**
 * @author Leonid Kazancev
 */
public class UserGroupVm {
    public static final String EDIT_GROUP_URL = "/groups/edit_group.zul";
    public static final String EDIT_GROUP_DIALOG = "#editGroupDialog";
    public static final String DELETE_CONFIRM_URL = "/groups/deleteConfirm.zul";
    public static final String DELETE_CONFIRM_DIALOG = "#deleteConfirmDialog";
    public static final String EDIT_GROUP_MEMBERS_URL = "/groups/EditMembers.zul";

    //Injected
    private GroupService groupService;
    private final WindowManager windowManager;

    private ListModelList<Group> groups;
    private Group selectedGroup;
    private SelectedEntity<Group> selectedEntity;
    private String searchString = "";

    private Boolean showDeleteDialog = Boolean.FALSE;
    private Boolean showEditDialog = Boolean.FALSE;
    private Boolean showNewDialog = Boolean.FALSE;

    public UserGroupVm(@Nonnull GroupService groupService, @Nonnull WindowManager windowManager) {
        this.groupService = groupService;
        this.windowManager = windowManager;

        this.groups = new ListModelList<Group>(groupService.getAll(), true);
    }

    /**
     * Makes group list view actual.
     */
    public void updateView() {
        groups.clear();
        groups.addAll(getGroupService().getAll());
    }

    // -- ZK Command bindings --------------------

    /**
     * Look for the users matching specified pattern from the search textbox.
     */
    @Command
    public void searchGroup() {
        groups.clear();
        groups.addAll(groupService.getAllMatchedByName(searchString));
    }

    /**
     * Opens edit group members window.
     */
    @Command
    public void showGroupMemberEditWindow() {
        selectedEntity.setEntity(selectedGroup);
        windowManager.open(EDIT_GROUP_MEMBERS_URL);
    }

    /**
     * Deletes selected group.
     */
    @Command
    @NotifyChange("showDeleteDialog")
    public void deleteGroup() {
        groupService.deleteGroup(selectedGroup);
        closeDialog();
        updateView();
    }

    /**
     * Opens group adding dialog.
     */
    @Command
    @NotifyChange({"showNewDialog", "name", "description"})
    public void addNewGroup() {
        selectedGroup = new Group();
        showNewDialog = true;
    }

    /**
     * Saves group, closing group edit(add) dialog and updates view.
     *
     * @param group editing group
     */

    @Command
    @NotifyChange({"showNewDialog", "showDeleteDialog", "showEditDialog"})
    public void saveGroup(@BindingParam(value = "group") Group group) {
        groupService.saveGroup(group);
        closeDialog();
        updateView();
    }

    @Command
    public void setSelectedGroup(@BindingParam(value = "group") Group group) {
        this.selectedGroup = group;
    }

    private void closeDialog() {
        showNewDialog = false;
        showDeleteDialog = false;
        showEditDialog = false;
    }

    // -- Getters/Setters --------------------

    public Boolean getShowDeleteDialog() {
        return showDeleteDialog;
    }

    public Boolean getShowEditDialog() {
        return showEditDialog;
    }

    public Boolean getShowNewDialog() {
        return showNewDialog;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public ListModelList<Group> getGroups() {
        this.groups = new ListModelList<Group>(groupService.getAll(), true);
        return groups;
    }

    public void setGroups(ListModelList<Group> groups) {
        this.groups = groups;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public SelectedEntity<Group> getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(SelectedEntity<Group> selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

}
