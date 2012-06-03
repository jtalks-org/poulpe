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

import static org.jtalks.poulpe.web.controller.users.UsersVm.*;
import static org.mockito.Mockito.*;

import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

public class UsersVmTest {
    // sut
    UsersVm usersVm;
    
    // dependencies
    @Mock UserService userService;
    @Mock ZkHelper zkHelper;
    @Mock Window userDialog;
    @Mock Component component;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        usersVm = new UsersVm(userService);
        usersVm.setZkHelper(zkHelper);
    }

    @Test
    public void init_componentsWired() {
        usersVm.init(component, zkHelper);
        verify(zkHelper).wireComponents(component, usersVm);
    }
    
    @Test
    public void init_firtsPageShown() {
        usersVm.init(component, zkHelper);
        verifyFirstPageShown();
    }

    private void verifyFirstPageShown() {
        verifyNthPageShown(1);
    }

    private void verifyNthPageShown(int page) {
        verify(userService).allUsersPaginated(page, usersVm.getItemsPerPage());
    }
    
    @Test
    public void setActivePage_contentChanged() {
        int activePage = 2; 
        // zk passes values decremented on 1
        usersVm.setActivePage(activePage - 1);
        
        verifyNthPageShown(activePage);
    }
    
    @Test
    public void clearSearch_firtsPageShown() {
        usersVm.clearSearch();
        verifyFirstPageShown();
    }
    
    @Test
    public void getTotalSize() {
        usersVm.getTotalSize();
        verify(userService).allUsersCount();
    }
    
    @Test
    public void testSearchUser() {
        String searchString = "searchString";
        usersVm.searchUser(searchString);
        verify(userService).getUsersByUsernameWord(searchString);
    }

    @Test
    public void testEditUser() throws Exception {
        usersVm.editUser(new PoulpeUser());
        verify(zkHelper).wireToZul(EDIT_USER_URL);
    }

    @Test
    public void testUpdateUser() throws Exception {
        initEditUserDialog();
        PoulpeUser user = new PoulpeUser();

        usersVm.saveUser(user);
        
        verify(userService).updateUser(user);
        verify(userDialog).detach();
    }

    private void initEditUserDialog() {
        when(zkHelper.findComponent(EDIT_USER_DIALOG)).thenReturn(userDialog);
    }

    @Test
    public void testCancelEdit() {
        initEditUserDialog();
        usersVm.cancelEdit();
        verify(userDialog).detach();
    }

}
