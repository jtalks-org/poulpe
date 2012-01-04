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
package org.jtalks.poulpe.web.controller.userbanning;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.userbanning.UserBanningPresenter.BanningDialog;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.jtalks.poulpe.web.controller.utils.UserListMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
        
        presenter.banBasters(fakeUsers, true, -1, banReason);
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
        presenter.banBasters(fakeUsers, false, banLength, banReason);
        BanningDialog dialog =  presenter.new BanningDialog(fakeUsers, false, banLength, banReason);
        dialog.execute();
        verify(service, times(1)).setTemporaryBanStatus(
                argThat(new UserListMatcher(fakeUsers)), eq(banLength), eq(banReason));
        verify(service, never()).setPermanentBanStatus(
                argThat(new UserListMatcher(fakeUsers)), anyBoolean(),
                eq(banReason));
        verify(view, times(1)).clearView();
    }
    
     @Test
     public void updateView() {
         List<User> fakeUsers = ObjectCreator.getFakeUsers(10);
         presenter.updateView(fakeUsers);
         verify(view,times(1)).updateView(argThat(new UserListMatcher(fakeUsers)));
     }
}
