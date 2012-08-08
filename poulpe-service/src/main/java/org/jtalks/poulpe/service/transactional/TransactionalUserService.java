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
package org.jtalks.poulpe.service.transactional;

import com.google.common.collect.Lists;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.logic.UserBanner;
import org.jtalks.poulpe.model.logic.UserList;
import org.jtalks.poulpe.pages.Pages;
import org.jtalks.poulpe.pages.Pagination;
import org.jtalks.poulpe.service.UserService;

import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.in;

/**
 * User service class, contains methods needed to manipulate with {@code User} persistent entity.
 *
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 * @author maxim reshetov
 * @author Mikhail Zaitsev
 */
public class TransactionalUserService implements UserService {
    private static final String NO_FILTER = "";

    private final UserDao userDao;
    private final UserBanner userBanner;
    private final AclManager aclManager;
    private final ComponentDao componentDao;


    /**
     * Create an instance of user entity based service.
     *
     * @param userDao a DAO providing persistence operations over {@link org.jtalks.poulpe.model.entity.PoulpeUser}
     *                entities
     */
    public TransactionalUserService(UserDao userDao, UserBanner userBanner, AclManager aclManager, ComponentDao componentDao) {
        this.userDao = userDao;
        this.userBanner = userBanner;
        this.aclManager = aclManager;
        this.componentDao = componentDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> getAll() {
        return userDao.findPoulpeUsersPaginated(NO_FILTER, Pages.NONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> findUsersPaginated(String searchString, int page, int itemsPerPage) {
        return userDao.findPoulpeUsersPaginated(searchString, Pages.paginate(page, itemsPerPage));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countUsernameMatches(String searchString) {
        return userDao.countUsernameMatches(searchString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> withUsernamesMatching(String searchString) {
        return userDao.findPoulpeUsersPaginated(searchString, Pages.NONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> findUsersNotInGroups(String availableFilterText, List<Group> groups, int page, int itemsPerPage){
    	return userDao.findUsersNotInGroups(availableFilterText, groups,  Pages.paginate(page, itemsPerPage));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public List<PoulpeUser> findUsersNotInGroups(String availableFilterText, List<Group> groups) {
		return userDao.findUsersNotInGroups(availableFilterText, groups,  Pages.NONE);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> findUsersNotInList(String availableFilterText, List<PoulpeUser> listUsers,
                                               int page, int itemsPerPage) {
        return userDao.findUsersNotInList(availableFilterText,listUsers,Pages.paginate(page, itemsPerPage));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> findUsersNotInList(String availableFilterText, List<PoulpeUser> listUsers) {
        return userDao.findUsersNotInList(availableFilterText,listUsers,Pages.NONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(PoulpeUser user) {
        userDao.update(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeUser get(long id) {
        return userDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeUser getByEmail(String email) throws NotFoundException {
        PoulpeUser user = userDao.getByEmail(email);
        if (user == null) {
            throw new NotFoundException("User with " + email + " was not found");
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmailAlreadyUsed(String email) {
        try {
            PoulpeUser user = getByEmail(email);
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> getAllBannedUsers() {
        return userBanner.getAllBannedUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void banUsers(PoulpeUser... usersToBan) {
        userBanner.banUsers(new UserList(usersToBan));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revokeBan(PoulpeUser... bannedUsersToRevoke) {
        userBanner.revokeBan(new UserList(bannedUsersToRevoke));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> getNonBannedUsersByUsername(String availableFilterText, Pagination pagination) {
        return userBanner.getNonBannedUsersByUsername(availableFilterText, pagination);
    }

    @Override
    public boolean accessAllowedToComponentType(String username, ComponentType componentType) {
        PoulpeUser user = userDao.getByUsername(username);
        Component component = componentDao.getByType(componentType);
        if (component == null) {
            return false;
        }
        List<GroupAce> permissions = aclManager.getGroupPermissionsOn(component);
        boolean granting = false;
        for (GroupAce permission : permissions) {
            if (user.isInGroupWithId(permission.getGroupId())) {
                if (!permission.isGranting()) {
                    return false;
                }
                granting = true;
            }
        }
        return granting;
    }

	
}
