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

import java.util.List;

import javax.annotation.Nonnull;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

/**
 * @author Vyacheslav Zhivaev
 * 
 */
public class EditGroupMembersVM {

    private GroupService groupService;
    private UserService userService;
    private WindowManager windowManager;

    private Group groupToEdit;

    private String availSearch;
    private List<User> availUsers;
    private User availSelected;

    private String existSearch;
    private List<User> existUsers;
    private User existSelected;

    /**
     * @param groupService
     * @param userService
     * @param dialogManager
     * @param windowManager
     */
    public EditGroupMembersVM(@Nonnull GroupService groupService, @Nonnull UserService userService,
            @Nonnull WindowManager windowManager) {
        this.groupService = groupService;
        this.userService = userService;
        this.windowManager = windowManager;

        groupToEdit = (Group) Executions.getCurrent().getDesktop().getAttribute("groupToEdit");

        availSearch = "";
        existSearch = "";

        searchAvail();
        searchExist();
    }

    // -- Accessors ------------------------------

    /**
     * @return the groupToEdit
     */
    public Group getGroupToEdit() {
        return groupToEdit;
    }

    /**
     * @return the availSearch
     */
    public String getAvailSearch() {
        return availSearch;
    }

    /**
     * @param availSearch
     *            the availSearch to set
     */
    public void setAvailSearch(String availSearch) {
        this.availSearch = availSearch;
    }

    /**
     * @return the availUsers
     */
    public List<User> getAvailUsers() {
        return availUsers;
    }

    /**
     * @param availUsers
     *            the availUsers to set
     */
    public void setAvailUsers(List<User> availUsers) {
        this.availUsers = availUsers;
    }

    /**
     * @return the availSelected
     */
    public User getAvailSelected() {
        return availSelected;
    }

    /**
     * @param availSelected
     *            the availSelected to set
     */
    public void setAvailSelected(User availSelected) {
        this.availSelected = availSelected;
    }

    /**
     * @return the existSearch
     */
    public String getExistSearch() {
        return existSearch;
    }

    /**
     * @param existSearch
     *            the existSearch to set
     */
    public void setExistSearch(String existSearch) {
        this.existSearch = existSearch;
    }

    /**
     * @return the existUsers
     */
    public List<User> getExistUsers() {
        return existUsers;
    }

    /**
     * @param existUsers
     *            the existUsers to set
     */
    public void setExistUsers(List<User> existUsers) {
        this.existUsers = existUsers;
    }

    /**
     * @return the existSelected
     */
    public User getExistSelected() {
        return existSelected;
    }

    /**
     * @param existSelected
     *            the existSelected to set
     */
    public void setExistSelected(User existSelected) {
        this.existSelected = existSelected;
    }

    // -- ZK Command bindings --------------------

    @Command
    @NotifyChange({ "availUsers", "availSelected" })
    public void searchAvail() {
        availUsers = userService.getUsersByUsernameWord(availSearch);
    }

    @Command
    @NotifyChange({ "existUsers", "existSelected" })
    public void searchExist() {
        existUsers = userService.getUsersByUsernameWord(existSearch);
    }

    @Command
    @NotifyChange({ "availUsers", "availSelected", "existUsers", "existSelected" })
    public void add(@BindingParam("direction") String direction) {
        if ("toExist".equals(direction)) {

        } else if ("toAvail".equals(direction)) {

        } else {
            LoggerFactory.getLogger(EditGroupMembersVM.class).warn("Invalid parametr 'direction': " + direction);
        }
    }

    @Command
    @NotifyChange({ "availUsers", "availSelected", "existUsers", "existSelected" })
    public void addAll(@BindingParam("direction") String direction) {
        if ("toExist".equals(direction)) {

        } else if ("toAvail".equals(direction)) {

        } else {
            LoggerFactory.getLogger(EditGroupMembersVM.class).warn("Invalid parametr 'direction': " + direction);
        }
    }

    @Command
    @NotifyChange
    public void save() {
        switchToGroupWindow();
    }

    @Command
    @NotifyChange
    public void cancel() {
        switchToGroupWindow();
    }

    // -- Utility methods ------------------------

    private void switchToGroupWindow() {
        Component workAreaComponent = (Component) Executions.getCurrent().getDesktop()
                .getAttribute("workAreaComponent");
        windowManager.open("groups.zul", workAreaComponent);
    }

}
