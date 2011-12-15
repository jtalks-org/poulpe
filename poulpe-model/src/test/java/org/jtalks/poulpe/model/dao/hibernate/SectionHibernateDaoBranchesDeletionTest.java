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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.hibernate.Query;
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
 * Test for {@link SectionHibernateDao} for checking deletion of branches 
 * in sections.
 * 
 * @author Dmitriy Sukharev
 * @author Vahluev Vyacheslav
 * @author Alexey Grigorev
 */
@ContextConfiguration(locations = { "classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class SectionHibernateDaoBranchesDeletionTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private SectionDao dao;
    
    private Session session;

    private int branchesAmount = 10;
    private Section section;
    
    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
        section = sectionWithSomeBranches(branchesAmount);
    }
    
    @Test
    public void deleteRecursevelyTest() {
        dao.deleteRecursively(section);
        assertAllBranchesDeleted();
    }
    
    private void assertAllBranchesDeleted() {
        assertActualAmountOfBranches(section, 0);
    }
    
    @Test
    public void deleteAndMoveBranchesToTest() {
        Section recipient = sectionWithNoBranches();       

        boolean isDeleted = dao.deleteAndMoveBranchesTo(section, recipient);
        assertTrue(isDeleted);
        
        assertAllBranchesDeleted();
        assertBranchesMovedTo(recipient);
    }

    private void assertBranchesMovedTo(Section section) {
        assertActualAmountOfBranches(section, branchesAmount);
    }
    
    private void assertActualAmountOfBranches(Section recipient, int branchesAmount) {
        Long actualAmount = retrieveActualBranchesAmount(recipient);
        assertEquals(actualAmount, Long.valueOf(branchesAmount));
    }
    
    private Long retrieveActualBranchesAmount(Section section) {
        String countQuery = "SELECT count(b) FROM Section s JOIN s.branches b WHERE s.id=:id";
        Query query = session.createQuery(countQuery).setLong("id", section.getId());
        return (Long) query.uniqueResult();
    }
    
    private Section sectionWithSomeBranches(int brancesAmount) {
        Section section = ObjectsFactory.createSectionWithBranches(brancesAmount);
        session.save(section);
        return section;
    }
    
    private Section sectionWithNoBranches() {
        return sectionWithSomeBranches(0);
    }

}