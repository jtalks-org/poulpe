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

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.pages.Pagination;
import org.jtalks.poulpe.model.sorting.UserSearchRequest;
import org.jtalks.poulpe.service.exceptions.UserExistException;
import org.jtalks.poulpe.service.exceptions.ValidationException;

import java.util.List;

/**
 * Service interface for operations with {@code User} persistent entity.
 *
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 * @author maxim reshetov
 * @author Mikhail Zaitsev
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
     * @param searchRequest sorting request
     * @return users paginated
     */
    List<PoulpeUser> findUsersBySearchRequest(UserSearchRequest searchRequest);

    /**
     * Gets all users which excludes in groups with username like in parameter.
     *
     * @param availableFilterText some word which must be like username
     * @param groups              List of groups
     * @param page                page number for retrieving
     * @param itemsPerPage        limit of items per page
     * @return list of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInGroups(String availableFilterText, List<Group> groups, int page, int itemsPerPage);

    /**
     * Gets all users which excludes in groups with username like in parameter
     *
     * @param availableFilterText some word which must be like username
     * @param groups              List of groups
     * @return list of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInGroups(String availableFilterText, List<Group> groups);

    /**
     * Gets all users which excludes in {@code listUsers} with username like in parameter
     *
     * @param availableFilterText some word which must be like username
     * @param listUsers           list of users
     * @param page                page number for retrieving
     * @param itemsPerPage        limit of items per page
     * @return ist of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInList(String availableFilterText, List<PoulpeUser> listUsers,
                                        int page, int itemsPerPage);

    /**
     * Gets all users which excludes in {@code listUsers} with username like in parameter
     *
     * @param availableFilterText some word which must be like username
     * @param listUsers           list of users
     * @return ist of users with username like in parameter
     */
    List<PoulpeUser> findUsersNotInList(String availableFilterText, List<PoulpeUser> listUsers);

    /**
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
     * Retrieves user by its email
     *
     * @param email to look up
     * @return retrieved {@link org.jtalks.poulpe.model.entity.PoulpeUser} instance or null, if there is no user with
     *         such email
     */
    PoulpeUser getByEmail(String email);

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
     * Get firsts (count) of non banned users with filter by username. Note, that this method was named loadXxx()
     * because we have configured read-only transactions for methods started with getXxx(). This method name is
     * temporal because right now we create Banned Users group if it's not created which violates read-only nature of
     * this transaction.
     *
     * @param availableFilterText Filter value to username (like '%availableFilterText%')
     * @param pagination          Params to limit
     * @return List of {@link PoulpeUser}
     */
    List<PoulpeUser> loadNonBannedUsersByUsername(String availableFilterText, Pagination pagination);

    /**
     * Check, whether user with specified name have rights to accept component of specified type.
     *
     * @param username      poulpe user, for whom access should be checked
     * @param componentType component type, that should be checked
     * @return true if user has administration rights for the component of specified type or false if the access is
     *         restricted or there is no granting access or the component does not exist
     */
    boolean accessAllowedToComponentType(String username, ComponentType componentType);

    /**
     * Update users at group. For proper operation of the replication
     * @param users users for update
     * @param group group with users
     */
    void updateUsersAtGroup(List<User> users, Group group);

    /**
     * Authenticates user by username and password hash.
     * Since version 2.1 we introduced case insensitive username verification. But to support previous installation
     * we have to handle this in smarter way:
     * <ul>
     *     <li>
     *         If there are several users exist with the same provided {@code username} differ only by letter's case
     *         we do case sensitive username verification
     *     </li>
     *     <li>
     *         If there is only one user with requested {@code username} we do case insensitive username verification
     *     </li>
     * <ul/>
     * 
     * @param username the username
     * @param password the hashed password
     * @return the {@code PoulpeUser} instance
     * @throws NotFoundException if user not found or password not match
     */
    PoulpeUser authenticate(String username, String password) throws NotFoundException;

    /**
     * Activate user by username
     * @param username username
     * @throws NotFoundException if user with provided username does not exist
     * @throws ValidationException if not valid username is provided
     */
    void activate(String username) throws NotFoundException, ValidationException;

    /**
     * Registers a new user
     *
     * @param user the user
     * @throws ValidationException {@link ValidationException} if there are validation errors
     */
    void registration(PoulpeUser user) throws ValidationException;

    /**
     * Makes dry registration (without saving user data), but returns validation errors, if any.
     *
     * @param user the user
     * @throws ValidationException {@link ValidationException} if there are validation errors
     */
    void dryRunRegistration(PoulpeUser user) throws ValidationException;
}
