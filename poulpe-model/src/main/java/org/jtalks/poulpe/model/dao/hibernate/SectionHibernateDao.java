/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.model.dao.hibernate;

import java.util.List;

import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Section;

/**
 *  @author tanya birina
 *  @author Dmitriy Sukharev
 */
public class SectionHibernateDao extends AbstractHibernateDao<Section> implements
		SectionDao {

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
    public boolean isSectionNameExists(String section) {
        return ((Number) getSession()
                .createQuery("select count(*) from Section s where s.name = ?")
                .setString(0, section)
                .uniqueResult()).intValue() != 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteRecursively(Long id) {
// TODO   Uncomment me when Section#getBranches method will be public
//        final String secectQuery = "FROM Section WHERE id=:id";
//        Section victim = (Section) getSession().createQuery(secectQuery).setLong("id", id).uniqueResult();
//        victim.getBranches().clear();
//        final String querySection = "DELETE FROM Section WHERE id = :id";
//        return getSession().createQuery(querySection).setLong("id", id).executeUpdate() == 1;
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteAndMoveBranchesTo(Long id, Long recipientId) {
// TODO   Uncomment me when Section#getBranches method will be public
//        final String query = "FROM Section WHERE id=:id";
//        Section victim = (Section) getSession().createQuery(query).setLong("id", id).uniqueResult();
//        Section recipient = (Section) getSession().createQuery(query ).setLong("id", recipientId).uniqueResult();
//        recipient.getBranches().addAll(victim.getBranches());
//        saveOrUpdate(recipient);
//        final String querySection = "DELETE FROM Section WHERE id = :id";
//        return getSession().createQuery(querySection).setLong("id", id).executeUpdate() == 1;
        return false;
    }

}
