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

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;

/**
 * This class provides implementation for the group presenter in pattern Model-View-Presenter.
 * 
 * @author Konstantin Akimov
 * @author Vyacheslav Zhivaev
 *
 */
public class GroupPresenter {

    private GroupViewImpl view;
    private GroupService groupService;
    private DialogManager dialogManager;
    private WindowManager windowManager;
    private String searchRestrictions;
    private SelectedEntity<Group> selectedEntity;

    /**
     * Sets {@link GroupService} for this presenter.
     *
     * @param groupService the groupService to set
     */
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Sets {@link DialogManager} for this presenter.
     * 
     * @param dialogManager the dialogManager to set
     */ 
    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    /**
     * Sets {@link GroupViewImpl} for this presenter and update it.
     * 
     * @param view
     */
    public void initView(GroupViewImpl view) {
        this.view = view;
        updateView(null);
    }

    /**
     * Update view for groups matching the specified pattern.
     * 
     *  @param restrictions - specified pattern for groups to match
     * */
    public void updateView(String restrictions) {
        searchRestrictions = restrictions;
        updateView();

    }

    /**
     * Update view of this presenter.
     */
    public void updateView() {
        view.updateView(groupService.getAllMatchedByName(searchRestrictions));
    }

    /**
     * Opens dialog for the new group. 
     * */ 
    public void onAddGroup() {
        view.openNewDialog();
    }

    /**
     * Opens group edit dialog for the specified groupToEdit. 
     * */
    public void onEditGroup(Group groupToEdit) {
        view.openEditDialog(groupToEdit);
    }

    /**
     * Look for the groups matching specified pattern.
     * */
    public void doSearch(String pattern) {
        updateView(pattern);
    }

    /**
     * Opens group delete dialog for the specified groupToDelete. 
     * */
    public void deleteGroup(final Group groupToDelete) {
        dialogManager.confirmDeletion(groupToDelete.getName(), new Performable() {
            @Override
            public void execute() {
                groupService.deleteGroup(groupToDelete);
                updateView();
            }
        });
    }

    /**
     * Edits user members of the group.
     * */
    public void editMembers(Group selectedGroup) {
        selectedEntity.setEntity(selectedGroup);
        windowManager.open("groups/EditMembers.zul");
    }

    /**
     * Sets {@link  WindowManager} for this presenter.
     * 
     * @param windowManager the windowManager to set
     */
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    /**
     * Sets {@link SelectedEntity} provider.
     * 
     * @param selectedEntity the selectedEntity to set
     */
    public void setSelectedEntity(SelectedEntity<Group> selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

}
