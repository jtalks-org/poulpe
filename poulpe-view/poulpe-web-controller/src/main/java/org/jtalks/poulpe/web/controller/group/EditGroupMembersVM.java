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

import javax.annotation.Nonnull;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

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
    private List<User> avail;
    private User availSelected;

    private String filterExist;
    private List<User> exist;
    private User existSelected;

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
     * @param windowManager the window manager instance
     * @param groupService the group service instance
     * @param userService the user service instance
     * @param groupToEdit the group to be edited
     * @throws NotFoundException
     */
    public EditGroupMembersVM(@Nonnull WindowManager windowManager, @Nonnull GroupService groupService,
            @Nonnull UserService userService, @Nonnull Group groupToEdit) throws NotFoundException {
        this.windowManager = windowManager;
        this.groupService = groupService;
        this.userService = userService;
        this.groupToEdit = groupService.get(groupToEdit.getId());

        usersInGroupAfterEdit = this.groupToEdit.getUsers();
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
     * @param filterAvail the new value for filter text field
     */
    public void setFilterAvail(String filterAvail) {
        this.filterAvail = filterAvail;
    }

    /**
     * Gets selected user in available for adding list.
     *
     * @return the {@link User} instance, indicating selected user
     */
    public User getAvailSelected() {
        return availSelected;
    }

    /**
     * Sets selected user in available for adding list.
     *
     * @param availSelected the new value indicating selected user
     */
    public void setAvailSelected(User availSelected) {
        this.availSelected = availSelected;
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
     * @param filterExist the new value for filter text field
     */
    public void setFilterExist(String filterExist) {
        this.filterExist = filterExist;
    }

    /**
     * Gets selected user in list which represent users already in group.
     *
     * @return the {@link User} instance, indicating selected user
     */
    public User getExistSelected() {
        return existSelected;
    }

    /**
     * Sets selected user in list which represent users already in group.
     *
     * @param existSelected the new value indicating selected user
     */
    public void setExistSelected(User existSelected) {
        this.existSelected = existSelected;
    }

    /**
     * Gets list of users available for adding in group.
     *
     * @return the list of users available for adding in group
     */
    public List<User> getAvail() {
        return avail;
    }

    /**
     * Gets list of users which already exist in group.
     *
     * @return the list of users which already exist in group
     */
    public List<User> getExist() {
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

    // -- ZK Command bindings --------------------

    /**
     * Search users users available for adding in group.
     * After executing this method list of available users would be updated with values of search result.
     */
    @Command
    @NotifyChange({ "avail", "availSelected" })
    public void searchAvail() {
        avail = userService.getUsersByUsernameWord(getFilterAvail());
        avail.removeAll(usersInGroupAfterEdit);
        setAvailSelected(null);
    }

    /**
     * Search users users which already exist in group.
     * After executing this method list of exist users would be updated with values of search result.
     */
    @Command
    @NotifyChange({ "exist", "existSelected" })
    public void searchExist() {
        exist = filter(having(on(User.class).getUsername(), containsString(getFilterExist())), usersInGroupAfterEdit);
        setExistSelected(null);
    }

    /**
     * Add selected user in group.
     */
    @Command
    @NotifyChange({ "avail", "availSelected", "exist", "existSelected" })
    public void add() {
        processAdd(false);
        updateView();
    }

    /**
     * Add all selected users in group.
     */
    @Command
    @NotifyChange({ "avail", "availSelected", "exist", "existSelected" })
    public void addAll() {
        processAdd(true);
        updateView();
    }

    /**
     * Remove selected user from group.
     */
    @Command
    @NotifyChange({ "avail", "availSelected", "exist", "existSelected" })
    public void remove() {
        processRemove(false);
        updateView();
    }

    /**
     * Remove all selected user from group.
     */
    @Command
    @NotifyChange({ "avail", "availSelected", "exist", "existSelected" })
    public void removeAll() {
        processRemove(true);
        updateView();
    }

    /**
     * Save changes provided for group and close edit window.
     */
    @Command
    @NotifyChange
    public void save() {
        groupToEdit.setUsers(usersInGroupAfterEdit);
        groupService.saveGroup(groupToEdit);

        switchToGroupWindow();
    }

    /**
     * Reject any changes for group and close window.
     */
    @Command
    @NotifyChange
    public void cancel() {
        switchToGroupWindow();
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
     * Provide adding users to group, also this method resets currently selected user.
     *
     * @param useAll value, indicating how many user we must process, if {@code true} we must use all items from list,
     *            in otherwise - one selected item
     */
    private void processAdd(boolean useAll) {
        List<User> usersForProcessing = getUsersForProcessing(availSelected, avail, useAll);
        usersInGroupAfterEdit.addAll(usersForProcessing);
    }

    /**
     * Provide removing users from group, also this method resets currently selected user.
     *
     * @param useAll value, indicating how many user we must process, if {@code true} we must use all items from list,
     *            in otherwise - one selected item
     */
    private void processRemove(boolean useAll) {
        List<User> usersForProcessing = getUsersForProcessing(existSelected, exist, useAll);
        usersInGroupAfterEdit.removeAll(usersForProcessing);
    }

    /**
     * Closes currently opened window and opens window with group list.
     */
    private void switchToGroupWindow() {
        windowManager.open("groups.zul");
    }

    /**
     * Gets user for processing.
     *
     * @param oneSelected the user, that can be currently selected
     * @param allSelected the list of users, that can be currently selected
     * @param useAll value, indicating how many user we must process, if {@code true} we must use {@code allSelected},
     *            in otherwise - {@code oneSelected}
     * @return list of users to be processed
     */
    private List<User> getUsersForProcessing(User oneSelected, List<User> allSelected, boolean useAll) {
        if (useAll) {
            return allSelected;
        }
        return Lists.newArrayList(oneSelected);
    }

}
