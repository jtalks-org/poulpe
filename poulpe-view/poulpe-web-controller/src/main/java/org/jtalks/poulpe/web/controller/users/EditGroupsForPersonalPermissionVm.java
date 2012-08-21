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

import com.google.common.collect.Lists;
import org.apache.commons.collections.ListUtils;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.TwoSideListWithFilterVm;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import javax.annotation.Nonnull;
import java.util.List;
import org.jtalks.common.model.permissions.ProfilePermission;


/**
 * Feeds the dialog for adding/removing groups for permissions. The page has 2
 * lists: available groups & those that are already granted/restricted to the
 * permission. So when the user selects some items from one list and moves them
 * to another, a command is triggered in {@link UserPersonalPermissionsVm}
 * which then delegates the actual changing of the lists to this view model.
 *
 * @author Enykey
 * @see UserPersonalPermissionsVm
 */
public class EditGroupsForPersonalPermissionVm extends TwoSideListWithFilterVm<Group> {

    private final WindowManager windowManager;
    private final GroupService groupService;
    private final SelectedEntity<Object> selectedEntity;
    // Related to internal state
    private final PermissionForEntity permissionForEntity;

    /**
     * Construct VM for editing group list for selected
     * {@link ProfilePermission}.
     *
     * @param windowManager the window manager instance
     * @param groupService the group service instance
     * @param selectedEntity the SelectedEntity contains
     * {@link PermissionForEntity} with data needed for construction VM state
     */
    public EditGroupsForPersonalPermissionVm(@Nonnull WindowManager windowManager,
            @Nonnull GroupService groupService,
            @Nonnull SelectedEntity<Object> selectedEntity) {
        permissionForEntity = (PermissionForEntity) selectedEntity.getEntity();

        this.windowManager = windowManager;
        this.groupService = groupService;
        this.selectedEntity = selectedEntity;

        getStateAfterEdit().addAll(getAlreadyAddedGroupsForMode(permissionForEntity.isAllowed()));
    }

    // -- ZK Command bindings --------------------
    /**
     * Closes the dialog.
     */
    @Command
    public void cancel() {
        openPersonalPermissionsWindow();
    }

    /**
     * Saves the state of PersonalPermission for each group. Entity Group according with its own permission change
     */
    @Command
    public void save() {
        List<Group> alreadyAddedGroups = getAlreadyAddedGroupsForMode(permissionForEntity.isAllowed());

        @SuppressWarnings("unchecked")
        List<Group> addedGroups = getStateAfterEdit();
        for (Group groupForSave : groupService.getAll()) {
            List<Group> listGroupForSave = Lists.newArrayList();
            listGroupForSave.add(groupForSave);
            PermissionChanges accessChanges = new PermissionChanges(permissionForEntity.getPermission(),
                    ListUtils.intersection(listGroupForSave, ListUtils.subtract(addedGroups, alreadyAddedGroups)),
                    ListUtils.intersection(ListUtils.subtract(alreadyAddedGroups,addedGroups),listGroupForSave));

            if (!accessChanges.isEmpty()) {
                if (permissionForEntity.isAllowed()) {
                    groupService.changeGrants(groupForSave, accessChanges);
                } else {
                    groupService.changeRestrictions(groupForSave, accessChanges);
                }
            }
        }
        openPersonalPermissionsWindow();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Init
    @Override
    public void updateVm() {
        getExist().clear();
        getExist().addAll(getStateAfterEdit());

        getAvail().clear();
        //List<Group> groups = groupService.getSecurityGroups().getAllGroups();
        List<Group> groups = groupService.getAll();
        getAvail().addAll(ListUtils.subtract(groups, getStateAfterEdit()));
    }

    /**
     * Gets list of groups that are allowed/restricted to/from permission for
     * the specified group.
     *
     * @param allowed the permission mode (allowed or restricted)
     * @return list of groups already added for current {@link Group} with
     * specified mode
     */
    private List<Group> getAlreadyAddedGroupsForMode(boolean allowed) {
        GroupsPermissions<ProfilePermission> permissionsMap = groupService.getPersonalPermissions();
        return permissionsMap.get((ProfilePermission) permissionForEntity.getPermission(), allowed);
    }

    /**
     * Opens window with UserPersonalPermissions page.
     */
    private void openPersonalPermissionsWindow() {
        UserPersonalPermissionsVm.showPage(windowManager);
    }
}
