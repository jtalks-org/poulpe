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

import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.pages.Pages;
import org.jtalks.poulpe.service.UserService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * View-Model for banning of users purposes.
 *
 * @author maxim reshetov
 */
public class UserBanningVm {
    private final UserService userService;
    /** Max count to combobox of users with filter **/
    private final static int MAX_COMBOBOX_SIZE = 10;
    private static final String AVAILABLE_USERS_PROP = "availableUsers", AVAILABLE_FILTER_TEXT = "availableFilterText",
            BANNED_USERS_PROP = "bannedUsers", ADD_BAN_FOR_PROP = "addBanFor";

    /**
     * User selected in list of banned, also this instance used by window for editing ban properties.
     */
    private PoulpeUser selectedUser;

    /**
     * User selected in list of available users to add ban for.
     */
    private PoulpeUser addBanFor;

    /**
     * Text to filter users by username in available list.
     */
    private String availableFilterText = "";

    /**
     * @param userService used to obtain data related to users for VM
     */
    public UserBanningVm(@Nonnull UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets list of user which hasn't banned state.
     *
     * @return list of users, list instance is UNMODIFIABLE
     */
    @Nonnull
    public List<PoulpeUser> getAvailableUsers() {
        return userService.getNonBannedUsersByUsername(availableFilterText, Pages.paginate(0, MAX_COMBOBOX_SIZE));
    }

    /**
     * Gets list of user which already has banned state.
     *
     * @return list of users, list instance is UNMODIFIABLE
     */
    @Nonnull
    public List<PoulpeUser> getBannedUsers() {
        return userService.getAllBannedUsers();
    }

    /**
     * Gets currently selected user. This user instance used by window to edit ban properties.
     *
     * @return currently selected user, can be {@code null}
     */
    @Nullable
    public PoulpeUser getSelectedUser() {
        return selectedUser;
    }

    /**
     * Sets selected banned user
     *
     * @param selectedUser the banned user currently selected
     */
    public void setSelectedUser(PoulpeUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    /**
     * Gets user selected to ban.
     *
     * @return the user currently selected to add ban state
     */
    @Nullable
    public PoulpeUser getAddBanFor() {
        return addBanFor;
    }

    /**
     * Sets user selected to ban.
     *
     * @param addBanFor the user which be used to add ban state
     */
    public void setAddBanFor(@Nonnull PoulpeUser addBanFor) {
        this.addBanFor = addBanFor;
    }

    /**
     * Sets new value to filter text for users in available list. This value later will be used to filter users by
     * username in list of available users.
     *
     * @param filterText the text to filter by
     */
    @Command
    @NotifyChange({AVAILABLE_USERS_PROP})
    public void setAvailableFilter(@Nonnull @BindingParam("filterText") String filterText) {
        this.availableFilterText = filterText;
    }

    /**
     * Add user to Banned Users group
     */
    @Command
    @NotifyChange({ADD_BAN_FOR_PROP, BANNED_USERS_PROP, AVAILABLE_FILTER_TEXT})
    public void banUser() {
        userService.banUsers(addBanFor);
        availableFilterText = "";
        addBanFor = null;
    }

    /**
     * Revoke user ban
     */
    @Command
    @NotifyChange({AVAILABLE_USERS_PROP, BANNED_USERS_PROP})
    public void revokeBan() {
        userService.revokeBan(selectedUser);
        selectedUser = null;
    }

}