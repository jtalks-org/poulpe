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
import org.jtalks.poulpe.model.entity.PoulpeUser;

/**
 * This interface provides persistence operations for {@link org.jtalks.poulpe.model.entity.PoulpeUser} objects.
 * 
 * @author Vyacheslav Zhivaev
 */
public interface UserDao extends org.jtalks.common.model.dao.UserDao, ParentRepository<PoulpeUser> {

    /**
     * Get {@link org.jtalks.poulpe.model.entity.PoulpeUser} with corresponding username.
     * 
     * @param username name of requested user.
     * @return {@link org.jtalks.poulpe.model.entity.PoulpeUser} with given username.
     * @see org.jtalks.poulpe.model.entity.PoulpeUser
     */
    PoulpeUser getPoulpeUserByUsername(String username);

    /**
     * Find all users whose username contains specified string.
     * 
     * @param substring or symbol in user name of requested user.
     * @return List of users with given string in username.
     * @see org.jtalks.poulpe.model.entity.PoulpeUser
     */
    List<PoulpeUser> getPoulpeUserByUsernamePart(String substring);

    /**
     * Get {@link org.jtalks.poulpe.model.entity.PoulpeUser} with corresponding encodedUsername.
     * 
     * @param encodedUsername encoded name of requested user.
     * @return {@link org.jtalks.poulpe.model.entity.PoulpeUser} with given encodedUsername.
     * @see org.jtalks.poulpe.model.entity.PoulpeUser
     */
    PoulpeUser getPoulpeUserByEncodedUsername(String encodedUsername);

    /**
     * Get {@link List} with all Users.
     * 
     * @return {@code List<User>}
     */
    List<PoulpeUser> getAllPoulpeUsers();

    /**
     * Get {@link List} with all banned Users.
     * 
     * @return {@code List<User>}
     */
    List<PoulpeUser> getAllBannedUsers();

    /**
     * Gets all non banned users with username like in parameter.
     * 
     * @param word some word which must be like username
     * @param maxCount max count of returned results
     * @return list of non banned users with username like in parameter
     */
    List<PoulpeUser> getNonBannedByUsername(String word, int maxCount);

}
