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
import org.jtalks.poulpe.model.entity.Branch;
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
        Section victim = (Section) getSession().load(Section.class, id);
        victim.getBranches().clear();
        final String querySection = "DELETE FROM Section WHERE id = :id";
        return getSession().createQuery(querySection).setLong("id", id).executeUpdate() == 1;
    }

//    Section section = new Section();
//    section.setName("section");
//    section.setBranches(new ArrayList<Branch>());
//    try {
//        sectionService.saveSection(section);
//    } catch (NotUniqueException e) {
//        e.printStackTrace();
//    }
//    Branch branch = new Branch();
//    branch.setName("branch");
//    branch.setSection(section);
//    section.getBranches().add(branch);
//    try {
//        branchService.saveBranch(branch);
//        sectionService.saveSection(section);    // to save indexes
//    } catch (NotUniqueException e) {
//        e.printStackTrace();
//    }
    
    /** {@inheritDoc} */
    @Override
    public boolean deleteAndMoveBranchesTo(Long id, Long recipientId) {
        Section victim = (Section) getSession().load(Section.class, id);
        Section recipient = (Section) getSession().load(Section.class, recipientId);
//        final String updateQuery = "UPDATE Branch SET section = :recipient WHERE section = :victim";
//        getSession().createQuery(updateQuery).setEntity("recipient", recipient).setEntity("victim", victim).executeUpdate();
        for (Branch branch : victim.getBranches()) {
            branch.setSection(recipient);
            getSession().save(branch);
        }
        recipient.getBranches().addAll(victim.getBranches());
        saveOrUpdate(recipient);
        victim.getBranches().clear();
//        try {
//            getSession().delete(victim);
//            return true;
//        } catch (HibernateException e) {
//            // TODO: handle exception
//            return false;
//        }
//        recipient.getBranches().addAll(victim.getBranches());
//        saveOrUpdate(recipient);
//        victim.getBranches().clear();
//        saveOrUpdate(victim);
        final String querySection = "DELETE FROM Section WHERE id = :id";
        return getSession().createQuery(querySection).setLong("id", id).executeUpdate() == 1;
    }

}
