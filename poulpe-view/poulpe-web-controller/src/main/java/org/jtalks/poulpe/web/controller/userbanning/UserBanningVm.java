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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.PrefixedTextConverter;
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

    private static final String NOT_SELECTED_ERROR = "To provide save action for user, user must be already selected";

    // Injected
    private final UserService userService;

    /**
     * User selected in list of banned, also this instance used by window for editing ban properties.
     */
    private User selectedUser;

    /**
     * User selected in list of available users to add ban for.
     */
    private User addBanFor;

    /**
     * Flag variable which indicates that window to edit ban properties should be shown.
     */
    private boolean editBanWindowOpened = false;

    /**
     * Converter to wrap strings with prefix in @load expressions.
     */
    private final PrefixedTextConverter prefixedTextConverter = new PrefixedTextConverter();

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
        return users;
    }

    /**
     * Gets list of user which already has banned state.
     * 
     * @return list of users, list instance is UNMODIFIABLE
     */
    @Nonnull
    public List<User> getBannedUsers() {
        return userService.getAllBannedUsers();
    }

    /**
     * Gets currently selected user. This user instance used by window to edit ban properties.
     * 
     * @return currently selected user, can be {@code null}
     */
    @Nullable
    public User getSelectedUser() {
        return selectedUser;
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
     * Gets user selected to ban.
     * 
     * @return the user currently selected to add ban state
     */
    @Nullable
    public User getAddBanFor() {
        return addBanFor;
    }

    /**
     * Sets user selected to ban.
     * 
     * @param addBanFor the user which be used to add ban state
     */
    public void setAddBanFor(@Nonnull User addBanFor) {
        this.addBanFor = addBanFor;
    }

    /**
     * Gets {@link PrefixedTextConverter} which can be used to wrap strings with prefix in @load expressions.
     * 
     * @return the prefixedTextConverter the {@link PrefixedTextConverter} instance
     */
    public PrefixedTextConverter getPrefixedTextConverter() {
        return prefixedTextConverter;
    }

    //-- ZK bindings ----------------------------

    /**
     * Set banned state to selected user.
     * 
     * @throws NotFoundException
     */
    @Command
    @NotifyChange({ "selectedUser", "editBanWindowOpened" })
    public void addBanToUser() {
        Validate.notNull(addBanFor, NOT_SELECTED_ERROR);
        editBan(addBanFor);
    }

    /**
     * Edit ban properties for specified user.
     * 
     * @param userId the id property of user to edit for
     * @throws NotFoundException when user can't be found by specified {@code userId} 
     */
    // why we're using userId, not by User instance? - look comment in userbanning.zul
    @Command
    @NotifyChange({ "selectedUser", "editBanWindowOpened" })
    public void editBan(@Nonnull @BindingParam("userId") long userId) throws NotFoundException {
        editBan(userService.get(userId));
    }

    /**
     * Revoke ban for specified user.
     * 
     * @param userId the id property of user to revoke for
     * @throws NotFoundException when user can't be found by specified {@code userId} 
     */
    // why we're using userId, not by User instance? - look comment in userbanning.zul
    @Command
    @NotifyChange({ "availableUsers", "bannedUsers" })
    public void revokeBan(@Nonnull @BindingParam("userId") long userId) throws NotFoundException {
        userService.updateUser(disableBannedState(userService.get(userId)));
    }

    /**
     * Save ban properties to already selected user. User must be already selected by {@link #editBan(User)} or
     * {@link #addBanToUser()}. This method also closes window for ban editing.
     */
    @Command
    @NotifyChange({ "addBanFor", "selectedUser", "availableUsers", "bannedUsers", "editBanWindowOpened" })
    public void saveBanProperties() {
        Validate.notNull(selectedUser, NOT_SELECTED_ERROR);

        userService.updateUser(enableBannedState(selectedUser));

        if (selectedUser == addBanFor) {
            addBanFor = null;
        }

        closeEditBanWindow();
    }

    /**
     * Close window for ban editing.
     */
    @Command
    @NotifyChange({ "editBanWindowOpened" })
    public void closeEditBanWindow() {
        selectedUser = null;
        editBanWindowOpened = false;
    }

    //-- Utility methods ------------------------

    /**
     * Edit ban properties for specified user.
     * 
     * @param user the user to edit for
     * @throws NotFoundException
     */
    private void editBan(User user) {
        selectedUser = user;
        editBanWindowOpened = true;
    }

    /**
     * Enable banned state for user.
     * 
     * @param user the user to enable for
     * @return the user instance same as parameter
     */
    private User enableBannedState(User user) {
        if (user.getBanReason() == null) {
            user.setBanReason("");
        }
        return user;
    }

    /**
     * Disable banned state for user.
     * 
     * @param user the user to disable for
     * @return the user instance same as parameter
     */
    private User disableBannedState(User user) {
        user.setBanReason(null);
        return user;
    }
}
