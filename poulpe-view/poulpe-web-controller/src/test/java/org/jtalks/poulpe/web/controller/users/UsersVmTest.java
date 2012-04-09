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

import static org.jtalks.poulpe.web.controller.users.UsersVm.EDIT_USER_DIALOG;
import static org.jtalks.poulpe.web.controller.users.UsersVm.EDIT_USER_URL;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.Window;

public class UsersVmTest {
    private static final String SEARCH_STRING = "searchString";
    UsersVm vm;
    @Mock
    UserService service;
    @Mock
    ZkHelper zkHelper;
    @Mock 
    EntityValidator entityValidator;
    @Mock
    Window userDialog;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vm = new UsersVm(service, entityValidator);
        vm.setZkHelper(zkHelper);
    }

    @Test
    public void testSearchUser() {
        vm.setSearchString(SEARCH_STRING);
        
        vm.searchUser();
        
        verify(service).getUsersByUsernameWord(SEARCH_STRING);
    }

    @Test
    public void testEditUser() throws Exception {
        vm.editUser(new User());
        
        verify(zkHelper).wireToZul(EDIT_USER_URL);
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(zkHelper.findComponent(EDIT_USER_DIALOG)).thenReturn(userDialog);
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(ValidationResult.EMPTY);
        User user = new User();

        vm.saveUser(user);

        verify(service).updateUser(user);
        verify(userDialog).detach();
    }

    @Test
    public void testCancelEdit() {
        when(zkHelper.findComponent(EDIT_USER_DIALOG)).thenReturn(userDialog);
        
        vm.cancelEdit();
        
        verify(userDialog).detach();
    }
}
