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

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.text.StringContains.containsString;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

import com.google.common.collect.Lists;

/**
 * View-Model for 'Edit Members of group'.
 * 
 * @author Vyacheslav Zhivaev
 * 
 */
public class EditGroupMembersVM {

    private GroupService groupService;
    private UserService userService;
    private WindowManager windowManager;

    /**
     * Group to be edited
     */
    private Group groupToEdit;

    /**
     * Lists represents state of group members after editing
     */
    private List<User> usersInGroupAfterEdit;

    private String filterAvail;
    private ListModelList<User> avail;

    private String filterExist;
    private ListModelList<User> exist;

    /**
     * Retrieve group selected for editing from current context.
     * 
     * @return {@link Group} instance
     */
    // TODO: this used by Dependency Injection, needs to move it in proper place
    public static Group retrieveGroupToEdit() {
        return (Group) Executions.getCurrent().getDesktop().getAttribute("groupToEdit");
    }

    /**
     * Construct View-Model for 'Edit Members of group' view.
     * 
     * @param windowManager
     *            the window manager instance
     * @param groupService
     *            the group service instance
     * @param userService
     *            the user service instance
     * @param groupToEdit
     *            the group to be edited
     * @throws NotFoundException
     */
    public EditGroupMembersVM(@Nonnull WindowManager windowManager, @Nonnull GroupService groupService,
            @Nonnull UserService userService, @Nonnull SelectedEntity<Group> selectedEntity) throws NotFoundException {
        this.windowManager = windowManager;
        this.groupService = groupService;
        this.userService = userService;
        this.groupToEdit = groupService.get(selectedEntity.getEntity().getId());

        usersInGroupAfterEdit = groupToEdit.getUsers();
        filterAvail = "";
        filterExist = "";

        updateView();
    }

    // -- Accessors ------------------------------

    /**
     * Gets text for filtering users in available for adding list.
     * 
     * @return the text for filter text field
     */
    public String getFilterAvail() {
        return filterAvail;
    }

    /**
     * Sets text for filtering users in available for adding list.
     * 
     * @param filterAvail
     *            the new value for filter text field
     */
    public void setFilterAvail(@Nonnull String filterAvail) {
        this.filterAvail = filterAvail;
    }

    /**
     * Gets text for filtering users which already exist in group.
     * 
     * @return the text for filter text field
     */
    public String getFilterExist() {
        return filterExist;
    }

    /**
     * Sets text for filtering users which already exist in group.
     * 
     * @param filterExist
     *            the new value for filter text field
     */
    public void setFilterExist(@Nonnull String filterExist) {
        this.filterExist = filterExist;
    }

    /**
     * Gets list of users available for adding in group.
     * 
     * @return the list of users available for adding in group
     */
    public ListModelList<User> getAvail() {
        return avail;
    }

    /**
     * Gets list of users which already exist in group.
     * 
     * @return the list of users which already exist in group
     */
    public ListModelList<User> getExist() {
        return exist;
    }

    /**
     * Gets group to be edited.
     * 
     * @return the {@link Group} instance
     */
    public Group getGroupToEdit() {
        return groupToEdit;
    }

    /**
     * Gets set of users which selected in list of available users.
     * 
     * @return set of selected users in list of available users
     */
    public Set<User> getAvailSelected() {
        return avail.getSelection();
    }

    /**
     * Gets selected users which already exist in group.
     * 
     * @return set of selected users which already exist in group
     */
    public Set<User> getExistSelected() {
        return exist.getSelection();
    }

    // -- ZK Command bindings --------------------

    /**
     * Search users users available for adding in group. After executing this method list of available users would be
     * updated with values of search result.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void searchAvail() {
        List<User> users = Lists.newLinkedList(userService.getUsersByUsernameWord(getFilterAvail()));
        users.removeAll(usersInGroupAfterEdit);
        avail = new ListModelList<User>(users);
    }

    /**
     * Search users users which already exist in group. After executing this method list of exist users would be updated
     * with values of search result.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void searchExist() {
        List<User> users = filter(having(on(User.class).getUsername(), containsString(getFilterExist())),
                usersInGroupAfterEdit);
        exist = new ListModelList<User>(users);
    }

    /**
     * Add selected user in group.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void add() {
        usersInGroupAfterEdit.addAll(getAvailSelected());
        updateView();
    }

    /**
     * Add all selected users in group.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void addAll() {
        usersInGroupAfterEdit.addAll(getAvail());
        updateView();
    }

    /**
     * Remove selected user from group.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void remove() {
        usersInGroupAfterEdit.removeAll(exist.getSelection());
        updateView();
    }

    /**
     * Remove all selected user from group.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void removeAll() {
        usersInGroupAfterEdit.removeAll(exist.getInnerList());
        updateView();
    }

    /**
     * Save changes provided for group and close edit window.
     */
    @Command
    public void save() {
        groupToEdit.setUsers(usersInGroupAfterEdit);
        groupService.saveGroup(groupToEdit);
        switchToGroupWindow();
    }

    /**
     * Reject any changes for group and close window.
     */
    @Command
    public void cancel() {
        switchToGroupWindow();
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
     * Updates view.
     */
    private void updateView() {
        searchAvail();
        searchExist();
    }

    /**
     * Closes currently opened window and opens window with group list.
     */
    private void switchToGroupWindow() {
        // TODO: Needs refactoring for window manager, it must looks like: windowManager.openGroupsWindow();
        windowManager.open("groups.zul");
    }

}
