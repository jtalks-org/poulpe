package org.jtalks.poulpe.web.controller.section.moderation;

import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.web.controller.DialogManager;

public class ModerationDialogPresenter {

    public static final String USER_ALREADY_MODERATOR = "moderatedialog.validation.user_already_in_list";
    private ModerationDialogView view;
    private Branch branch;
    private BranchService branchService;
    private UserService userService;
    private DialogManager dialogManager;

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
        try {
            branchService.saveBranch(branch);
        } catch (NotUniqueException e) {
            dialogManager.notify("item.already.exist");
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

}

