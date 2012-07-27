package org.jtalks.poulpe.web.controller.userbanning;

import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.logic.UserList;
import org.jtalks.poulpe.pages.Pages;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
		sut = new UserBanningVm(userService);
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
		sut.getAvailableUsers();
		verify(userService, times(1)).getNonBannedUsersByUsername("", Pages.paginate(0, 10));
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
	public void testAddUserToBannedGroup() throws Exception {
		sut.setAddBanFor(user);
		sut.banUser();
		verify(userService, times(1)).banUsers(user);
		assertEquals(sut.getAddBanFor(), null);
	}

	@Test
	public void testRevokeBan() throws Exception {
		sut.setSelectedUser(user);
		sut.revokeBan();
		verify(userService, times(1)).revokeBan(user);
		assertEquals(sut.getSelectedUser(), null);
	}
}
