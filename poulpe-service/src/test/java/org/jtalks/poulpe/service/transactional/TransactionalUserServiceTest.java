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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.*;

import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.pages.Pages;
import org.jtalks.poulpe.pages.Pagination;
import org.jtalks.poulpe.service.UserService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Class for testing {@code TransactionalUserService} functionality.
 * 
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 */
public class TransactionalUserServiceTest {

    // sut
    private UserService userService;

    // dependencies
    private UserDao userDao;

    @BeforeMethod
    public void setUp() {
        userDao = mock(UserDao.class);
        userService = new TransactionalUserService(userDao);
    }

    @Test
    public void testGetAll() {
        userService.getAll();
        verify(userDao).getAllPoulpeUsers();
    }

    @Test
    public void allUsersCount() {
        userService.allUsersCount();
        verify(userDao).getAllUsersCount();
    }

    @Test
    public void testAllUsersPaginated() {
        int page = 1, limit = 10;
        userService.allUsersPaginated(page, limit);
        verify(userDao).getAllPoulpeUsersPaginated(paginationWith(page, limit));
    }

    @Test
    public void testGetUsersByUsernameWord() {
        String word = "word";
        userService.getUsersByUsernameWord(word);
        verify(userDao).getPoulpeUserByUsernamePart(word);
    }

    @Test
    public void testUpdateUser() {
        PoulpeUser user = user();
        userService.updateUser(user);
        verify(userDao).update(user);
    }

    @Test
    public void testGetAllBannedUsers() {
        userService.getAllBannedUsers();
        verify(userDao).getAllBannedUsers();
    }

    @Test
    public void testGetNonBannedByUsername() {
        String usernameWord = "word";
        int maxCount = 1000;

        userService.getNonBannedByUsername(usernameWord, maxCount);
        verify(userDao).getNonBannedByUsername(usernameWord, maxCount);
    }

    private static PoulpeUser user() {
        return new PoulpeUser(RandomStringUtils.randomAlphanumeric(10), "username@mail.com", "PASSWORD", "salt");
    }

    public static Pagination paginationWith(int page, int limit) {
        final Pagination expected = Pages.paginate(page, limit);

        return argThat(new BaseMatcher<Pagination>() {
            @Override
            public boolean matches(Object o) {
                if (o instanceof Pagination) {
                    Pagination second = (Pagination) o;
                    return expected.getFrom() == second.getFrom() && expected.getCount() == second.getCount();
                }
                return false;
            }

            @Override
            public void describeTo(Description d) {
            }
        });
    }

}
