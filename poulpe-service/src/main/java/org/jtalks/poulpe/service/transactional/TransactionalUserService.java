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

import java.util.List;

import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.pages.Pages;
import org.jtalks.poulpe.service.UserService;

/**
 * User service class, contains methods needed to manipulate with {@code User} persistent entity.
 * 
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 */
public class TransactionalUserService implements UserService {
    private static final String NO_FILTER = "";
    
    private final UserDao userDao;

    /**
     * Create an instance of user entity based service.
     * 
     * @param userDao a DAO providing persistence operations over {@link org.jtalks.poulpe.model.entity.PoulpeUser} entities
     */
    public TransactionalUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> getAll() {
        return userDao.findPoulpeUsersPaginated(NO_FILTER, Pages.NONE);
    }
    
    @Override
    public List<PoulpeUser> findUsersPaginated(String searchString, int page, int itemsPerPage) {
        return userDao.findPoulpeUsersPaginated(searchString, Pages.paginate(page, itemsPerPage));
    }

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

    @Override
    public List<PoulpeUser> getAllBannedUsers() {
        return userDao.getAllBannedUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> getNonBannedByUsername(String searchString, int limit) {
        return userDao.getNonBannedByUsername(searchString, limit);
    }


}
