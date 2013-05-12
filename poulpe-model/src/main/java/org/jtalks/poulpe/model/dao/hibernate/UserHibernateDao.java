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

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.jtalks.common.model.dao.hibernate.GenericDao;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.dao.utils.SqlLikeEscaper;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.pages.Pagination;
import org.jtalks.poulpe.model.sorting.UserSearchRequest;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Hibernate implementation of UserDao.
 *
 * @author Vyacheslav Zhivaev
 * @author Alexey Grigorev
 * @author Mikhail Zaitsev
 */
public class UserHibernateDao extends GenericDao<PoulpeUser> implements UserDao {

    private static final String FORMAT = "%{0}%";

    /**
     * @param sessionFactory The SessionFactory.
     */
    public UserHibernateDao(SessionFactory sessionFactory) {
        super(sessionFactory, PoulpeUser.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> findPoulpeUsersPaginated(String searchString, Pagination paginate) {
        searchString = SqlLikeEscaper.escapeControlCharacters(searchString);
        Query query = session().getNamedQuery("findUsersByLikeUsername");
        query.setString("username", MessageFormat.format(FORMAT, searchString));
        paginate.addPagination(query);

        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> findPoulpeUsersBySearchRequest(UserSearchRequest searchRequest) {
        String searchString = SqlLikeEscaper.escapeControlCharacters(searchRequest.getSearchString());
        Criteria criteria = session().createCriteria(PoulpeUser.class)
                .add(Restrictions.like("username", MessageFormat.format(FORMAT, searchString)))
                .addOrder(getOrder(searchRequest));
        searchRequest.getPagination().addPagination(criteria);
        return criteria.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countUsernameMatches(String searchString) {
        searchString = SqlLikeEscaper.escapeControlCharacters(searchString);
        Query query = session().getNamedQuery("countUsersByLikeUsername");
        query.setString("username", MessageFormat.format(FORMAT, searchString));

        Number result = (Number) query.uniqueResult();
        return result.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeUser getByUsername(String username) {
        Query query = session().getNamedQuery("findUsersByUsername");
        query.setString("username", username);

        return (PoulpeUser) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeUser getByEmail(String email) {
        Query query = session().getNamedQuery("findUsersByEmail");
        query.setString("email", email);

        return (PoulpeUser) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> getUsersInGroups(List<Group> groups) {
        Query query = session().getNamedQuery("findBannedUsers");

        ArrayList groupsIds = new ArrayList();
        for (Group group : groups) {
            groupsIds.add(new BigInteger(group.getId() + ""));
        }
        query.setParameterList("bannedGroups", groupsIds, StandardBasicTypes.BIG_INTEGER);
        //noinspection unchecked
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> findUsersNotInGroups(String availableFilterText,
                                                 List<Group> groups, Pagination paginate) {
        availableFilterText = SqlLikeEscaper.escapeControlCharacters(availableFilterText);
        Query query = session().getNamedQuery("findUnbannedUsersByLikeUsername");
        query.setString("username", MessageFormat.format(FORMAT, availableFilterText));

        ArrayList groupsIds = new ArrayList();
        for (Group group : groups) {
            groupsIds.add(new BigInteger(group.getId() + ""));
        }

        query.setParameterList("bannedGroups", groupsIds, StandardBasicTypes.BIG_INTEGER);
        paginate.addPagination(query);
        //noinspection unchecked
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoulpeUser> findUsersNotInList(String availableFilterText,
                                               List<PoulpeUser> listUsers, Pagination paginate) {
        availableFilterText = SqlLikeEscaper.escapeControlCharacters(availableFilterText);
        ArrayList<Long> ids = new ArrayList<Long>();
        for (PoulpeUser b : listUsers) {
            ids.add(b.getId());
        }
        Query query = null;
        if (ids.size() == 0) {
            query = session().getNamedQuery("findEnabledUsersByLikeUsername");
        } else {
            query = session().getNamedQuery("findEnabledUsersByLikeUsernameNotInList");
            query.setParameterList("listUsers", ids);
        }

        query.setString("username", MessageFormat.format(FORMAT, availableFilterText));
        paginate.addPagination(query);

        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(User user) {
        session().save(user);
        session().flush();
    }

    /**
     * Returns order of sorting by sorting request
     *
     * @param request sorting request
     * @return order
     */
    private Order getOrder(UserSearchRequest request) {
        if (request.isAscending()) {
            return Order.asc(request.getColumn()).ignoreCase();
        } else {
            return Order.desc(request.getColumn()).ignoreCase();
        }
    }


}
