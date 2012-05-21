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
package org.jtalks.poulpe.service;

import java.util.List;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeUser;

/**
 * Service interface for operations with {@code User} persistent entity.
 * 
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 */
public interface UserService {

    /**
     * Gets all Users from the database
     * 
     * @return list of all users
     */
    List<PoulpeUser> getAll();

    /**
     * Gets Users with corresponding word in user name
     * 
     * @param word to look up
     * @return list of users with the word in the name
     */
    List<PoulpeUser> getUsersByUsernameWord(String word);

    /**
     * Updates the user
     * 
     * @param user entity to be updated
     */
    void updateUser(PoulpeUser user);

    /**
     * Retrieves user by its id
     * 
     * @param id to look up
     * @return retrieved {@link org.jtalks.poulpe.model.entity.PoulpeUser} instance
     * @throws NotFoundException when user can't be found
     */
    PoulpeUser get(long id) throws NotFoundException;

    /**
     * Gets all banned users from the database
     * 
     * @return list of all users
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
