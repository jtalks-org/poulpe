package org.jtalks.poulpe.web.controller.userbanning;

import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.logic.UserList;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * @author afanasev alexandr
 */
public class UserBanningVmTest {
    GroupService groupService;
    UserService userService;
    UserBanningVm sut;
    List<PoulpeUser> availableUsers;
    List<PoulpeUser> bannedUsers;
    UserList banned;
    PoulpeUser user;

    @BeforeMethod
    public void setUp() throws Exception {
        groupService = mock(GroupService.class);
        userService = mock(UserService.class);
        banned = mock(UserList.class);
        sut = new UserBanningVm(userService, groupService);
        availableUsers = Arrays.asList(new PoulpeUser("a", "a", "a", "a"),
                new PoulpeUser("b", "b", "b", "b"),
                new PoulpeUser("c", "c", "c", "c"),
                new PoulpeUser("d", "d", "d", "d"));
        bannedUsers = Arrays.asList(new PoulpeUser("c", "c", "c", "c"),
                new PoulpeUser("b", "b", "b", "b"));
        user = new PoulpeUser("a", "d", "r", "y");
    }

    @Test
    public void testGetAvailableUsers() throws Exception {
//        doReturn(availableUsers).when(userService).getAll();
//        doReturn(banned).when(groupService).getBannedUsers();
//        doReturn(bannedUsers).when(banned).getUsers();
//        availableUsers.removeAll(bannedUsers);
//        List<PoulpeUser> users=sut.getAvailableUsers();
//        assertEquals(availableUsers,users);
    }

    @Test
    public void testGetSelectedUser() throws Exception {
        sut.setSelectedUser(user);
        assertEquals(sut.getSelectedUser(), user);
    }

    @Test
    public void testGetAddBanFor() throws Exception {
        sut.setAddBanFor(user);
        assertEquals(sut.getAddBanFor(), user);
    }


    @Test
    public void testSetAvailableFilter() throws Exception {

    }

    @Test
    public void testAddUserToBannedGroup() throws Exception {
        sut.setAddBanFor(user);
        sut.addUserToBannedGroup();
        verify(groupService, times(1)).banUsers(user);
        assertEquals(sut.getAddBanFor(), null);
    }

    @Test
    public void testRevokeBan() throws Exception {
        sut.setSelectedUser(user);
        sut.revokeBan();
        verify(groupService, times(1)).revokeBan(user);
        assertEquals(sut.getSelectedUser(), null);
    }
}
