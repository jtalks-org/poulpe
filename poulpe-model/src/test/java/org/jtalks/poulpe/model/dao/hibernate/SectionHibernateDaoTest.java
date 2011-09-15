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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class SectionHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private SectionDao dao;
    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
    }

    @Test
    public void deleteRecursevelyTest() {
        Section section = ObjectsFactory.createSectionWithBranches();
        session.save(section);
        dao.deleteRecursively(section.getId());
        Long actualAmount = (Long) session
                .createQuery("SELECT count(b) FROM Section s JOIN s.branches b WHERE s.id=:id")
                .setLong("id", section.getId()).uniqueResult();
        assertEquals(actualAmount, (Long) 0L);
    }

    @Test
    public void deleteAndMoveBranchesToTest() {
        Section victim = ObjectsFactory.createSectionWithBranches();
        Section recipient = ObjectsFactory.createSectionWithBranches();
        Long victimBranchesAmount = (long) victim.getBranches().size();
        recipient.getBranches().clear();
        session.save(victim);
        session.save(recipient);
        
        boolean isDeleted = dao.deleteAndMoveBranchesTo(victim.getId(), recipient.getId());
        assertTrue(isDeleted);
        Long actualAmount = (Long) session.createQuery("SELECT count(b) FROM Section s JOIN s.branches b WHERE s.id=:id").setLong("id",victim.getId()).uniqueResult();
        assertEquals(actualAmount, (Long)0L);
        actualAmount = (Long) session.createQuery("SELECT count(b) FROM Section s JOIN s.branches b WHERE s.id=:id").setLong("id",recipient.getId()).uniqueResult();
        assertEquals(actualAmount, victimBranchesAmount);
    }
    
    
    @Test
    public void saveSectionTest() {
        Section section = ObjectsFactory.createSection();

        dao.saveOrUpdate(section);

        assertNotSame(section.getId(), 0, "Id not created");

        session.evict(section);
        Section result = (Section) session.get(Section.class, section.getId());

        assertReflectionEquals(section, result);
    }
    
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveSectionWithNameNotNullViolationTest() {
        Section section = new Section();

        dao.saveOrUpdate(section);
    }
    
    @Test
    public void getTest() {
        Section section = ObjectsFactory.createSection();
        session.save(section);

        Section result = dao.get(section.getId());

        assertNotNull(result);
        assertEquals(result.getId(), section.getId());
    }
    
    @Test
    public void getInvalidIdTest() {
        Section result = dao.get(-567890L);

        assertNull(result);
    }
    
    @Test
    public void updateTest() {
        String newName = "new section name";
        Section section = ObjectsFactory.createSection();
        session.save(section);
        section.setName(newName);

        dao.saveOrUpdate(section);
        session.evict(section);
        Section result = (Section) session.get(Section.class, section.getId());

        assertEquals(result.getName(), newName);
    }
    
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void UpdateNotNullViolationTest() {
        Section section = ObjectsFactory.createSection();
        session.save(section);
        section.setName(null);

        dao.saveOrUpdate(section);
    }
    
    @Test
    public void getAllTest() {
        Section section1 = ObjectsFactory.createSection();
        session.save(section1);
        Section section2 = ObjectsFactory.createSection();
        session.save(section2);

        List<Section> sections = dao.getAll();

        assertEquals(sections.size(), 2);
    }
    
    @Test
    public void GetAllWhenTableIsEmptyTest() {
        List<Section> sections = dao.getAll();

        assertTrue(sections.isEmpty());
    }

    @Test
    public void isSectionExistTest() {
        Section section = ObjectsFactory.createSection();
        session.save(section);

        assertTrue(dao.isExist(section.getId()));
    }

      
    @Test
    public void NotExistingSectionTest() {
     assertFalse(dao.isExist(99999L));
    }
}