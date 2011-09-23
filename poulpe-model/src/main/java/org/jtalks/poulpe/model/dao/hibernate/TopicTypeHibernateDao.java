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

import java.util.List;

import org.hibernate.Query;
import org.jtalks.common.model.dao.hibernate.DefaultParentRepository;
import org.jtalks.poulpe.model.entity.TopicType;

/**
 * @author Vladimir Bukhtoyarov
 */
public class TopicTypeHibernateDao extends DefaultParentRepository<TopicType>
        implements org.jtalks.poulpe.model.dao.TopicTypeDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<TopicType> getAll() {
        return getSession().createQuery("from TopicType").list();
    }

    @Override
    public boolean isTopicTypeNameExists(String topicTypeName) {
        String hql = "select count(*) from TopicType t where t.title = ?";
        Query query = getSession().createQuery(hql);
        query.setString(0, topicTypeName);
        Number count = (Number) query.uniqueResult();
        return count.intValue() != 0;
    }

}
