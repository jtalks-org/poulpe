package org.jtalks.poulpe.web.controller.userbanning;

import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class UserBanningVM {
    @Wire
    private Window customersWindow;
    private UserService userService;

    private User selectedUser;
    private ListModelList<User> bannedUsers = new BindingListModelList<User>(
            new ArrayList<User>(), true);

    public UserBanningVM(UserService userService) {
        this.userService = userService;
        initData();
    }

    public void initData() {

        bannedUsers.addAll(userService.getAllBannedUsers());
    }

    @Command
    public void addBannedUser() {

        openEditUserDialog(customersWindow);
    }

    @Command
    public void editBannedUser(@Nonnull @BindingParam("user") User user) {
        this.selectedUser = user;
        openEditUserDialog(customersWindow);
    }

    private Component openEditUserDialog(Window customersWindow) {
        return Executions.createComponents(
                "/WEB-INF/pages/edit_banned_user.zul", customersWindow, null);
    }

    @Command
    public void deleteBannedUser() {

    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public ListModelList<User> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(ListModelList<User> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }

}