package org.jtalks.poulpe.web.controller.group;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ListModelList;

import javax.annotation.Nonnull;

/**
 * @author Leonid Kazancev
 */
public class UserGroupVm {
    public static final String EDIT_GROUP_URL = "/groups/edit_group.zul";
    public static final String EDIT_GROUP_DIALOG = "#editGroupDialog";
    public static final String DELETE_CONFIRM_URL="/groups/deleteConfirm.zul";
    public static final String DELETE_CONFIRM_DIALOG="#deleteConfirmDialog";
    public static final String EDIT_GROUP_MEMBERS_URL = "/groups/EditMembers.zul";

    //Injected
    private GroupService groupService;
    private final WindowManager windowManager;

    private ListModelList<Group> groups;
    private Group selectedGroup;
    private SelectedEntity<Group> selectedEntity;
    private ZkHelper zkHelper;
    private String searchString = "";


    public UserGroupVm(@Nonnull GroupService groupService, @Nonnull WindowManager windowManager) {
        this.groupService = groupService;
        this.windowManager = windowManager;

        this.groups = new ListModelList<Group>(groupService.getAll(), true);
    }

    /**
     * Wires users window to this ViewModel.
     *
     * @param component users window
     */
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component component) {
        zkHelper = new ZkHelper(component);
        zkHelper.wireComponents(component, this);
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
     * Calls confirm delete dialog.
     */
    @Command
    public void confirmDelete() {
       zkHelper.wireToZul(DELETE_CONFIRM_URL);
    }

    /**
     * Deletes selected group.
     */
    @Command
    public void deleteGroup(){
        groupService.deleteGroup(selectedGroup);
        updateView();
        cancelDelete();
    }

    /**
     * Opens group ading dialog.
     */
    @Command
    public void addGroup() {
        selectedGroup = new Group();
        zkHelper.wireToZul(EDIT_GROUP_URL);
    }

    /**
     * Opens edit group dialog.
     *
     * @param group selected group for editing.
     */
    @Command
    public void editGroup(@BindingParam(value = "group") Group group) {
        selectedGroup = group;
        zkHelper.wireToZul(EDIT_GROUP_URL);
    }

    /**
     * Saves group, closing group edit(add) dialog and updates view.
     *
     * @param group editing group
     */
    @Command
    public void saveGroup(@BindingParam(value = "group") Group group) {
            groupService.saveGroup(group);
            cancelEdit();
            updateView();
    }

    /**
     * Closing edit window without apply changes.
     */
    @Command
    public void cancelEdit() {
        zkHelper.findComponent(EDIT_GROUP_DIALOG).detach();
    }

    /**
     *Closing delete window without apply changes.
     */
    @Command
    public void cancelDelete(){
        zkHelper.findComponent(DELETE_CONFIRM_DIALOG).detach();
    }

    @Command
    public void setSelectedGroup(@BindingParam(value = "group") Group group) {
        this.selectedGroup = group;
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

    public void setZkHelper(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }


    public SelectedEntity<Group> getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(SelectedEntity<Group> selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

}
