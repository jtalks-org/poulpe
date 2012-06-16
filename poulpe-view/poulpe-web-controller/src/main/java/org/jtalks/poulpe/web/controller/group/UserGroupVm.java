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

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * View-model for 'User Groups' Is used to order to work with page that allows admin to manage groups(add, edit,
 * delete). Also class provides access to Members edit window, presented by {@link EditGroupMembersVm}.
 *
 * @author Leonid Kazancev
 */
public class UserGroupVm {
    @SuppressWarnings("JpaQlInspection")
    private static final String SHOW_DELETE_DIALOG = "showDeleteDialog", SHOW_GROUP_DIALOG = "showGroupDialog",
            SELECTED_GROUP = "selectedGroup", MODERAING_GROUP_ID = "moderating_group_id",
            QUERY = "from PoulpeBranch where MODERATORS_GROUP_ID=:", SHOW_MODERATING_BRANCHES = "showModeratingBranches",
            MODERATING_BRANCHES = "moderatingBranches";


    //Injected
    private GroupService groupService;
    private final WindowManager windowManager;
    private final SessionFactory sessionFactory;

    private ListModelList<Group> groups;
    private Group selectedGroup;
    private SelectedEntity<Group> selectedEntity;
    private String searchString = "";
    private BindUtilsWrapper bindWrapper = new BindUtilsWrapper();

    private boolean showDeleteDialog, showGroupDialog, showModeratingBranches;

    /**
     * Construct View-Model for 'User groups' view.
     *
     * @param groupService   the group service instance
     * @param selectedEntity the selected entity instance
     * @param windowManager  the window manager instance
     * @param sessionFactory the session factory instance
     */
    public UserGroupVm(@Nonnull GroupService groupService, @Nonnull SelectedEntity<Group> selectedEntity,
                       @Nonnull WindowManager windowManager, @Nonnull SessionFactory sessionFactory) {
        this.groupService = groupService;
        this.selectedEntity = selectedEntity;
        this.windowManager = windowManager;
        this.sessionFactory = sessionFactory;

        this.groups = new ListModelList<Group>(groupService.getAll(), true);
    }

    /**
     * Makes group list view actual.
     */
    public void updateView() {
        groups.clear();
        groups.addAll(groupService.getAll());
    }

    // -- ZK Command bindings --------------------

    /**
     * Look for the users matching specified pattern from the search textbox.
     */
    @Command
    public void searchGroup() {
        groups.clear();
        groups.addAll(groupService.getByName(searchString));
    }

    /**
     * Opens edit group members window.
     */
    @Command
    public void showGroupMemberEditWindow() {
        selectedEntity.setEntity(selectedGroup);
        EditGroupMembersVm.showDialog(windowManager);
    }

    /**
     * Deletes selected group. Opens branch dialog if group to delete moderating some branches.
     */
    @Command
    @NotifyChange({SELECTED_GROUP, SHOW_DELETE_DIALOG, SHOW_MODERATING_BRANCHES})
    public void deleteGroup() {
        if (hasModeratingGroup()) {
            showModeratingBranches = true;
            bindWrapper.postNotifyChange(UserGroupVm.this, MODERATING_BRANCHES);
        } else {
            groupService.deleteGroup(selectedGroup);
            updateView();
            closeDialog();
        }
    }

    /**
     * Opens group adding dialog.
     */
    @Command
    @NotifyChange({SELECTED_GROUP, SHOW_GROUP_DIALOG})
    public void showNewGroupDialog() {
        selectedGroup = new Group();
        showGroupDialog = true;
    }

    /**
     * Opens group edit dialog.
     */
    @Command
    @NotifyChange({SELECTED_GROUP, SHOW_GROUP_DIALOG})
    public void showEditDialog() {
        showGroupDialog = true;
    }

    /**
     * Saves group, closing group edit(add) dialog and updates view.
     */
    @Command
    @NotifyChange({SHOW_GROUP_DIALOG})
    public void saveGroup() {
        groupService.saveGroup(selectedGroup);
        closeDialog();
        updateView();
    }

    /**
     * Close all dialogs by set visibility to false.
     */
    @Command
    @NotifyChange({SHOW_GROUP_DIALOG, SHOW_DELETE_DIALOG, SHOW_MODERATING_BRANCHES})
    public void closeDialog() {
        showDeleteDialog = false;
        showGroupDialog = false;
        showModeratingBranches = false;
    }

    /**
     * @return list of branches moderated by selected group
     */
    @SuppressWarnings("unchecked")
    public List<PoulpeBranch> getModeratingBranches() {
        if (selectedGroup != null) {
            getSession().flush();
            Query query = getSession().createQuery(QUERY + MODERAING_GROUP_ID);
            query.setParameter(MODERAING_GROUP_ID, selectedGroup.getId());
            return query.list();
        } else return null;
    }

    /**
     * @return current Session instance
     */
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * @return true if group are moderating some branches
     */
    private boolean hasModeratingGroup() {
        return getModeratingBranches().size() != 0;
    }

    // -- Getters/Setters --------------------

    /**
     * Gets visibility status of Delete dialog window.
     *
     * @return true if dialog is visible
     */
    public boolean isShowDeleteDialog() {
        return showDeleteDialog;
    }

    /**
     * Gets visibility status of group(edit/create) dialog window, boolean show added as fix for onClose action,
     * which don't send anything to the server when closing window because of event.stopPropagation, so during next
     * change notification ZK will think that we need to show that dialog again which is wrong.
     *
     * @return true if dialog is visible
     */
    public boolean isShowGroupDialog() {
        boolean show = showGroupDialog;
        showGroupDialog = false;
        return show;
    }

    /**
     * @return true if show branches dialog is visible
     */
    public boolean isShowModeratingBranches() {
        return showModeratingBranches;
    }

    /**
     * @return groupService instance for creating comboboxes.
     */
    public GroupService getGroupService() {
        return groupService;
    }

    /**
     * Gets List of groups which shown at UI.
     *
     * @return Groups currently displayed at UI.
     */
    public ListModelList<Group> getGroups() {
        updateView();
        return groups;
    }

    /**
     * Gets current selected group.
     *
     * @return Group selected at UI.
     */
    public Group getSelectedGroup() {
        return selectedGroup;
    }

    /**
     * Sets current selected group.
     *
     * @param group selected at UI.
     */
    public void setSelectedGroup(Group group) {
        this.selectedGroup = group;
    }

    /**
     * Sets List of groups which shown at UI.
     *
     * @param groups selected at UI.
     */
    public void setGroups(ListModelList<Group> groups) {
        this.groups = groups;
    }

    /**
     * Sets Search string, used for group search.
     *
     * @param searchString string used for group search.
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * @param bindWrapper instance of bindWrapper
     */
    public void setBindWrapper(BindUtilsWrapper bindWrapper) {
        this.bindWrapper = bindWrapper;
    }
}
