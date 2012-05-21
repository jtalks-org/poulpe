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

import org.jtalks.common.model.dao.hibernate.AbstractHibernateParentRepository;
import org.jtalks.common.model.entity.Branch;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.PoulpeSection;

/**
 * @author tanya birina
 * @author Dmitriy Sukharev
 */
public class SectionHibernateDao extends AbstractHibernateParentRepository<PoulpeSection> implements SectionDao {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PoulpeSection> getAll() {
        return getSession().createQuery("from PoulpeSection").list();
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteRecursively(PoulpeSection section) {
        PoulpeSection victim = (PoulpeSection) getSession().load(PoulpeSection.class, section.getId());
        getSession().delete(victim);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteAndMoveBranchesTo(PoulpeSection victimSection, PoulpeSection recipientSection) {
        PoulpeSection victim = (PoulpeSection) getSession().load(PoulpeSection.class, victimSection.getId());
        PoulpeSection recipient = (PoulpeSection) getSession().load(PoulpeSection.class, recipientSection.getId());
        for (Branch branch : victim.getBranches()) {
            branch.setSection(recipient);
        }
        recipient.getBranches().addAll(victim.getBranches());
        getSession().saveOrUpdate(recipient);
        // The reason for flush and evict: https://forum.hibernate.org/viewtopic.php?f=1&t=1012422
        getSession().flush();
        getSession().evict(victim);
        final String querySection = "DELETE FROM PoulpeSection WHERE id = :id";
        return getSession().createQuery(querySection).setLong("id", victim.getId()).executeUpdate() == 1;
    }

}
