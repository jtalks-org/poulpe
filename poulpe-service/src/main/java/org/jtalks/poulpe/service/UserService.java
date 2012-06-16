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

import org.jtalks.common.service.exceptions.NotFoundException;
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
}
