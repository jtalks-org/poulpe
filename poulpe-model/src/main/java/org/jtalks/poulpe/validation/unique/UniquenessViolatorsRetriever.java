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
package org.jtalks.poulpe.validation.unique;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.validation.annotations.UniqueConstraint;
import org.jtalks.poulpe.validation.annotations.UniqueField;

/**
 * Class for building queries for retrieving fields whose values are duplicated,
 * based on JSR-303 and annotations {@link UniqueConstraint} and
 * {@link UniqueField}. Used from {@link UniqueConstraintValidator} - and
 * injected there.
 * 
 * For further validation {@link UniquenessViolationFinder} is used, once the
 * list of duplicates is obtained via {@link #retrieve(Entity, List)}
 * 
 * @author Alexey Grigorev
 */
public class UniquenessViolatorsRetriever {

    private SessionFactory sessionFactory;

    /**
     * Looks for entities with the same values as in the given bean, also
     * removing from the result the bean itself (by its id).<br>
     * <br>
     * 
     * For example, for {@link Component} and for {@link Branch} the equivalents
     * in hql would be
     * 
     * <pre>
     * from Component c where (c.name = :name or c.componentType = :type) and c.id != :id
     * from Branch b where (b.name = :name) and b.id != :id
     * </pre>
     * 
     * 
     * @return list of entities whose uniqueness is violated
     */
    public List<EntityWrapper> duplicatesFor(EntityWrapper bean) {
        List<Entity> list = retrieveDuplicates(bean);
        return wrap(list);
    }

    private List<Entity> retrieveDuplicates(EntityWrapper bean) {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(bean.getEntityClass());

        criteria.add(atLeastOneFieldViolated(bean));
        criteria.add(Restrictions.ne("id", bean.getEntityId()));

        @SuppressWarnings("unchecked")
        List<Entity> list = criteria.list();
        return list;
    }

    private List<EntityWrapper> wrap(List<Entity> list) {
        List<EntityWrapper> wrappers = new ArrayList<EntityWrapper>();
        
        for (Entity entity : list) {
            wrappers.add(new EntityWrapper(entity));
        }
        
        return wrappers;
    }
    
    /**
     * Builds 'or' criterion for the list of fields. The equivalent in hql, for
     * example, for two fields 'name' and 'type' would be
     * 
     * <pre>
     * where s.name = :name or s.type = :type
     * </pre>
     * 
     * Another examples for one and three fields respectively:
     * 
     * <pre>
     * where s.name = :name
     * where s.name = :name or s.type = :type or s.whatever = :whatever
     * </pre>
     * 
     * @return {@link Criterion} ready for using via {@link Criteria}
     */
    private Criterion atLeastOneFieldViolated(EntityWrapper bean) {
        Iterator<Entry<String, Object>> iterator = bean.getIterator();
        Entry<String, Object> first = iterator.next();
        Criterion or = Restrictions.eq(first.getKey(), first.getValue());

        while (iterator.hasNext()) {
            Entry<String, Object> next = iterator.next();
            Object value = next.getValue();
            Criterion eq = Restrictions.eq(next.getKey(), value);
            or = Restrictions.or(or, eq);
        }

        return or;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
