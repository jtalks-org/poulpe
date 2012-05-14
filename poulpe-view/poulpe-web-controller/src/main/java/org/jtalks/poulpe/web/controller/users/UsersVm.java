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
package org.jtalks.poulpe.web.controller.users;

import com.google.common.annotations.VisibleForTesting;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;

import javax.annotation.Nonnull;

/**
 * ViewModel for users page.
 *
 * @author dim42
 */
public class UsersVm {
    public static final String EDIT_USER_URL = "/WEB-INF/pages/users/edit_user.zul";
    public static final String EDIT_USER_DIALOG = "#editUserDialog";
    private final UserService userService;
    private final ListModelList<User> users;
    private String searchString;
    private User selectedUser;
    private ZkHelper zkHelper;

    /**
     * @param userService the service to get access to users and to store changes to the database
     */
    public UsersVm(@Nonnull UserService userService) {
        this.userService = userService;
        this.users = new BindingListModelList<User>(userService.getAll(), true);
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
     * Look for the users matching specified pattern from the search textbox.
     */
    @Command
    public void searchUser() {
        users.clear();
        users.addAll(userService.getUsersByUsernameWord(searchString));
    }

    /**
     * Opens edit user dialog.
     *
     * @param user selected user
     */
    @Command
    public void editUser(@BindingParam(value = "user") User user) {
        selectedUser = user;
        zkHelper.wireToZul(EDIT_USER_URL);
    }

    /**
     * Validates editing user, on success saves him, on failure shows the error message.
     *
     * @param user editing user
     */
    @Command
    public void saveUser(@BindingParam(value = "user") User user) {
        userService.updateUser(user);
        closeEditDialog();
    }

    /**
     * Closes edit user dialog.
     */
    @Command
    public void cancelEdit() {
        closeEditDialog();
    }

    private void closeEditDialog() {
        zkHelper.findComponent(EDIT_USER_DIALOG).detach();
    }

    public ListModelList<User> getUsers() {
        users.clear();
        users.addAll(userService.getAll());
        return users;
    }

    /**
     * Sets the search keyword to find the users by it. Is used by ZK Binder.
     *
     * @param searchString search keyword to find the users by it
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * Gets the user selected on the UI.
     *
     * @return the user selected on the UI
     */
    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    @VisibleForTesting
    void setZkHelper(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }
}