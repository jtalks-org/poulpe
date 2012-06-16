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
package org.jtalks.poulpe.service.transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.logic.UserBanner;
import org.jtalks.poulpe.pages.Pages;
import org.jtalks.poulpe.service.GroupService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

/**
 * Class for testing {@code TransactionalUserService} functionality.
 *
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 * @author maxim reshetov
 */
public class TransactionalUserServiceTest {

	@Mock
	GroupService groupService;

	// sut
	private TransactionalUserService userService;

	// dependencies
	private UserDao userDao;

	final String searchString = "searchString";

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userDao = mock(UserDao.class);
		userService = new TransactionalUserService(userDao, mock(UserBanner.class));
	}

	@Test
	public void getAll() {
		String NO_FILTER = "";
		userService.getAll();
		verify(userDao).findPoulpeUsersPaginated(NO_FILTER, Pages.NONE);
	}

	@Test
	public void countUsernameMatches() {
		userService.countUsernameMatches(searchString);
		verify(userDao).countUsernameMatches(searchString);
	}

	@Test
	public void findUsersPaginated() {
		int page = 1, limit = 10;
		userService.findUsersPaginated(searchString, page, limit);
		verify(userDao).findPoulpeUsersPaginated(searchString, Pages.paginate(page, limit));
	}

	@Test
	public void testGetUsersByUsernameWord() {
		userService.withUsernamesMatching(searchString);
		verify(userDao).findPoulpeUsersPaginated(searchString, Pages.NONE);
	}

	@Test
	public void testUpdateUser() {
		PoulpeUser user = user();
		userService.updateUser(user);
		verify(userDao).update(user);
	}

	@Test
	public void testGetAllBannedUsers() {
		List<PoulpeUser> bannedUsers = userService.getAllBannedUsers();
		assertEquals(bannedUsers, new ArrayList<PoulpeUser>());
	}

	@Test
	public void testGetNonBannedByUsername() {
		List<PoulpeUser> nonBannedUsers = userService.getNonBannedUsersByUsername(searchString, Pages.paginate(0, 1000));
		assertEquals(nonBannedUsers, new ArrayList<PoulpeUser>());
	}

	private static PoulpeUser user() {
		return new PoulpeUser(RandomStringUtils.randomAlphanumeric(10), "username@mail.com", "PASSWORD", "salt");
	}

}
