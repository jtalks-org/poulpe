package org.jtalks.poulpe.model.logic;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.pages.Pages;

import java.util.List;

/**
 * Class for working with users banning
 *
 * @author stanislav bashkirtsev
 * @author maxim reshetov
 */
public class UserBanner {
	public static final String BANNED_USERS_GROUP_NAME = "Banned Users";
	private final GroupDao groupDao;
	private final UserDao userDao;

	public UserBanner(GroupDao groupDao, UserDao userDao) {
		this.groupDao = groupDao;
		this.userDao = userDao;
	}

	/**
	 * Gets a {@link UserList} of banned users from banned users group.
	 *
	 * @return the List of {@link PoulpeUser} with banned users.
	 */
	public List<PoulpeUser> getAllBannedUsers() {
		List<Group> bannedUserGroups = getBannedUsersGroups();
		return userDao.getUsersInGroups(bannedUserGroups);
	}

	/**
	 * Gets List of {@PoulpeUser} unbanned users
	 *
	 * @param availableFilterText Filter (like '%%') to username
	 * @param page                Number of page
	 * @param itemsPerPage        Count items on page
	 * @return List of {@PoulpeUser}
	 *         //
	 */
	//TODO Page in param
	public List<PoulpeUser> getNonBannedUsersByUsername(String availableFilterText, int page, int itemsPerPage) {
		List<Group> bannedUserGroups = getBannedUsersGroups();
		return userDao.findUsersNotInGroups(availableFilterText, bannedUserGroups, Pages.paginate(page, itemsPerPage));
	}

	/**
	 * Adds users to banned users group.
	 *
	 * @param usersToBan {@link UserList} with users to ban
	 */
	public void banUsers(UserList usersToBan) {
		Group bannedUserGroup = getBannedUsersGroups().get(0);
		bannedUserGroup.getUsers().addAll(usersToBan.getUsers());
		groupDao.saveOrUpdate(bannedUserGroup);
	}

	/**
	 * Revokes ban from users, deleting them from banned users group.
	 *
	 * @param usersToRevoke {@link UserList} with users to revoke ban.
	 */
	public void revokeBan(UserList usersToRevoke) {
		Group bannedUserGroup = getBannedUsersGroups().get(0);
		bannedUserGroup.getUsers().removeAll(usersToRevoke.getUsers());
		groupDao.saveOrUpdate(bannedUserGroup);
	}


	/**
	 * Create group to ban
	 *
	 * @return {@Group} of ban
	 */
	private Group createBannedUserGroup() {
		Group bannedUsersGroup = new Group(BANNED_USERS_GROUP_NAME, "Banned Users");
		groupDao.saveOrUpdate(bannedUsersGroup);
		return bannedUsersGroup;
	}

	/**
	 * Search and return list of banned groups.  If groups wasn't found in database, then creates new one.Note, that
	 * creating of this group is a temporal solution until we implement Permission Schemas.
	 *
	 * @return List of banned groups
	 */
	public List<Group> getBannedUsersGroups() {
		List<Group> bannedUserGroups = groupDao.getByName(BANNED_USERS_GROUP_NAME);
		if (bannedUserGroups.isEmpty()) {
			bannedUserGroups.add(createBannedUserGroup());
		}

		return bannedUserGroups;
	}
}
