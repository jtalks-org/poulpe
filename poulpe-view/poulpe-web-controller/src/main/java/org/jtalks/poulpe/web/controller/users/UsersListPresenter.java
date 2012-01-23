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

import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.EditListener;
import org.jtalks.poulpe.web.controller.HighConcurrencyEditListener;
import org.jtalks.poulpe.web.controller.WindowManager;

/**
 * Presenter manager displaying a list of
 * {@link org.jtalks.common.model.entity.User}
 * 
 * @author Vytautas Kashchuk
 */
public class UsersListPresenter {

    private UserService userService;
    private WindowManager windowManager;
    private UsersListView view;

    private EditListener<User> editListener = new HighConcurrencyEditListener<User>() {
        @Override
        protected void refreshData() {
            refreshUsersList();
        };
    };

    /**
     * Initialize view instance before first rendering
     * 
     * @param UsersListView
     */
    public void initView(UsersListView view) {
        this.view = view;
        // Potential low performance.
        refreshUsersList();
    }

    public void onEditAction(User user) {
        this.windowManager.openUserWindowForEdit(user, editListener);
    }

    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void onSearchAction() {
        String searchString = view.getSearchString();
        List<User> users = null;
        if (searchString == null || searchString.trim().isEmpty()) {
            // Potentially low performance
            users = userService.getAll();
        } else {
            users = userService.getUsersByUsernameWord(searchString);
        }
        refreshUsersList(users);
    }

    private void refreshUsersList() {
        List<User> list = userService.getAll();
        refreshUsersList(list);
    }

    private void refreshUsersList(List<User> users) {
        view.showUsersList(users);
    }
}
