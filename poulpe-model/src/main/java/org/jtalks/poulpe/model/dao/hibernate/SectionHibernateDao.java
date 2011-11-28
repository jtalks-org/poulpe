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
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;

/**
 * @author tanya birina
 * @author Dmitriy Sukharev
 */
public class SectionHibernateDao extends AbstractHibernateParentRepository<Section> implements SectionDao {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Section> getAll() {
        return getSession().createQuery("from Section").list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSectionNameExists(Section section) {    	
        return ((Number) getSession()
                .createQuery("select count(*) from Section s where s.name = ?")
                .setString(0, section.getName()).uniqueResult()).intValue() != 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteRecursively(Section section) {
        Section victim = (Section) getSession().load(Section.class, section.getId());
        getSession().delete(victim);
        // There's no use catching HibernateException to get to know if any section was deleted. I
        // read source code of Hibernate delete method, such case just is logged (in trace
        // level) and ignored there. So there is no way we can get to know if any section was
        // deleted. I can't use HQL here as well. So I'm just returning true.
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteAndMoveBranchesTo(Section victimSection, Section recipientSection) {
        Section victim = (Section) getSession().load(Section.class, victimSection.getId());
        Section recipient = (Section) getSession().load(Section.class, recipientSection.getId());
        for (Branch branch : victim.getBranches()) {
            branch.setSection(recipient);
        }
        recipient.getBranches().addAll(victim.getBranches());
        getSession().saveOrUpdate(recipient);
        // The reason for flush and evict: https://forum.hibernate.org/viewtopic.php?f=1&t=1012422
        getSession().flush();
        getSession().evict(victim);
        final String querySection = "DELETE FROM Section WHERE id = :id";
        return getSession().createQuery(querySection).setLong("id", victim.getId()).executeUpdate() == 1;
    }

}
