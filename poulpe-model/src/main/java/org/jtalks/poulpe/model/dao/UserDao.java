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

import org.jtalks.common.model.dao.Crud;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.pages.Pagination;
import org.jtalks.poulpe.model.sorting.UserSearchRequest;

import java.util.List;

/**
 * This interface provides persistence operations for
 * {@link org.jtalks.poulpe.model.entity.PoulpeUser} objects.
 *
 * @author Vyacheslav Zhivaev
 * @author Alexey Grigorev
 * @author maxim reshetov
 * @author Mikhail Zaitsev
 */
public interface UserDao extends org.jtalks.common.model.dao.UserDao<PoulpeUser>, Crud<PoulpeUser> {

    /**
     * Looks for users whose nicknames matches the given string
     *
     * @param searchString string to search
     * @param pagination   setting for pagination
     * @return paginated result list
     */
    List<PoulpeUser> findPoulpeUsersPaginated(String searchString, Pagination pagination);

    /**
     * Looks for users by sorting request
     * @param searchRequest sorting request
     * @return paginated result list
     */
    List<PoulpeUser> findPoulpeUsersBySearchRequest(UserSearchRequest searchRequest);

    /**
     * Counts how many usernames matches the given string
     *
     * @param searchString for matching with usernames
     * @return amount of matches
     */
    int countUsernameMatches(String searchString);

    /**
     * Get {@link List} users which includes in groups.
     *
     * @param groups List of groups
     * @return {@code List<User>}
     */
    List<PoulpeUser> getUsersInGroups(List<Group> groups);

    /**
     * Get user by username and hashed password
     * @param username       username we're looking for
     * @param hashedPassword hash of password
     * @return {@Code List<PoulpeUser>}
     */
    List<PoulpeUser> getByUsernameAndPassword(String username, String hashedPassword);

    /**
     * Retrieves user by its email
     * @param email to look up
     * @return retrieved {@link org.jtalks.poulpe.model.entity.PoulpeUser} instance
     */
    PoulpeUser getByEmail(String email);

    /**
     * Retrieves user by uuid
     * @param uuid to look up
     * @return retrieved {@link org.jtalks.poulpe.model.entity.PoulpeUser} instance
     */
    PoulpeUser getByUUID(String uuid);

    /**
     * Gets all users which excludes in groups with username like in parameter.
     *
     * @param availableFilterText some word which must be like username
     * @param paginate            max count of returned results
     * @param groups              List of groups
     * @return list of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInGroups(String availableFilterText, List<Group> groups, Pagination paginate);

    /**
     * Gets all users which excludes in {@code listUsers} with username like in parameter.
     *
     * @param availableFilterText some word which must be like username
     * @param listUsers list of users
     * @param paginate max count of returned results
     * @return list of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInList(String availableFilterText, List<PoulpeUser> listUsers, Pagination paginate);

    /**
     * Save new user
     * @param user the user
     */
    void save(User user);

    /**
     * Get user by username and hashed password
     * @param username      username for search
     * @param passwordHash  hash string of user's password
     * @return {@link org.jtalks.poulpe.model.entity.PoulpeUser} instance or NULL if not found
     */
    PoulpeUser getByUsernameAndHashedPassword(String username, String passwordHash);
}
