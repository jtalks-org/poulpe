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
package org.jtalks.poulpe.model.dao;

import java.util.List;

import org.jtalks.common.model.dao.ParentRepository;
import org.jtalks.poulpe.model.entity.User;

/**
 * This interface provides persistence operations for {@link User} objects.
 * 
 * @author Vyacheslav Zhivaev
 */
public interface UserDao extends org.jtalks.common.model.dao.UserDao, ParentRepository<User> {

    /**
     * Get {@link User} with corresponding username.
     * 
     * @param username name of requested user.
     * @return {@link User} with given username.
     * @see User
     */
    User getPoulpeUserByUsername(String username);

    /**
     * Find all users whose username contains specified string.
     * 
     * @param substring or symbol in user name of requested user.
     * @return List of users with given string in username.
     * @see User
     */
    List<User> getPoulpeUserByUsernamePart(String substring);

    /**
     * Get {@link User} with corresponding encodedUsername.
     * 
     * @param encodedUsername encoded name of requested user.
     * @return {@link User} with given encodedUsername.
     * @see User
     */
    User getPoulpeUserByEncodedUsername(String encodedUsername);

    /**
     * Get {@link List} with all Users.
     * 
     * @return {@code List<User>}
     */
    List<User> getAllPoulpeUsers();

}
