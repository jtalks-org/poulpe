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

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.TwoSideListWithFilterVm;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.text.StringContains.containsString;

/**
 * View-Model for 'Edit Members of group'.
 *
 * @author Vyacheslav Zhivaev
 */
public class EditGroupMembersVm extends TwoSideListWithFilterVm<PoulpeUser> {
    public static final String EDIT_GROUP_MEMBERS_URL = "/groups/EditMembers.zul";
    private final GroupService groupService;
    private final UserService userService;
    private final WindowManager windowManager;
    /**
     * Group to be edited
     */
    private final Group groupToEdit;

    /**
     * Construct View-Model for 'Edit Members of group' view.
     *
     * @param windowManager  the window manager instance
     * @param groupService   the group service instance
     * @param userService    the user service instance
     * @param selectedEntity the selected entity instance, for obtaining group which to be edited
     * @throws NotFoundException if specified group not exist in persistence
     */
    public EditGroupMembersVm(@Nonnull WindowManager windowManager, @Nonnull GroupService groupService,
                              @Nonnull UserService userService, @Nonnull SelectedEntity<Group> selectedEntity)
            throws NotFoundException {

        groupToEdit = groupService.get(selectedEntity.getEntity().getId());
        this.windowManager = windowManager;
        this.groupService = groupService;
        this.userService = userService;

        List<PoulpeUser> users = (List<PoulpeUser>) (List<?>) groupToEdit.getUsers();
        setStateAfterEdit(users);
    }

    // -- Accessors ------------------------------

    /**
     * Gets group to be edited.
     *
     * @return the {@link Group} instance
     */
    public Group getGroupToEdit() {
        return groupToEdit;
    }

    // -- ZK Command bindings --------------------

    /**
     * Search users users available for adding in group. After executing this method list of available users would be
     * updated with values of search result.
     */
    @Command
    @NotifyChange({AVAIL_PROPERTY, EXIST_PROPERTY, AVAIL_SELECTED_PROPERTY, EXIST_SELECTED_PROPERTY})
    public void filterAvail() {
        List<PoulpeUser> users = Lists.newLinkedList(userService.withUsernamesMatching(getAvailFilterTxt()));
        users.removeAll(getStateAfterEdit());
        getAvail().clear();
        getAvail().addAll(users);
    }

    /**
     * Search users users which already exist in group. After executing this method list of exist users would be updated
     * with values of search result.
     */
    @Command
    @NotifyChange({AVAIL_PROPERTY, EXIST_PROPERTY, AVAIL_SELECTED_PROPERTY, EXIST_SELECTED_PROPERTY})
    public void filterExist() {
        getExist().clear();
        getExist().addAll(filter(having(on(PoulpeUser.class).getUsername(), containsString(getExistFilterTxt())),
                getStateAfterEdit()));
    }

    /**
     * Save changes provided for group and close edit window.
     */
    @Command
    public void save() {
        groupToEdit.setUsers(new ArrayList<org.jtalks.common.model.entity.User>(getStateAfterEdit()));
        groupService.saveGroup(groupToEdit);
        switchToGroupsWindow();
    }

    /**
     * Reject any changes for group and close window.
     */
    @Command
    public void cancel() {
        switchToGroupsWindow();
    }

    // -- Utility methods ------------------------

    /**
     * {@inheritDoc}
     */
    @Init
    public void updateVm() {
        filterAvail();
        filterExist();
    }

    /**
     * Closes currently opened window and opens window with group list.
     */
    private void switchToGroupsWindow() {
        // TODO: Needs refactoring for window manager, it must looks like: windowManager.openGroupsWindow();
        windowManager.open("usergroup.zul");
    }

    /**
     * Opens edit group members dialog window.
     *
     * @param windowManager the window manager instance
     */
    public static void showDialog(WindowManager windowManager) {
        windowManager.open(EDIT_GROUP_MEMBERS_URL);
    }

    // -- Applying pagination ------------------------

    private static final int ITEMS_PER_PAGE = 50;
    private static final String TOTAL_SIZE = "totalSize";
    static final String NO_FILTER_SEARCH_STRING = "",
            ACTIVE_PAGE = "activePage",
            USERS = "users";

    private String searchString = NO_FILTER_SEARCH_STRING;
    private int activePage = 0;
    private List<PoulpeUser> users;

    public int getItemsPerPage() {
        return ITEMS_PER_PAGE;
    }

    /**
     * @return total amount of users matched the searchString
     */
    public int getTotalSize() {
        return userService.countUsernameMatches(searchString);
    }

    private void displayFirstPage(String searchString) {
        this.searchString = searchString;
        setActivePage(0);
    }

    private List<PoulpeUser> usersOf(int page) {
        return userService.findUsersPaginated(searchString, page, ITEMS_PER_PAGE);
    }

    /**
     * Filters all the users using the given string.
     *
     * @param searchString string for filtering
     */
    @Command
    @NotifyChange({USERS, TOTAL_SIZE, ACTIVE_PAGE})
    public void searchUsers(@BindingParam(value = "searchString") String searchString) {
        displayFirstPage(searchString);
    }

    /**
     * Updates the active page value with the current page of pagination.
     * Updates the list of users so it displays the needed page.
     *
     * @param activePage current page of pagination
     */
    @NotifyChange({USERS})
    public void setActivePage(int activePage) {
        this.activePage = activePage;
        this.users = usersOf(activePage);
    }

}
