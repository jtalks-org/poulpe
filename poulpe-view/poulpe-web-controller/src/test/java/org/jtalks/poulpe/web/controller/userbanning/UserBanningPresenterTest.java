package org.jtalks.poulpe.web.controller.userbanning;

import java.util.List;

import org.jtalks.common.model.entity.User;

import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.userbanning.UserBanningPresenter.BanningDialog;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserBanningPresenterTest {

    @Mock
    private UserBanningView view;
    @Mock
    private DialogManager dialogManager;
    @Mock
    private UserService service;

    UserBanningPresenter presenter;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        presenter = new UserBanningPresenter();
        presenter.setUserService(service);
        presenter.setDialogManager(dialogManager);
        presenter.initView(view);
    }

    @Test
    public void banBastersPermanent() {
        List<User> fakeUsers = ObjectCreator.getFakeUsers(10);
        final String banReason = "ban this basters";
        
//        presenter.banBasters(fakeUsers, true, -1, banReason);
        BanningDialog dialog =  presenter.new BanningDialog(fakeUsers, true, -1, banReason);
        dialog.execute();
        verify(service, times(1)).setPermanentBanStatus(
                argThat(new UserListMatcher(fakeUsers)), eq(true), eq(banReason));
        verify(service, never()).setTemporaryBanStatus(
                argThat(new UserListMatcher(fakeUsers)), anyInt(), eq(banReason));
        verify(view, times(1)).clearView();
    }

    @Test
    public void banBastersTemporary() {
        List<User> fakeUsers = ObjectCreator.getFakeUsers(10);
        final String banReason = "ban this basters";
        final Integer banLength = 10;
//        presenter.banBasters(fakeUsers, false, banLength, banReason);
        BanningDialog dialog =  presenter.new BanningDialog(fakeUsers, false, banLength, banReason);
        dialog.execute();
        verify(service, times(1)).setTemporaryBanStatus(
                argThat(new UserListMatcher(fakeUsers)), eq(banLength), eq(banReason));
        verify(service, never()).setPermanentBanStatus(
                argThat(new UserListMatcher(fakeUsers)), anyBoolean(),
                eq(banReason));
        verify(view, times(1)).clearView();
    }
    
//     @Test
//     public void getDialogManager() {
//         presenter.getDialogManager();
//     }
    
    // @Test
    // public void getUserService() {
    // throw new RuntimeException("Test not implemented");
    // }
    //
//     @Test
//     public void initView() {
////         when(service.)
//         presenter.initView(view);
//         Li
//         verify(view,times(1)).updateView(users)
//     }
    //
    // @Test
    // public void setDialogManager() {
    // throw new RuntimeException("Test not implemented");
    // }
    //
    // @Test
    // public void setUserService() {
    // throw new RuntimeException("Test not implemented");
    // }
    //
     @Test
     public void updateView() {
         List<User> fakeUsers = ObjectCreator.getFakeUsers(10);
         presenter.updateView(fakeUsers);
         verify(view,times(1)).updateView(argThat(new UserListMatcher(fakeUsers)));
     }
}
