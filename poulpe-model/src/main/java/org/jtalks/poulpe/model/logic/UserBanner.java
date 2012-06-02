package org.jtalks.poulpe.model.logic;

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.dao.GroupDao;

import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class UserBanner {
    public static final String BANNED_USERS_GROUP_NAME = "Banned Users";
    private final GroupDao groupDao;

    public UserBanner(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    /**
     * TODO: update the comment Gets a list of banned users or creates one if it wasn't found in database. Note, that
     * creating of this group is a temporal solution until we implement Permission Schemas.
     *
     * @return the group of banned users (the one where users will be added to when they are banned)
     */
    public UserList getBannedUsers() {
        List<Group> bannedUserGroups = groupDao.getMatchedByName(BANNED_USERS_GROUP_NAME);
        if (bannedUserGroups.isEmpty()) {
            return UserList.ofCommonUsers(createBannedUserGroup().getUsers());
        } else {
            return UserList.ofCommonUsers(bannedUserGroups.get(0).getUsers());
        }
    }

    public void banUsers(UserList usersToBan) {
        Group bannedUserGroup = groupDao.getMatchedByName(BANNED_USERS_GROUP_NAME).get(0);
        bannedUserGroup.getUsers().addAll(usersToBan.getUsers());
        groupDao.saveOrUpdate(bannedUserGroup);
    }

    private Group createBannedUserGroup() {
        Group bannedUsersGroup = new Group(BANNED_USERS_GROUP_NAME, "Group for banned users");
        groupDao.saveOrUpdate(bannedUsersGroup);
        return bannedUsersGroup;
    }
}
