package org.jtalks.poulpe.web.controller.section.moderation;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.utils.BranchMatcher;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.jtalks.poulpe.web.controller.utils.UserListMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ModerationDialogPresenterTest {

    @Mock
    private ModerationDialogView view;
    private ModerationDialogPresenter presenter;
    @Mock
    private UserService userService;
    @Mock
    private BranchService branchService;

    @Mock
    private DialogManager dialogManager;

    private Branch branch;

    private List<User> fakeUsers;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        branch = ObjectCreator.getFakeBranch("test", "test");
        branch.setModerators(new ArrayList<User>());
        presenter = new ModerationDialogPresenter();
        presenter.setBranchService(branchService);
        presenter.setUserService(userService);
        presenter.setDialogManager(dialogManager);
        presenter.setBranch(branch);
        presenter.initView(view);
        fakeUsers = ObjectCreator.getFakeUsers(5);

    }

    @Test
    public void initView() {
        when(userService.getAll()).thenReturn(fakeUsers);
        presenter.initView(view);
        verify(view, times(1)).updateView(
                argThat(new UserListMatcher(branch.getModerators())),
                argThat(new UserListMatcher(fakeUsers)));
    }

    @Test
    public void onAddWithoutErrors() {
        when(userService.getAll()).thenReturn(fakeUsers);
        presenter.onAdd(fakeUsers.get(2));

        // a list of branch's moderators
        List<User> moderators = new ArrayList<User>() {
            {
                add(fakeUsers.get(2));
            }
        };
        verify(view, times(1)).updateView(
                argThat(new UserListMatcher(moderators)),
                argThat(new UserListMatcher(fakeUsers)));

    }

    @Test
    public void onAddWithError() {
        when(userService.getAll()).thenReturn(fakeUsers);
        branch.setModerators(new ArrayList<User>() {
            {
                add(fakeUsers.get(2));
            }
        });
        presenter.onAdd(fakeUsers.get(2));
        // a list of branch's moderators
        List<User> moderators = new ArrayList<User>() {
            {
                add(fakeUsers.get(2));
            }
        };
        verify(view, never()).updateView(
                argThat(new UserListMatcher(moderators)),
                argThat(new UserListMatcher(fakeUsers)));
        verify(view, times(1))
                .showComboboxErrorMessage(
                        ModerationDialogPresenter.MODERATEDIALOG_VALIDATION_USER_ALREADY_IN_LIST);

    }

    @Test
    public void onConfirm() {
        presenter.onConfirm();
        try {
            verify(branchService, times(1)).saveBranch(
                    argThat(new BranchMatcher(branch)));
        } catch (NotUniqueException e) {
            fail("Saved branch must be unique");
        }
    }

    @Test
    public void onConfirmWithError() {

        try {
            doThrow(new NotUniqueException()).when(branchService).saveBranch(
                    branch);
            presenter.onConfirm();
            verify(branchService, times(1)).saveBranch(
                    argThat(new BranchMatcher(branch)));

            verify(dialogManager, times(1)).notify(eq("item.already.exist"));
        } catch (NotUniqueException e) {
            fail("unreachable");
        }

    }

    @Test
    public void onDelete() {
        when(userService.getAll()).thenReturn(fakeUsers);
        branch.setModerators(new ArrayList<User>() {
            {
                add(fakeUsers.get(1));
                add(fakeUsers.get(2));
            }
        });
        presenter.onDelete(fakeUsers.get(2));
        assertEquals(branch.getModerators().size(), 1);
        assertEquals(branch.getModerators().get(0), fakeUsers.get(1));
        verify(view, times(1)).updateView(
                argThat(new UserListMatcher(branch.getModerators())),
                argThat(new UserListMatcher(fakeUsers)));
    }

    @Test
    public void onReject() {
        presenter.onReject();
        verify(view, times(1)).showDialog(eq(false));
    }

    @Test
     public void refreshView() {
        when(userService.getAll()).thenReturn(fakeUsers);
         branch.setModerators(fakeUsers);
     presenter.refreshView();
     verify(view,times(1)).updateView(argThat(new UserListMatcher(branch.getModerators())), argThat(new UserListMatcher(fakeUsers)));
     }
    
     @Test
     public void setBranch() {
         when(userService.getAll()).thenReturn(fakeUsers);
         branch.setModerators(fakeUsers);
         Branch newBranch = ObjectCreator.getFakeBranch("tt", "ttt");
         newBranch.setModerators(new ArrayList<User>());
         presenter.setBranch(newBranch);
         presenter.refreshView();
         verify(view,times(1)).updateView(argThat(new UserListMatcher(newBranch.getModerators())), argThat(new UserListMatcher(fakeUsers)));
     }
    
     @Test
     public void updateView() {
         presenter.updateView(fakeUsers, fakeUsers);
         verify(view,times(1)).updateView(argThat(new UserListMatcher(fakeUsers)), argThat(new UserListMatcher(fakeUsers)));
     }
    
     @Test
     public void validateUser() {
         branch.setModerators(fakeUsers);
         assertEquals(presenter.validateUser(fakeUsers.get(1)), ModerationDialogPresenter.MODERATEDIALOG_VALIDATION_USER_ALREADY_IN_LIST);
         assertEquals(presenter.validateUser(new User("AA", "CC", "DD")), null);
     }
}
