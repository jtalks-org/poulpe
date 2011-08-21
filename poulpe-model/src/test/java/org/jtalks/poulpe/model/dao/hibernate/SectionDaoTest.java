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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The test for {@link SectionHibernateDao}.
 * @author Dmitriy Sukharev
 */
@ContextConfiguration(locations = { "classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class SectionDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private SectionDao dao;
    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
        ObjectsFactory.setSession(session);
    }

// TODO Uncomment me when Section#getBranches method will be public
//    @Test
//    public void deleteRecursevelyTest() {
//        Section section = ObjectsFactory.createSection();
//        session.save(section);
//        dao.deleteRecursively(section.getId());
//        // let's say due to rollback we don't have any branches but section's, but it would be
//        // better if I had list of branches.
//        // TODO Finish this test when Section#getBranches method will be public
//        Long actualAmount = (Long) session.createQuery("SELECT count(*) FROM Branch").uniqueResult();
//        assertEquals(actualAmount, (Long) 0L);
//    }

// TODO Uncomment me when Section#getBranches method will be public
//    @Test
//    public void deleteAndMoveBranchesToTest() {
//        Section victim = ObjectsFactory.createSection();
//        Section recipient = ObjectsFactory.createSection();
//        Long victimBranchesAmount = (long) victim.getBranches().size();
//        recipient.getBranches().clear();
//        session.save(victim);
//        session.save(recipient);
//        
//        boolean isDeleted = dao.deleteAndMoveBranchesTo(victim.getId(), recipient.getId());
//        assertTrue(isDeleted);
//        // let's say due to rollback we don't have any branches but section's, but it would be
//        // better if I had list of branches.
//        Long actualAmount = (Long) session.createQuery("SELECT count(b) FROM Section s JOIN s.branches b WHERE s.id=:id").setLong("id",victim.getId()).uniqueResult();
//        assertEquals(actualAmount, (Long)0L);
//        actualAmount = (Long) session.createQuery("SELECT count(b) FROM Section s JOIN s.branches b WHERE s.id=:id").setLong("id",recipient.getId()).uniqueResult();
//        assertEquals(actualAmount, victimBranchesAmount);
//    }
}