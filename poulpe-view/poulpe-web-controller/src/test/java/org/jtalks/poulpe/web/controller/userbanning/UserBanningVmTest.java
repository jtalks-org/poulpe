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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.utils.ObjectsFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * Tests for {@link UserBanningVm}.
 * 
 * @author Vyacheslav Zhivaev
 * 
 */
public class UserBanningVmTest {
    private UserBanningVm viewModel;

    @Mock
    private UserService userService;

    private List<PoulpeUser> allUsers;
    private List<PoulpeUser> bannedUsers;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        allUsers = ObjectsFactory.getFakeUsers(10 + RandomUtils.nextInt(20));
        bannedUsers = Lists.newArrayList();

        // assuming that 9 allUsers, starting from 1 has banned state
        for (int i = 1; i < 10; i++) {
            PoulpeUser user = allUsers.get(i);
            user.setBanReason("some reason");
            bannedUsers.add(user);
        }

        viewModel = new UserBanningVm(userService);

        when(userService.getAll()).thenReturn(allUsers);
        when(userService.getAllBannedUsers()).thenReturn(bannedUsers);
    }

    @Test
    public void testAddBanToUser() {
        PoulpeUser user = allUsers.get(0);

        viewModel.setAddBanFor(user);
        viewModel.addBanToUser();

        assertTrue(viewModel.isEditBanWindowOpened());
        assertEquals(viewModel.getSelectedUser(), user);

        vefiryNothingChanges();
    }

    @Test
    public void testCloseEditBanWindow() {
        PoulpeUser user = allUsers.get(0);

        viewModel.setAddBanFor(user);
        viewModel.addBanToUser();
        viewModel.closeEditBanWindow();

        assertFalse(viewModel.isEditBanWindowOpened());

        vefiryNothingChanges();
    }

    @Test
    public void testGetAvailableUsers() {
        List<PoulpeUser> available = viewModel.getAvailableUsers();

        assertTrue(allUsers.containsAll(available));
        assertTrue(ListUtils.intersection(available, bannedUsers).isEmpty());

        vefiryNothingChanges();
    }

    @Test
    public void testGetBannedUsers() {
        List<PoulpeUser> banned = viewModel.getBannedUsers();

        assertTrue(bannedUsers.containsAll(banned));
        assertTrue(banned.containsAll(bannedUsers));

        vefiryNothingChanges();
    }

    @Test
    public void testRevokeBan() throws NotFoundException {
        PoulpeUser user = allUsers.get(0);
        long userId = user.getId();

        when(userService.get(eq(userId))).thenReturn(user);

        viewModel.revokeBan(userId);

        verify(userService).updateUser(eq(user));
    }

    @Test
    public void testEditBan() throws NotFoundException {
        PoulpeUser user = allUsers.get(0);
        long userId = user.getId();

        when(userService.get(eq(userId))).thenReturn(user);

        viewModel.editBan(userId);

        assertTrue(viewModel.isEditBanWindowOpened());
        assertEquals(viewModel.getSelectedUser(), user);

        vefiryNothingChanges();
    }

    @Test
    public void testSaveBanProperties() throws NotFoundException {
        PoulpeUser user = allUsers.get(0);
        long userId = user.getId();

        when(userService.get(eq(userId))).thenReturn(user);

        viewModel.editBan(userId);
        viewModel.saveBanProperties();

        verify(userService).updateUser(eq(user));
    }

    private void vefiryNothingChanges() {
        verify(userService, never()).updateUser(any(PoulpeUser.class));
    }
}
