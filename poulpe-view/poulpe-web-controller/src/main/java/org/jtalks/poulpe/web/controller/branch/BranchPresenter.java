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

import java.util.Collections;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.web.controller.section.SectionPresenter;

/**
 * This class is implementation the branch presenter in pattern
 * Model-View-Presenter
 * 
 * @author Bekrenev Dmitry
 */
public class BranchPresenter {

    public static final String GROUP_SUFFIX = " Moderators";
    private SectionPresenter sectionPresenter;
    private SectionService sectionService;
    private BranchService branchService;
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
     * When editing branch (its name), its group's name changed respectively.
     * 
     * @param branch branch to save/edit
     * @return true on success creation/modification, false on branch or group validation failure
     */
    protected boolean saveBranch(PoulpeBranch branch) {
        if (validate(branch)) {
            PoulpeSection section = branch.getPoulpeSection();
            section.addOrUpdateBranch(branch);
            Group group = addOrUpdateGroup(branch);
            sectionService.saveSection(section);
            setBranchPermissions(branch, group);
            view.hide();
            return true;
        } else {
            return false;
        }
    }
    
    private boolean validate(Entity entity) {
        ValidationResult result = entityValidator.validate(entity);
        if (result.hasErrors()) {
            view.validationFailure(result);
            return false;
        } else {
            return true;
        }
    }

    private Group addOrUpdateGroup(PoulpeBranch branch) {
        Group group;
        if(branch.getGroups().size() > 0) {
            group = branch.getGroups().get(0);
        } else {
            group = new Group();
        }
        group.setName(branch.getName() + GROUP_SUFFIX);
        if(validate(group)) {
            branch.addOrUpdateGroup(group);
        }
        return group;
    }

    private void setBranchPermissions(PoulpeBranch branch, Group group) {
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
