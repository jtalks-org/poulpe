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

import org.jtalks.common.model.dao.hibernate.AbstractHibernateParentRepository;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;

import java.util.List;

/**
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 */
public class GroupHibernateDao extends AbstractHibernateParentRepository<Group> implements
        GroupDao {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Group> getAll() {
        return getSession().createQuery("from Group").list();
    }
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGroupDuplicated(Group group) {
        return ((Number) getSession()
                .createQuery(
                        "select count(*) from Group g where g.name = ? and g.id != ?")
                .setString(0, group.getName()).setLong(1, group.getId())
                .uniqueResult()).intValue() != 0;
    }
    
    @Override
    public List<Group> getMatchedByName(String name) {
        if(name == null){
            return getAll();
        }
        return getSession().createQuery("from Group g where g.name like ?").setString(0, "%"+name+"%").list();
    }
}