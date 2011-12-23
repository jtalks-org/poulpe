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
import org.jtalks.poulpe.service.exceptions.NotUniqueException;

public class EditGroupDialogPresenter {

    private EditGroupDialogView view;
    private Group group;
    private GroupService groupService;
    private final static String ERROR_LABEL_SECTION_NAME_WRONG = "sections.editsection.name.err";

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public void initView(EditGroupDialogView view, Group group) {
        this.view = view;
        this.group = group;
    }
    public String validate(String name){
    	if(name == null || name.length() > 245){
    		return ERROR_LABEL_SECTION_NAME_WRONG;
    	}
    	return null;
    }

    public void saveOrUpdateGroup(String name, String description) {
        if (group == null) {
            group = new Group();
        }
        group.setName(name);
        group.setDescription(description);
        try {
            groupService.saveGroup(group);
        } catch (NotUniqueException e) {
            view.notUniqueGroupName();
        }
    }

    public void editGroup(String name, String description) {
    }

}
