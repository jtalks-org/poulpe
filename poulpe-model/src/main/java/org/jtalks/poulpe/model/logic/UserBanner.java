package org.jtalks.poulpe.model.logic;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.dao.GroupDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for working with users banning
 *
 * @author stanislav bashkirtsev
 */
public class UserBanner {
	public static final String BANNED_USERS_GROUP_NAME = "Banned Users";
	private final GroupDao groupDao;

	public UserBanner(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	/**
	 * Gets a {@link UserList} of banned users from banned users group. If group wasn't found in database, then
	 * creates new one.Note, that
	 * creating of this group is a temporal solution until we implement Permission Schemas.
	 *
	 * @return the {@link UserList} with banned users.
	 */
	public UserList getBannedUsers() {
		List<Group> bannedUserGroups = groupDao.getMatchedByName(BANNED_USERS_GROUP_NAME);
		if (bannedUserGroups.isEmpty()) {
			return UserList.ofCommonUsers(createBannedUserGroup().getUsers());
		}
		else {
			return UserList.ofCommonUsers(bannedUserGroups.get(0).getUsers());
		}
	}

	/**
	 * Adds users to banned users group.
	 *
	 * @param usersToBan {@link UserList} with users to ban
	 */
	public void banUsers(UserList usersToBan) {
		Group bannedUserGroup = groupDao.getMatchedByName(BANNED_USERS_GROUP_NAME).get(0);
		bannedUserGroup.getUsers().addAll(usersToBan.getUsers());
		groupDao.saveOrUpdate(bannedUserGroup);
	}

	/**
	 * Revokes ban from users, deleting them from banned users group.
	 *
	 * @param usersToRevoke {@link UserList} with users to revoke ban.
	 */
	public void revokeBan(UserList usersToRevoke) {
		Group bannedUserGroup = groupDao.getMatchedByName(BANNED_USERS_GROUP_NAME).get(0);
		bannedUserGroup.getUsers().removeAll(usersToRevoke.getUsers());
		groupDao.saveOrUpdate(bannedUserGroup);
	}

	private Group createBannedUserGroup() {
		Group bannedUsersGroup = new Group(BANNED_USERS_GROUP_NAME, "Group for banned users");
		groupDao.saveOrUpdate(bannedUsersGroup);
		return bannedUsersGroup;
	}

	/**
	 * Search and return list of banned groups
	 *
	 * @return List of banned groups
	 */
	public List<Group> getBannedUsersGroups() {
		Group bannedUserGroup = groupDao.getMatchedByName(BANNED_USERS_GROUP_NAME).get(0);
		ArrayList<Group> bannedGroups = new ArrayList();
		bannedGroups.add(bannedUserGroup);

		return bannedGroups;
	}
}
