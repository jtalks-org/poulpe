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

import org.jtalks.common.model.dao.ParentRepository;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.pages.Pagination;

import java.util.List;

/**
 * This interface provides persistence operations for
 * {@link org.jtalks.poulpe.model.entity.PoulpeUser} objects.
 *
 * @author Vyacheslav Zhivaev
 * @author Alexey Grigorev
 */
public interface UserDao extends org.jtalks.common.model.dao.UserDao<PoulpeUser>, ParentRepository<PoulpeUser> {

	/**
	 * Looks for users whose nicknames matches the given string
	 *
	 * @param searchString string to search
	 * @param pagination   setting for pagination
	 * @return paginated result list
	 */
	List<PoulpeUser> findPoulpeUsersPaginated(String searchString, Pagination pagination);

	/**
	 * Counts how many usernames matches the given string
	 *
	 * @param searchString for matching with usernames
	 * @return amount of matches
	 */
	int countUsernameMatches(String searchString);

	/**
	 * Get {@link List} with all banned Users.
	 *
	 * @return {@code List<User>}
	 */
	List<PoulpeUser> getAllBannedUsers();

	/**
	 * Gets all non banned users with username like in parameter.
	 *
	 * @param word     some word which must be like username
	 * @param maxCount max count of returned results
	 * @param groups   List of banned groups
	 * @return list of non banned users with username like in parameter
	 */
	List<PoulpeUser> getNonBannedByUsername(String word, List<Group> groups, int maxCount);

}
