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

import org.jtalks.common.model.dao.hibernate.DefaultParentRepository;
import org.jtalks.poulpe.model.dao.BranchDao;
import org.jtalks.poulpe.model.entity.Branch;

import java.util.List;

/**
 * @author Vitaliy Kravchenko
 * @author Pavel Vervenko
 */
public class BranchHibernateDao extends DefaultParentRepository<Branch> implements
        BranchDao {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Branch> getAll() {
        return getSession().createQuery("from Branch").list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBranchDuplicated(Branch branch) {
        return ((Number) getSession()
                .createQuery(
                        "select count(*) from Branch b where b.name = ? and b.id != ?")
                .setString(0, branch.getName()).setLong(1, branch.getId())
                .uniqueResult()).intValue() != 0;
    }
}