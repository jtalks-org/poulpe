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
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.jtalks.common.model.dao.hibernate.AbstractHibernateParentRepository;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.pages.Pages;
import org.jtalks.poulpe.pages.Pagination;

/**
 * Hibernate implementation of UserDao.
 * 
 * @author Vyacheslav Zhivaev
 */
public class UserHibernateDao extends AbstractHibernateParentRepository<PoulpeUser> implements UserDao {

    private static final String TYPE_NAME = PoulpeUser.class.getSimpleName();

    /** {@inheritDoc} */
    @Override
    public PoulpeUser getPoulpeUserByUsername(String username) {
        Query query = getSession().createQuery("from " + TYPE_NAME + " u where u.username = ?");
        query.setString(0, username);
        
        return (PoulpeUser) query.uniqueResult();
    }

    /** {@inheritDoc} */
    @Override
    public PoulpeUser getPoulpeUserByEncodedUsername(String encodedUsername) {
        Query query = getSession().createQuery("from " + TYPE_NAME + " u where u.encodedUsername = ?");
        query.setString(0, encodedUsername);
        query.setCacheable(true);

        return (PoulpeUser) query.uniqueResult();
    }

    /** {@inheritDoc} */
    @Override
    public List<PoulpeUser> getAllPoulpeUsers() {
        return getAllPoulpeUsersPaginated(Pages.NONE);
    }

    /** {@inheritDoc} */
    @Override
    public List<PoulpeUser> getAllPoulpeUsersPaginated(Pagination pagination) {
        Query query = getSession().createQuery("from " + TYPE_NAME);
        pagination.addPagination(query);

        @SuppressWarnings("unchecked")
        List<PoulpeUser> result = query.list();
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public int getAllUsersCount() {
        Query query = getSession().createQuery("select count(*) from " + TYPE_NAME);
        Number result = (Number) query.uniqueResult();
        return result.intValue();
    }

    /** {@inheritDoc} */
    @Override
    public List<PoulpeUser> getPoulpeUserByUsernamePart(String substring) {
        String param = MessageFormat.format("%{0}%", substring);
        Query query = getSession().createQuery("from " + TYPE_NAME + " u where u.username like ?");
        query.setString(0, param);

        @SuppressWarnings("unchecked")
        List<PoulpeUser> result = query.list();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public PoulpeUser getByUsername(String username) {
        return getPoulpeUserByUsername(username);
    }

    /** {@inheritDoc} */
    @Override
    public List<PoulpeUser> getAllBannedUsers() {
        Criteria criteria = getSession().createCriteria(PoulpeUser.class);
        criteria.add(Restrictions.isNotNull("banReason"));

        @SuppressWarnings("unchecked")
        List<PoulpeUser> result = criteria.list();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<PoulpeUser> getNonBannedByUsername(String word, int maxResults) {
        String queryString = "from " + TYPE_NAME + " u where u.banReason is null and u.username like ?";
        Query query = getSession().createQuery(queryString);
        query.setString(0, MessageFormat.format("%{0}%", word)).setMaxResults(maxResults);

        @SuppressWarnings("unchecked")
        List<PoulpeUser> result = query.list();
        return result;
    }


}
