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
package org.jtalks.poulpe.model.dao.hibernate;

import java.text.MessageFormat;
import java.util.List;

import org.jtalks.common.model.dao.hibernate.AbstractHibernateParentRepository;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.User;

/**
 * Hibernate implementation of UserDao.
 * 
 * @author Vyacheslav Zhivaev
 */
public class UserHibernateDao extends AbstractHibernateParentRepository<User> implements UserDao {
    /**
     * Class on which hibernate mapping is set
     */
    private final static Class<User> type = User.class;

    /**
     * {@inheritDoc}
     */
    @Override
    public User getPoulpeUserByUsername(String username) {
        return (User) getSession().createQuery("from " + type.getSimpleName() + " u where u.username = ?")
                .setString(0, username).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getPoulpeUserByEncodedUsername(String encodedUsername) {
        return (User) getSession().createQuery("from " + type.getSimpleName() + " u where u.encodedUsername = ?")
                .setCacheable(true).setString(0, encodedUsername).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllPoulpeUsers() {
        return (List<User>) getSession().createQuery("from " + type.getSimpleName()).list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<User> getPoulpeUserByUsernamePart(String substring) {
        String param = MessageFormat.format("%{0}%", substring);
        return (List<User>) getSession().createQuery("from " + type.getSimpleName() + " u where u.username like ?")
                .setString(0, param).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public org.jtalks.common.model.entity.User getByUsername(String username) {
        return getPoulpeUserByUsername(username);
    }

}
