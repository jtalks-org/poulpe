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

<<<<<<< HEAD
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
=======
>>>>>>> db5f24edd5f2d79509d8490c51c89017c092f692
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.pages.Pagination;

import java.util.List;

/**
 * Service interface for operations with {@code User} persistent entity.
 *
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 * @author maxim reshetov
 */
public interface UserService {

    /**
     * Gets all Users from the database
     *
     * @return list of all users
     */
    List<PoulpeUser> getAll();

    /**
     * @param searchString string for searching users, if empty - all users will be returned
     * @param page         page number for retrieving
     * @param itemsPerPage limit of items per page
     * @return users matched given search string paginated
     */
    List<PoulpeUser> findUsersPaginated(String searchString, int page, int itemsPerPage);

    /**
<<<<<<< HEAD
     * Gets all users which excludes in groups with username like in parameter.
     *
     * @param availableFilterText some word which must be like username
     * @param groups              List of groups
     * @param page         page number for retrieving
     * @param itemsPerPage limit of items per page
     * @return list of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInGroups(String availableFilterText, List<Group> groups, int page, int itemsPerPage);

    /**
    * Gets all users which excludes in groups with username like in parameter
    * @param availableFilterText some word which must be like username
    * @param groups List of groups
    * @return list of users with username like in parameter
    */
    List<PoulpeUser> findUsersNotInGroups(String availableFilterText, List<Group> groups);

    /**
     * Gets all users which excludes in {@code listUsers} with username like in parameter
     * @param availableFilterText some word which must be like username
     * @param listUsers list of users
     * @param page page number for retrieving
     * @param itemsPerPage limit of items per page
     * @return ist of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInList(String availableFilterText, List<PoulpeUser> listUsers,  int page, int itemsPerPage);

    /**
     * Gets all users which excludes in {@code listUsers} with username like in parameter
     * @param availableFilterText some word which must be like username
     * @param listUsers list of users
     * @return ist of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInList(String availableFilterText, List<PoulpeUser> listUsers);

    /**
=======
>>>>>>> db5f24edd5f2d79509d8490c51c89017c092f692
     * @param searchString string for searching users, if empty - all users will be returned
     * @return amount of users matched the given string
     */
    int countUsernameMatches(String searchString);

    /**
     * Gets Users with corresponding word in user name
     *
     * @param usernamePart to look up
     * @return list of users with the word in the name
     */
    List<PoulpeUser> withUsernamesMatching(String usernamePart);

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
     * Adds users to banned users group
     *
     * @param usersToBan users to add to banned users group
     */
    void banUsers(PoulpeUser... usersToBan);

    /**
     * Revokes ban state from users, deleting them from banned users group
     *
     * @param bannedUsersToRevoke users to remove from banned users group
     */
    void revokeBan(PoulpeUser... bannedUsersToRevoke);

    /**
     * Get firsts (count) of non banned users with filter by username
     *
     * @param availableFilterText Filter value to username (like '%availableFilterText%')
     * @param pagination          Params to limit
     * @return List of {@PoulpeUser}
     */
    List<PoulpeUser> getNonBannedUsersByUsername(String availableFilterText, Pagination pagination);

    /**
     * Check, whether user with specified name have rights to accept component of specified type.
     *
     * @param username      poulpe user, for whom access should be checked
     * @param componentType component type, that should be checked
     * @return true if user has administration rights for the component of specified type or false if the access is
     *         restricted or there is no granting access or the component does not exist
     */
    boolean accessAllowedToComponentType(String username, ComponentType componentType);
}
