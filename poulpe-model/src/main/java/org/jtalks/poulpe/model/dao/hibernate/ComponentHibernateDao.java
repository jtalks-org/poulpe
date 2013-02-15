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

import org.hibernate.Query;
import org.jtalks.common.model.dao.hibernate.AbstractHibernateParentRepository;
import org.jtalks.common.model.entity.Component;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.entity.ComponentBase;
import org.jtalks.common.model.entity.ComponentType;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Implementation of dao for {@link Component}.
 * 
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 */
public class ComponentHibernateDao extends AbstractHibernateParentRepository<Component> implements ComponentDao {

    /** {@inheritDoc} */
    @Override
    public List<Component> getAll() {
        Query query = getSession().getNamedQuery("allComponents");
        
        @SuppressWarnings("unchecked")
        List<Component> result = query.list();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Set<ComponentType> getAvailableTypes() {
        Set<ComponentType> result = new LinkedHashSet<ComponentType>();
        Collections.addAll(result, ComponentType.values());

        for (Component current : getAll()) {
            result.remove(current.getComponentType());
        }

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Component getByType(ComponentType componentType) {
        Query query = getSession().getNamedQuery("findComponentByComponentType");
        query.setParameter("componentType", componentType);
        return (Component) query.uniqueResult();
    }

    /** {@inheritDoc} */
    @Override
    public ComponentBase getBaseComponent(ComponentType componentType) {
        Query query = getSession().getNamedQuery("findBaseComponentByComponentType");
        query.setParameter("componentType", componentType);
        return (ComponentBase) query.uniqueResult();
    }

}
