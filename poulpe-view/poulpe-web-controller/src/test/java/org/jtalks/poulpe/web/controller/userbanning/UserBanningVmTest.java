package org.jtalks.poulpe.web.controller.userbanning;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
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
import org.jtalks.poulpe.model.entity.User;
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

    private List<User> allUsers;
    private List<User> bannedUsers;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        allUsers = ObjectsFactory.getFakeUsers(10 + RandomUtils.nextInt(20));
        bannedUsers = Lists.newArrayList();

        // assuming that 9 allUsers, starting from 1 has banned state
        for (int i = 1; i < 10; i++) {
            User user = allUsers.get(i);
            user.setBanReason("some reason");
            bannedUsers.add(user);
        }

        viewModel = new UserBanningVm(userService);

        when(userService.getAll()).thenReturn(allUsers);
        when(userService.getAllBannedUsers()).thenReturn(bannedUsers);
    }

    @Test
    public void testAddBanToSelectedUser() {
        User user = allUsers.get(0);

        viewModel.setSelectedUser(user);
        viewModel.addBanToSelectedUser();

        assertTrue(viewModel.isEditBanWindowOpened());
        assertEquals(viewModel.getSelectedUser(), user);

        vefiryNothingChanges();
    }

    @Test
    public void testCloseEditBanWindow() {
        User user = allUsers.get(0);

        viewModel.setSelectedUser(user);
        viewModel.addBanToSelectedUser();
        viewModel.closeEditBanWindow();

        assertFalse(viewModel.isEditBanWindowOpened());

        vefiryNothingChanges();
    }

    @Test
    public void testGetAvailableUsers() {
        List<User> available = viewModel.getAvailableUsers();

        assertTrue(allUsers.containsAll(available));
        assertTrue(ListUtils.intersection(available, bannedUsers).isEmpty());

        vefiryNothingChanges();
    }

    @Test
    public void testGetBannedUsers() {
        List<User> banned = viewModel.getBannedUsers();

        assertTrue(bannedUsers.containsAll(banned));
        assertTrue(banned.containsAll(bannedUsers));

        vefiryNothingChanges();
    }

    @Test
    public void testRevokeBan() throws NotFoundException {
        User user = allUsers.get(0);
        long userId = user.getId();

        when(userService.get(eq(userId))).thenReturn(user);

        viewModel.revokeBan(userId);

        verify(userService).updateUser(eq(user));
    }

    @Test
    public void testEditBan() throws NotFoundException {
        User user = allUsers.get(0);
        long userId = user.getId();

        when(userService.get(eq(userId))).thenReturn(user);

        viewModel.editBan(userId);

        assertTrue(viewModel.isEditBanWindowOpened());
        assertEquals(viewModel.getSelectedUser(), user);

        vefiryNothingChanges();
    }

    @Test
    public void testSaveBanProperties() {
        User user = allUsers.get(0);

        viewModel.setSelectedUser(user);
        viewModel.saveBanProperties();

        verify(userService).updateUser(eq(user));
    }

    private void vefiryNothingChanges() {
        verify(userService, never()).setPermanentBanStatus(anyCollectionOf(User.class), anyBoolean(), anyString());
        verify(userService, never()).setTemporaryBanStatus(anyCollectionOf(User.class), anyInt(), anyString());
        verify(userService, never()).updateUser(any(User.class));
    }
}
