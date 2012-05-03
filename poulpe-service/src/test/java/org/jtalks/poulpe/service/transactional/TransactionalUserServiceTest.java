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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.User;
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

    private UserDao userDao;
    private UserService userService;

    private static final String EMAIL = "username@mail.com";
    private static final String PASSWORD = "password";
    private static final String BAN_REASON = "ban reason";
    private static Collection<User> users;

    @BeforeMethod
    public void setUp() {
        userDao = mock(UserDao.class);

        userService = new TransactionalUserService(userDao);

        users = new ArrayList<User>();
        users.add(getUser("tony"));
        users.add(getUser("antony"));
        users.add(getUser("jack"));
    }

    @Test
    public void testGetAll() {
        userService.getAll();

        verify(userDao).getAllPoulpeUsers();
    }

    @Test
    public void testGetUsersByUsernameWord() {
        String word = "word";

        userService.getUsersByUsernameWord(word);

        verify(userDao).getPoulpeUserByUsernamePart(word);
    }

    @Test
    public void testUpdateUser() {
        User user = new User("username", "email", "password", "salt");
        
        userService.updateUser(user);
        
        verify(userDao).update(user);
    }

    /*@Test
    public void testUpdateLastLoginTime() {
        User user = mock(User.class);
        
        userService.updateLastLoginTime(user);
        
        verify(user).updateLastLoginTime();
        verify(userDao).update(user);
    }*/

    @Test
    public void testGetAllBannedUsers() {
        userService.getAllBannedUsers();
        verify(userDao).getAllBannedUsers();
    }

    @Test
    public void testGetNonBannedByUsername() {
        String usernameWord = RandomStringUtils.randomAlphanumeric(15);
        int maxCount = RandomUtils.nextInt() + 1000;

        userService.getNonBannedByUsername(usernameWord, maxCount);
        verify(userDao).getNonBannedByUsername(eq(usernameWord), eq(maxCount));
    }

    /**
     * Creates and return the {@link User} entity with default username, email
     * and password, etc.
     * 
     * @param username username
     * @return the user entity
     */
    private User getUser(String username) {
        return new User(username, EMAIL, PASSWORD, "salt");
    }

}
