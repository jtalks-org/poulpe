package org.jtalks.poulpe.web.controller.section.moderation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.branch.BranchPresenter;

public class ModerationDialogPresenter {

    public static final String MODERATEDIALOG_VALIDATION_USER_ALREADY_IN_LIST = "moderatedialog.validation.user_already_in_list";
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
        updateView(branch.getModerators(), userService.getAll());
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void updateView(List<User> users, List<User> usersInCombo) {
        view.updateView(users, usersInCombo);
    }

    public void refreshView() {
        updateView(branch.getModerators(), userService.getAll());
    }

    public void onConfirm() {
        try {
            branchService.saveBranch(branch);
        } catch (NotUniqueException e) {
            Logger.getLogger(BranchPresenter.class.getName()).log(
                    Level.SEVERE, null, e);
            dialogManager.notify("item.already.exist");
        }
        view.showDialog(false);
    }

    public void onReject() {
        view.showDialog(false);

    }

    public void onAdd(final User user) {
        String validationMessage = validateUser(user);
        if (validationMessage == null) {
            branch.getModerators().add(user);
            updateView(branch.getModerators(), userService.getAll());
        } else {
            view.showComboboxErrorMessage(validationMessage);
        }
    }
    
    public void onDelete(final User user) {
            branch.getModerators().remove(user);
            updateView(branch.getModerators(), userService.getAll());        
    }

    public String validateUser(User user) {
        if (branch.getModerators().contains(user)) {
            return MODERATEDIALOG_VALIDATION_USER_ALREADY_IN_LIST;
        }
        return null;
    }

    /**
     * Accessor to set the {@link BranchService} for this presenter
     * 
     * @param service
     */
    public void setBranchService(BranchService service) {
        this.branchService = service;
    }

    /**
     * Accessor to set the {@link UserService} for this presenter
     * 
     * @param service
     */
    public void setUserService(UserService service) {
        this.userService = service;
    }

}
