package org.jtalks.poulpe.web.controller.section.moderation;

import static org.jtalks.poulpe.web.controller.section.moderation.ModerationDialogPresenter.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ModerationDialogPresenterTest {

    private ModerationDialogPresenter presenter;

    @Mock
    private ModerationDialogView view;
    @Mock
    private UserService userService;
    @Mock
    private BranchService branchService;
    @Mock
    private DialogManager dialogManager;

    private Branch branch;

    private List<User> allUsers;
    private User user1, user2;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        branch = new Branch("name", "description");

        presenter = new ModerationDialogPresenter();
        
        presenter.setBranchService(branchService);
        presenter.setUserService(userService);
        presenter.setDialogManager(dialogManager);
        presenter.setBranch(branch);
        presenter.initView(view);
        
        allUsers = ObjectCreator.getFakeUsers(5);
        when(userService.getAll()).thenReturn(allUsers);
        
        user1 = allUsers.get(1);
        user2 = allUsers.get(2);
    }

    @Test
    public void initView() {
        presenter.initView(view);
        verify(view).updateView(branch.getModeratorsList(), allUsers);
    }

    @Test
    public void onAddWithoutErrors() {
        presenter.onAdd(user1);
        assertTrue(branch.isModeratedBy(user1));
    }

    @Test
    public void onAddUserIsAlreadyModerator() {
        branch.addModerator(user1);

        presenter.onAdd(user1);

        verify(view, never()).updateView(branch.getModeratorsList(), allUsers);
        verify(view).showComboboxErrorMessage(USER_ALREADY_MODERATOR);
    }

    @Test
    public void onConfirm() throws Exception {
        presenter.onConfirm();
        verify(branchService).saveBranch(branch);
    }

    @Test
    public void onConfirmWithError() throws Exception {
        doThrow(new NotUniqueException()).when(branchService).saveBranch(branch);
        
        presenter.onConfirm();
        
        verify(branchService).saveBranch(branch);
        verify(dialogManager).notify("item.already.exist");
    }

    @Test
    public void onDeleteModeratorRemovedFromBranch() {
        branch.addModerators(user1, user2);

        presenter.onDelete(user2);

        List<User> moderators = branch.getModeratorsList();
        
        assertEquals(moderators.size(), 1);
        assertEquals(moderators.get(0), user1);
    }
    
    @Test
    public void onDeleteViewGetsUpdated() {
        branch.addModerator(user1);
        presenter.onDelete(user1);
        verify(view).updateView(branch.getModeratorsList(), allUsers);
    }

    @Test
    public void onReject() {
        presenter.onReject();
        verify(view).hideDialog();
    }

    @Test
    public void refreshView() {
        branch.addModerators(allUsers);
        presenter.refreshView();
        verify(view).updateView(branch.getModeratorsList(), allUsers);
    }

    @Test
    public void setBranch() {
        branch.addModerators(allUsers);
        Branch newBranch = new Branch("tt", "ttt");

        presenter.setBranch(newBranch);
        presenter.refreshView();

        verify(view).updateView(newBranch.getModeratorsList(), allUsers);
    }

    @Test
    public void updateView() {
        presenter.updateView(allUsers, allUsers);
        verify(view).updateView(allUsers, allUsers);
    }

    @Test
    public void validateUserWhenUserIsAlreadyModerator() {
        branch.addModerator(user1);
        UserValidator validator = presenter.validateUser(user1);
        assertEquals(validator.getError(), USER_ALREADY_MODERATOR);
    }
       
    @Test
    public void validateUserWhenUserIsNotAModerator() {
        branch.addModerator(user1);
        UserValidator validator = presenter.validateUser(user2);
        assertFalse(validator.hasError());
    }
    
}
