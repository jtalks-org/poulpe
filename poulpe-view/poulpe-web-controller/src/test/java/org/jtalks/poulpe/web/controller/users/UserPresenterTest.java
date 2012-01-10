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
package org.jtalks.poulpe.web.controller.users;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.EditListener;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link UserPresenter}
 *
 * @author Vyacheslav Zhivaev
 *
 */
public class UserPresenterTest {

    // SUT
    UserPresenter presenter;

    @Mock
    DialogManager dialogManager;
    @Mock
    WindowManager windowManager;
    @Mock
    UserService userService;

    @Mock
    UserView view;
    @Mock
    User user;

    @Mock
    EditListener<User> listener;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new UserPresenter();
        presenter.setDialogManager(dialogManager);
        presenter.setUserService(userService);
        presenter.setWindowManager(windowManager);
    }

    @Test
    public void testInitView() {
        presenter.initView(view);
    }

    @Test
    public void testInitializeForEditUserExist() throws NotFoundException {
        givenUserExist();

        presenter.initializeForEdit(view, user, listener);

        verify(view).showFirstname(any(String.class));
        verify(view).showLastname(any(String.class));
        verify(view).showEmail(any(String.class));
        verify(view).showPassword(any(String.class));
    }

    @Test
    public void testInitializeForEditUserNotExist() throws NotFoundException {
        givenUserNotExist();

        presenter.initializeForEdit(view, user, listener);

        verify(dialogManager).notify(anyString());
        verifyViewClosed();
    }

    @Test
    public void testOnUpdateAction() throws NotFoundException {
        givenUserExist();

        presenter.initializeForEdit(view, user, listener);
        presenter.onUpdateAction();

        verifyUserUpdated();
        verifyViewClosed();
        verify(listener).onUpdate(eq(user));
    }

    @Test
    public void testOnCancelEditAction() throws NotFoundException {
        givenUserExist();

        presenter.initializeForEdit(view, user, listener);
        presenter.onCancelEditAction();

        verifyViewClosed();
        verify(listener).onCloseEditorWithoutChanges();
    }

    private void verifyViewClosed() {
        verify(windowManager).closeWindow(eq(view));
    }

    private void verifyUserUpdated() {
        assertEquals(user.getFirstName(), view.getFirstname());
        assertEquals(user.getLastName(), view.getLastname());
        assertEquals(user.getEmail(), view.getEmail());
        assertEquals(user.getPassword(), view.getPassword());

        verify(userService).updateUser(eq(user));
    }

    private void givenUserExist() throws NotFoundException {
        when(userService.get(anyLong())).thenReturn(user);
    }

    private void givenUserNotExist() throws NotFoundException {
        when(userService.get(anyLong())).thenThrow(new NotFoundException());
    }

}
