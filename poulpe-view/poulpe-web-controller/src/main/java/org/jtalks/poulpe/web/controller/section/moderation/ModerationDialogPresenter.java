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
package org.jtalks.poulpe.web.controller.section.moderation;

import java.util.List;

import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.validation.EntityValidator;
import org.jtalks.poulpe.validation.ValidationResult;
import org.jtalks.poulpe.web.controller.DialogManager;

public class ModerationDialogPresenter {

    public static final String USER_ALREADY_MODERATOR = "moderatedialog.validation.user_already_in_list";
    private ModerationDialogView view;
    private Branch branch;
    private BranchService branchService;
    private UserService userService;
    private DialogManager dialogManager;
    private EntityValidator entityValidator;

    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    public void initView(ModerationDialogView view) {
        this.view = view;
        refreshView();
    }

    public void updateView(List<User> users, List<User> usersInCombo) {
        view.updateView(users, usersInCombo);
    }

    public void refreshView() {
        updateView(branch.getModeratorsList(), userService.getAll());
    }

    public void onConfirm() {
        ValidationResult result = entityValidator.validate(branch);

        if (result.hasErrors()) {
            dialogManager.notify("item.already.exist");
        } else {
            branchService.saveBranch(branch);
        }

        view.hideDialog();
    }

    public void onReject() {
        view.hideDialog();
    }

    public void onAdd(final User user) {
        UserValidator validator = validateUser(user);

        if (validator.hasError()) {
            view.showComboboxErrorMessage(validator.getError());
        } else {
            branch.addModerator(user);
            refreshView();
        }
    }

    public void onDelete(final User user) {
        branch.removeModerator(user);
        refreshView();
    }

    public UserValidator validateUser(User user) {
        UserValidator validator = new UserValidator(branch);
        validator.validate(user);
        return validator;
    }

    /**
     * Accessor to set the {@link BranchService} for this presenter
     */
    public void setBranchService(BranchService service) {
        this.branchService = service;
    }

    /**
     * Accessor to set the {@link UserService} for this presenter
     */
    public void setUserService(UserService service) {
        this.userService = service;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    
    /**
     * @param entityValidator the entityValidator to set
     */
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

}
