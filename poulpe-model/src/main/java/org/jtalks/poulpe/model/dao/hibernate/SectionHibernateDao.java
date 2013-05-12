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

import org.hibernate.SessionFactory;
import org.jtalks.common.model.dao.hibernate.GenericDao;
import org.jtalks.common.model.entity.Branch;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.PoulpeSection;

import java.util.List;

/**
 * @author tanya birina
 * @author Dmitriy Sukharev
 */
public class SectionHibernateDao extends GenericDao<PoulpeSection> implements SectionDao {

    /**
     * @param sessionFactory The SessionFactory.
     */
    public SectionHibernateDao(SessionFactory sessionFactory) {
        super(sessionFactory, PoulpeSection.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PoulpeSection> getAll() {
        return session().createQuery("from PoulpeSection").list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteRecursively(PoulpeSection section) {
        PoulpeSection victim = (PoulpeSection) session().load(PoulpeSection.class, section.getId());
        session().delete(victim);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteAndMoveBranchesTo(PoulpeSection victimSection, PoulpeSection recipientSection) {
        PoulpeSection victim = (PoulpeSection) session().load(PoulpeSection.class, victimSection.getId());
        PoulpeSection recipient = (PoulpeSection) session().load(PoulpeSection.class, recipientSection.getId());
        for (Branch branch : victim.getBranches()) {
            branch.setSection(recipient);
        }
        recipient.getBranches().addAll(victim.getBranches());
        session().saveOrUpdate(recipient);
        // The reason for flush and evict: https://forum.hibernate.org/viewtopic.php?f=1&t=1012422
        session().flush();
        session().evict(victim);
        final String querySection = "DELETE FROM PoulpeSection WHERE id = :id";
        return session().createQuery(querySection).setLong("id", victim.getId()).executeUpdate() == 1;
    }

}
