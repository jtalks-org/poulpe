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
package org.jtalks.poulpe.web.controller.userbanning;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * View-Model for banning of users purposes.
 * 
 * @author Tatiana Birina
 * @author Vyacheslav Zhivaev
 */
public class UserBanningVm {

    // Injected
    private UserService userService;

    /**
     * User selected in list of available users.
     */
    private User selectedUser;

    /**
     * Flag variable which indicates that window to edit ban properties should be shown.
     */
    private boolean editBanWindowOpened = false;

    /**
     * Constructs VM.
     * 
     * @param userService the UserService instance, used to obtain data related to users for VM
     */
    public UserBanningVm(@Nonnull UserService userService) {
        this.userService = userService;
    }

    //-- Accessors ------------------------------

    /**
     * Gets list of user which hasn't banned state.
     * 
     * @return list of users, list instance is UNMODIFIABLE
     */
    @Nonnull
    public List<User> getAvailableUsers() {
        List<User> users = userService.getAll();
        users.removeAll(userService.getAllBannedUsers());
        return Collections.unmodifiableList(users);
    }

    /**
     * Gets list of user which already has banned state.
     * 
     * @return list of users, list instance is UNMODIFIABLE
     */
    @Nonnull
    public List<User> getBannedUsers() {
        return Collections.unmodifiableList(userService.getAllBannedUsers());
    }

    /**
     * Gets user which selected in view. This user can be used later to edit ban properties.
     * 
     * @return currently selected user, can be {@code null}
     */
    @Nullable
    public User getSelectedUser() {
        return selectedUser;
    }

    /**
     * Sets user which selected in view. This user can be used later to edit ban properties.
     * 
     * @see #addBanToSelectedUser()
     * @see #saveBanProperties()
     * @param selectedUser the user instance to set
     */
    public void setSelectedUser(@Nonnull User selectedUser) {
        this.selectedUser = selectedUser;
    }

    /**
     * Gets status of editBanWindowOpened flag. This flag used to control visibility of window which used to edit ban
     * properties of user.
     * 
     * @return the editBanWindowOpened state
     */
    public boolean isEditBanWindowOpened() {
        return editBanWindowOpened;
    }

    /**
     * Sets status of editBanWindowOpened flag. This flag used to control visibility of window which used to edit ban
     * properties of user.
     * 
     * @param editBanWindowOpened the editBanWindowOpened to set
     */
    public void setEditBanWindowOpened(boolean editBanWindowOpened) {
        this.editBanWindowOpened = editBanWindowOpened;
    }

    //-- ZK bindings ----------------------------

    /**
     * Set banned state to selected user.
     */
    @Command
    @NotifyChange({ "editBanWindowOpened" })
    public void addBanToSelectedUser() {
        editBan(selectedUser);
    }

    /**
     * Edit ban properties for specified user.
     * 
     * @param user the user to edit for
     */
    @Command
    @NotifyChange({ "editBanWindowOpened" })
    public void editBan(@Nonnull @BindingParam("user") User user) {
        selectedUser = user;
        editBanWindowOpened = true;
    }

    /**
     * Revoke ban for specified user.
     * 
     * @param user the user to revoke for
     */
    @Command
    @NotifyChange({ "availableUsers", "bannedUsers" })
    public void revokeBan(@Nonnull @BindingParam("user") User user) {
        user.setBanReason(null);
        userService.updateUser(user);
    }

    /**
     * Save ban properties to already selected user. User must be already selected by {@link #editBan(User)} or
     * {@link #addBanToSelectedUser()}. This method also closes window for ban editing.
     */
    @Command
    @NotifyChange({ "availableUsers", "bannedUsers", "editBanWindowOpened" })
    public void saveBanProperties() {
        Validate.notNull(selectedUser, "To provide save action for user, user must be already selected");

        userService.updateUser(selectedUser);
        editBanWindowOpened = false;
    }

    /**
     * Close window for ban editing.
     */
    @Command
    @NotifyChange({ "editBanWindowOpened" })
    public void closeEditBanWindow() {
        editBanWindowOpened = false;
    }
}
