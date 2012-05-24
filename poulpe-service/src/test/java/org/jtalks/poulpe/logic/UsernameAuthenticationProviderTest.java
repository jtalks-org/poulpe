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
package org.jtalks.poulpe.logic;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import org.jtalks.common.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

/**
 * 
 * @author Oleg Kokarev
 *
 */

public class UsernameAuthenticationProviderTest {
    
    private final String correctUsername = "CorrectUsername";
    private final String wrongUsername = "WrongUsername";
    private final UserDetails savedUser = new User(correctUsername, "user@mail.ru", "savedPassword", "salt");
    
    private UsernameAuthenticationProvider usernameAuthenticationProvider;
    private UserDetailsService userDetailsService;

    @BeforeTest
    public void setUp() throws Exception {
        userDetailsService = mock(UserDetailsService.class);
        usernameAuthenticationProvider = new UsernameAuthenticationProvider(userDetailsService);
    }
    
    @Test
    public void testRetriveUser() {
        when(userDetailsService.loadUserByUsername(correctUsername)).thenReturn(savedUser);
        assertEquals(savedUser, usernameAuthenticationProvider.retrieveUser(correctUsername, null));
    }

    @Test
    public void testUserIsAuthenticated() {
        UsernamePasswordAuthenticationToken correctUser = new UsernamePasswordAuthenticationToken(correctUsername, "");
        when(usernameAuthenticationProvider.retrieveUser(correctUsername, correctUser)).thenReturn(savedUser);
        Authentication result = usernameAuthenticationProvider.authenticate(correctUser);
        assertTrue(result.isAuthenticated());
    }

    @Test(expectedExceptions = AuthenticationException.class)
    public void testUserIsNotAuthenticated() {
        UsernamePasswordAuthenticationToken wrongUser = new UsernamePasswordAuthenticationToken(wrongUsername, "");
        when(usernameAuthenticationProvider.retrieveUser(wrongUsername, wrongUser)).thenThrow(new UsernameNotFoundException("Not found"));
        usernameAuthenticationProvider.authenticate(wrongUser);
    }

}
