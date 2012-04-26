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
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.service.GroupService;

public class EditGroupDialogPresenter {

    private EditGroupDialogView view;
    private Group group;
    private GroupService groupService;
    private EntityValidator entityValidator;

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
    
    /**
     * @param entityValidator the entityValidator to set
     */
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    public void initView(EditGroupDialogView view, Group group) {
        this.view = view;
        this.group = group;
    }
    

    public boolean saveOrUpdateGroup(String name, String description) {
        if (group == null) {
            group = new Group();
        }
        group.setName(name);
        group.setDescription(description);
        if (validate(group)) {
            groupService.saveGroup(group);
            view.hide();
            return true;
        } else {
            return false;
        }
    }
    
    private boolean validate(Group group) {
        ValidationResult result = entityValidator.validate(group);

        if (result.hasErrors()) {
            view.validationFailure(result);
            return false;
        } else {
            return true;
        }
    }

    public void editGroup(String name, String description) {
    }

}
