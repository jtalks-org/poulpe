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

import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.DialogManager;

/**
 * 
 * @author Konstantin Akimov
 * 
 */
public class GroupPresenter {

    private GroupViewImpl view;
    private GroupService groupService;
    private DialogManager dialogManager;
    private String searchRestrictions;

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    public void initView(GroupViewImpl view) {
        this.view = view;        
        updateView(null);
    }

    public void updateView(String restrictions) {
        searchRestrictions = restrictions;
        updateView();

    }

    public void updateView() {
        view.updateView(groupService.getAllMatchedByName(searchRestrictions));
    }

    public void onAddGroup() {
        view.openNewDialog();
    }

    public void onEditGroup(Group groupToEdit) {
        view.openEditDialog(groupToEdit);
    }

    public void doSearch(String pattern) {
        updateView(pattern);
    }

    public void deleteGroup(final Group groupToDelete) {
        dialogManager.confirmDeletion(groupToDelete.getName(),
                new DialogManager.Performable() {

                    @Override
                    public void execute() {
                        groupService.deleteGroup(groupToDelete);
                        updateView();
                    }
                });
        
    }

}
