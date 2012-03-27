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

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.text.StringContains.containsString;

import java.util.List;

import javax.annotation.Nonnull;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.TwoSideListWithFilterVm;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.google.common.collect.Lists;

/**
 * View-Model for 'Edit Members of group'.
 * 
 * @author Vyacheslav Zhivaev
 * 
 */
public class EditGroupMembersVm extends TwoSideListWithFilterVm<User> {

    // Injected
    private final GroupService groupService;
    private final UserService userService;
    private final WindowManager windowManager;

    /**
     * PoulpeGroup to be edited
     */
    private final PoulpeGroup groupToEdit;

    /**
     * Construct View-Model for 'Edit Members of group' view.
     * 
     * @param windowManager the window manager instance
     * @param groupService the group service instance
     * @param userService the user service instance
     * @param selectedEntity the selected entity instance, for obtaining group which to be edited
     * @throws NotFoundException if specified group not exist in persistence
     */
    public EditGroupMembersVm(@Nonnull WindowManager windowManager, @Nonnull GroupService groupService,
            @Nonnull UserService userService, @Nonnull SelectedEntity<PoulpeGroup> selectedEntity)
            throws NotFoundException {
        super();

        groupToEdit = groupService.get(selectedEntity.getEntity().getId());

        this.windowManager = windowManager;
        this.groupService = groupService;
        this.userService = userService;

        stateAfterEdit = groupToEdit.getPoulpeUsers();
    }

    // -- Accessors ------------------------------

    /**
     * Gets group to be edited.
     * 
     * @return the {@link PoulpeGroup} instance
     */
    public PoulpeGroup getGroupToEdit() {
        return groupToEdit;
    }

    // -- ZK Command bindings --------------------

    /**
     * Search users users available for adding in group. After executing this method list of available users would be
     * updated with values of search result.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void filterAvail() {
        List<User> users = Lists.newLinkedList(userService.getUsersByUsernameWord(getAvailFilterTxt()));
        users.removeAll(stateAfterEdit);
        avail.clear();
        avail.addAll(users);
    }

    /**
     * Search users users which already exist in group. After executing this method list of exist users would be updated
     * with values of search result.
     */
    @Command
    @NotifyChange({ "avail", "exist", "availSelected", "existSelected" })
    public void filterExist() {
        exist.clear();
        exist.addAll(filter(having(on(User.class).getUsername(), containsString(getExistFilterTxt())), stateAfterEdit));
    }

    /**
     * Save changes provided for group and close edit window.
     */
    @Command
    public void save() {
        groupToEdit.setPoulpeUsers(stateAfterEdit);
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
        windowManager.open("groups.zul");
    }

}
