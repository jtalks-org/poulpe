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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jtalks.common.model.dao.hibernate.AbstractHibernateParentRepository;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;

/**
 * Hibernate implementation of UserDao.
 * 
 * @author Vyacheslav Zhivaev
 */
public class UserHibernateDao extends AbstractHibernateParentRepository<PoulpeUser> implements UserDao {
    /**
     * Class on which hibernate mapping is set
     */
    private final static Class<PoulpeUser> type = PoulpeUser.class;

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeUser getPoulpeUserByUsername(String username) {
        return (PoulpeUser) getSession().createQuery("from " + type.getSimpleName() + " u where u.username = ?")
                .setString(0, username).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeUser getPoulpeUserByEncodedUsername(String encodedUsername) {
        return (PoulpeUser) getSession().createQuery("from " + type.getSimpleName() + " u where u.encodedUsername = ?")
                .setCacheable(true).setString(0, encodedUsername).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PoulpeUser> getAllPoulpeUsers() {
        return (List<PoulpeUser>) getSession().createQuery("from " + type.getSimpleName()).list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PoulpeUser> getPoulpeUserByUsernamePart(String substring) {
        String param = MessageFormat.format("%{0}%", substring);
        return (List<PoulpeUser>) getSession().createQuery("from " + type.getSimpleName() + " u where u.username like ?")
                .setString(0, param).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public org.jtalks.common.model.entity.User getByUsername(String username) {
        return getPoulpeUserByUsername(username);
    }

    @Override
    public List<PoulpeUser> getAllBannedUsers() {
        Criteria criteria = getSession().createCriteria(PoulpeUser.class).add(Restrictions.isNotNull("banReason"));
        return (List<PoulpeUser>) criteria.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PoulpeUser> getNonBannedByUsername(String word, int maxResults) {
        return (List<PoulpeUser>) getSession()
                .createQuery("from " + type.getSimpleName() + " u where u.banReason is null and u.username like ?")
                .setString(0, MessageFormat.format("%{0}%", word)).setMaxResults(maxResults).list();
    }

}
