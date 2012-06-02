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

import org.jtalks.poulpe.service.transactional.SecurityService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.jtalks.common.model.dao.UserDao;
import org.jtalks.common.model.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertSame;

/**
 * 
 * @author Oleg Kokarev
 *
 */

public class SecurityServiceTest {
    private static String username = "user";
    private static User user = new User(username, "user@mail.ru", "password", "salt");
    private UserDao userDao;
    private SecurityService securityService;

    @BeforeMethod
    public void setUp() throws Exception {
        userDao = mock(UserDao.class);
        securityService = new SecurityService(userDao);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        when(userDao.getByUsername(username)).thenReturn(user);
        assertSame(user, securityService.loadUserByUsername(username));
    }
    
    @Test(expectedExceptions = UsernameNotFoundException.class)
    public void testUserNotFound() {
        when(userDao.getByUsername(username)).thenReturn(null);
        securityService.loadUserByUsername(username);
    }
}