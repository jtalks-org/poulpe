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

import java.util.List;

import javax.annotation.Nonnull;

import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;

import com.google.common.annotations.VisibleForTesting;

/**
 * ViewModel for users page.
 * 
 * @author dim42
 */
public class UsersVm {
    
    public static final String EDIT_USER_URL = "/WEB-INF/pages/users/edit_user.zul";
    public static final String EDIT_USER_DIALOG = "#editUserDialog";
    
    private static final int ITEMS_PER_PAGE = 50;
    
    private static final String SELECTED_ITEM_PROP = "selectedUser", 
            VIEW_DATA_PROP = "viewData",
            USERS = "users";

    private final UserService userService;
    private ZkHelper zkHelper;

    private List<PoulpeUser> users;
    private PoulpeUser selectedUser;

    /**
     * @param userService the service to get access to users and to store
     * changes to the database
     */
    public UsersVm(@Nonnull UserService userService) {
        this.userService = userService;
    }

    /**
     * Wires users window to this ViewModel.
     * @param component users window
     */
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component component) {
        init(component, new ZkHelper(component));
    }
    
    @VisibleForTesting
    void init(Component component, ZkHelper zkHelper) {
        zkHelper.wireComponents(component, this);
        prepareForListing();
    }
    
    // === listing ===
    
    public void prepareForListing() {
        this.users = getPaginated(0);
    }
    
    public List<PoulpeUser> getUsers() {
        return users;
    }
    
    private List<PoulpeUser> getPaginated(int page) {
        return userService.allUsersPaginated(page + 1, ITEMS_PER_PAGE);
    }
    
    public int getTotalSize() {
        return userService.allUsersCount();
    }

    @NotifyChange({ USERS })
    public void setActivePage(int activePage) {
        this.users = getPaginated(activePage);
    }

    // ===  search ===
    
    @Command
    @NotifyChange({ USERS })
    public void searchUser(@BindingParam(value = "searchString") String searchString) {
        this.users = userService.getUsersByUsernameWord(searchString);
    }
    
    @Command
    @NotifyChange({ USERS })
    public void clearSearch() {
        prepareForListing();
    }
    
    // === edit & save ===

    /**
     * Opens edit user dialog.
     * @param user selected user
     */
    @Command
    public void editUser(@BindingParam(value = "user") PoulpeUser user) {
        selectedUser = user;
        zkHelper.wireToZul(EDIT_USER_URL);
    }

    /**
     * Validates editing user, on success saves him, on failure shows the error
     * message.
     * 
     * @param user editing user
     */
    @Command
    @NotifyChange({ VIEW_DATA_PROP, SELECTED_ITEM_PROP })
    public void saveUser(@BindingParam(value = "user") PoulpeUser user) {
        userService.updateUser(user);
        closeEditDialog();
    }

    @Command
    @NotifyChange({ VIEW_DATA_PROP, SELECTED_ITEM_PROP })
    public void cancelEdit() {
        closeEditDialog();
    }

    private void closeEditDialog() {
        zkHelper.findComponent(EDIT_USER_DIALOG).detach();
    }
  
    /**
     * Gets the user selected on the UI.
     * 
     * @return the user selected on the UI
     */
    public PoulpeUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(PoulpeUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public int getItemsPerPage() {
        return ITEMS_PER_PAGE;
    }
    
    @VisibleForTesting
    void setZkHelper(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }
}