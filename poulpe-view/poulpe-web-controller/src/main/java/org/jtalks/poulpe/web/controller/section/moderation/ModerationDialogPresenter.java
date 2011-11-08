package org.jtalks.poulpe.web.controller.section.moderation;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;

public class ModerationDialogPresenter {

    private ModerationDialogView view;
    private Branch branch;
    private List<User> moderators;
    private BranchService branchService;

    public void initView(ModerationDialogView view) {
        this.view = view;
        moderators = new ArrayList<User>() {
            {
                add(new User("test", "test", "test"));
            }
        };
        updateView(moderators, new ArrayList<User>() {
            {
                add(new User("A", "A", "A"));
                add(new User("A1", "A1", "A1"));
                add(new User("A2", "A2", "A2"));
            }
        });
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
        moderators = branch.getModerators();
    }

    public void updateView(List<User> users, List<User> usersInCombo) {
        view.updateView(users, usersInCombo);
    }

    public void refreshView() {
        updateView(moderators, new ArrayList<User>() {
            {
                add(new User("A", "A", "A"));
                add(new User("A1", "A1", "A1"));
                add(new User("A2", "A2", "A2"));
            }
        });
    }

    public void onConfirm() {
        try {
            branchService.saveBranch(branch);
        } catch (NotUniqueException e) {

        }
        view.showDialog(false);
    }

    public void onReject() {
        view.showDialog(false);

    }

    public void onAdd(final User user) {
        moderators.add(user);
        updateView(moderators, new ArrayList<User>() {
            {
                add(new User("A", "A", "A"));
                add(new User("A1", "A1", "A1"));
                add(new User("A2", "A2", "A2"));
            }
        });
    }

    /**
     * Accessor to set the {@link BranchService} for this presenter Used by
     * spring DI
     * 
     * @param service
     */
    public void setBranchService(BranchService service) {
        this.branchService = service;
    }
}
