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

import java.util.Collection;
import java.util.List;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.User;

/**
 * Service interface for operations with {@code User} persistent entity.
 * 
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 */
public interface UserService {

    /**
     * Sets permanent ban status for the user list.
     * 
     * @param users the users to update ban status
     * @param permanentBan the permanent ban status
     * @param banReason the ban reason description
     */
    void setPermanentBanStatus(Collection<User> users, boolean permanentBan, String banReason);

    /**
     * Sets temporary ban status for the user list.
     * 
     * @param users the users to update ban status
     * @param days the length of the temporary ban status in days
     * @param banReason the ban reason description
     */
    void setTemporaryBanStatus(Collection<User> users, int days, String banReason);

    /**
     * Gets all Users from the database
     * 
     * @return list of all users
     */
    List<User> getAll();

    /**
     * Gets Users with corresponding word in user name
     * 
     * @param word to look up
     * @return list of users with the word in the name
     */
    List<User> getUsersByUsernameWord(String word);

    /**
     * Updates the user
     * 
     * @param user entity to be updated
     */
    void updateUser(User user);

    /**
     * Retrieves user by its id
     * 
     * @param id to look up
     * @return retrieved {@link User} instance
     * @throws NotFoundException when user can't be found
     */
    User get(long id) throws NotFoundException;

    /**
     * Gets all banned users from the database
     * 
     * @return list of all users
     */
    List<User> getAllBannedUsers();

    /**
     * Gets all non banned users with username like in parameter.
     * 
     * @param word some word which must be like username
     * @param maxCount max count of returned results
     * @return list of non banned users with username like in parameter
     */
    List<User> getNonBannedByUsername(String word, int maxCount);

}
