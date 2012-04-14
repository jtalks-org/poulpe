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

import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.DialogManager;

import java.util.List;

/**
 * Presenter class for 'ban user' action
 * @author costa
 */
public class UserBanningPresenter {

    private UserService userService; // Service to operate with {@link User} objects
    private DialogManager dialogManager; // Manager to use dialogs in view
    private UserBanningView view;

    /**
     * Method to be invoked when view is initialized
     * @param view the view for banning users
     */
    public void initView(UserBanningView view) {
        this.view = view;
        List<User> users = userService.getAll();
        updateView(users);
    }

    public void updateView(List<User> listOfUsers) {
        view.updateView(listOfUsers);
    }

    public void banBasters(final List<User> usersToBan, final boolean permanent, final Integer daysOfBan,
                           final String reason) {
        dialogManager.confirmBan(usersToBan, reason, new BanningDialog(usersToBan, permanent, daysOfBan, reason));
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public DialogManager getDialogManager() {
        return dialogManager;
    }

    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    public class BanningDialog implements DialogManager.Performable {

        private boolean permanent;
        private List<User> usersToBan;
        private Integer daysOfBan;
        private String reason;

        public BanningDialog(List<User> users, boolean permanent, Integer daysOfBan, String reason) {
            this.usersToBan = users;
            this.permanent = permanent;
            this.daysOfBan = daysOfBan;
            this.reason = reason;
        }

        @Override
        public void execute() {
            if (permanent) {
                userService.setPermanentBanStatus(usersToBan, true, reason);
            } else {
                userService.setTemporaryBanStatus(usersToBan, daysOfBan, reason);
            }
            view.clearView();
        }
    }
}
