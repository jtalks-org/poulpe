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
package org.jtalks.poulpe.web.controller.branch;

import static org.jtalks.common.model.entity.Group.GROUP_ALREADY_EXISTS;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.web.controller.section.SectionPresenter;

/**
 * This class is implementation the branch presenter in pattern
 * Model-View-Presenter
 * 
 * @author Bekrenev Dmitry
 */
public class BranchPresenter {

    private static final String CORRESPONDING_GROUP_ALREADY_EXISTS = "groups.validation.existing_group_name";
    public static final String GROUP_SUFFIX = " Moderators";
    private SectionPresenter sectionPresenter;
    private SectionService sectionService;
    private BranchService branchService;
    private GroupService groupService;
    private EntityValidator entityValidator;
    private BranchDialogView view;

    public SectionPresenter getSectionPresenter() {
        return sectionPresenter;
    }

    public void setSectionPresenter(SectionPresenter sectionPresenter) {
        this.sectionPresenter = sectionPresenter;
    }

    /**
     * Sets the PoulpeBranch instance
     */
    public void setBranchService(BranchService service) {
        branchService = service;
    }

    /**
     * Sets the PoulpeSection instance
     */
    public void setSectionService(SectionService service) {
        sectionService = service;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public void updateView() {
        sectionPresenter.updateView();
    }

    /**
     * Sets the view instance which represent User interface
     */
    public void setView(BranchDialogView view) {
        this.view = view;
    }

    /**
     * Init view initial data
     */
    public void initView() {
        view.initSectionList(sectionService.getAll());
    }


    /**
     * Save new or edited branch in db In case when branch with equal name
     * exists, cause open error popup in view.
     */
    public boolean saveBranch() {
        PoulpeSection section = view.getSection();
        PoulpeBranch branch = view.getBranch(section);
        return saveBranch(branch);
    }

    /**
     * When a new branch is saved, then a new group is created using this branch's name: '[New_branch_name] Moderators'.
     * And branch is added permissions with this group.
     * When editing branch (its name), its group's name changed respectively.
     * 
     * @param branch branch to save/edit
     * @return true on success creation/modification, false on branch or group validation failure
     */
    protected boolean saveBranch(PoulpeBranch branch) {
        if (validate(branch)) {
            PoulpeSection section = branch.getPoulpeSection();
            section.addOrUpdateBranch(branch);
            PoulpeGroup group = createOrGetExistingGroup(branch);
            if(validate(group)) {
                branch.addOrUpdateGroup(group);
            } else {
                return false;
            }
            sectionService.saveSection(section);
            setBranchPermissions(branch, group);
            view.hide();
            return true;
        } else {
            return false;
        }
    }

    private boolean validate(PoulpeBranch branch) {
        ValidationResult result = entityValidator.validate(branch);
        if (result.hasErrors()) {
            view.validationFailure(result);
            return false;
        } else {
            return true;
        }
    }

    private boolean validate(PoulpeGroup group) {
        ValidationResult result = entityValidator.validate(group);
        if (result.hasErrors()) {
            correctMessageIfGroupAlreadyExists(result);
            view.validationFailure(result);
            return false;
        } else {
            return true;
        }
    }

    private void correctMessageIfGroupAlreadyExists(ValidationResult result) {
        Set<ValidationError> errors = result.getErrors();
        Iterator<ValidationError> iterator = errors.iterator();
        while(iterator.hasNext()) {
            ValidationError error = iterator.next();
            if(GROUP_ALREADY_EXISTS.equals(error.getErrorMessageCode())) {
                errors.remove(error);
                error = new ValidationError(error.getFieldName(), CORRESPONDING_GROUP_ALREADY_EXISTS);
                errors.add(error);
                break;
            }
        }
    }

    private PoulpeGroup createOrGetExistingGroup(PoulpeBranch branch) {
        String groupName = branch.getName() + GROUP_SUFFIX;
        PoulpeGroup group = null;
        if(branch.getGroups().size() > 0) {
            group = branch.getGroups().get(0);
        } else {
            group = getGroupMatchedByName(groupName);
            if(group == null) {
                group = createNewGroup();
            }
        }
        group.setName(groupName);
        return group;
    }

    private PoulpeGroup getGroupMatchedByName(String groupName) {
        List<PoulpeGroup> groups = groupService.getAllMatchedByName(groupName);
        for(PoulpeGroup group : groups) {
            if(groupName.equals(group.getName())) {
                return group;
            }
        }
        return null;
    }

    private PoulpeGroup createNewGroup() {
        PoulpeGroup group = new PoulpeGroup();
        group.setDescription("");
        return group;
    }

    private void setBranchPermissions(PoulpeBranch branch, PoulpeGroup group) {
        for(BranchPermission permission : BranchPermission.values()) {
            BranchAccessChanges branchAccess = new BranchAccessChanges(permission);
            branchAccess.setNewlyAddedGroups(Collections.singleton(group));
            branchService.changeGrants(branch, branchAccess);
        }
    }

    /**
     * @param entityValidator the entityValidator to set
     */
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

}
