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

import javax.annotation.Nonnull;

import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.validator.ValidationFailure;
import org.jtalks.poulpe.validator.ValidationFailureHandler;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * ViewModel for users page.
 * 
 * @author dim42
 */
public class UsersVm implements ValidationFailure {
    public static final String EDIT_USER_URL = "/WEB-INF/pages/users/edit_user.zul";
    public static final String EDIT_USER_DIALOG = "#editUserDialog";
    private UserService userService;
    private ListModelList<User> users;
    private String searchString;
    private User selectedUser;
    @Wire
    private Window usersWindow;
    private ZkHelper zkHelper;
    private EntityValidator entityValidator;
    private ValidationFailureHandler handler;

    public UsersVm(@Nonnull UserService userService, EntityValidator entityValidator) {
        this.userService = userService;
        this.entityValidator = entityValidator;
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
        if (validate(user)) {
            userService.updateUser(user);
            closeEditDialog();
        }
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

    private boolean validate(User user) {
        ValidationResult result = entityValidator.validate(user);

        if (result.hasErrors()) {
            validationFailure(result);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void validationFailure(ValidationResult result) {
        handler = new ValidationFailureHandler("userName", (Textbox) zkHelper.getCurrentComponent("userName"));
        handler.validationFailure(result);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ListModelList<User> getUsers() {
        this.users = new BindingListModelList<User>(userService.getAll(), true);
        return users;
    }

    public void setUsers(ListModelList<User> users) {
        this.users = users;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setZkHelper(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }
}