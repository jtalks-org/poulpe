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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.Branch;
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
 * 
 * @author Dmitriy Sukharev
 * @author Vahluev Vyacheslav
 * @author Alexey Grigorev
 * @author Guram Savinov
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
    private Section section;
    
    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
        section = ObjectsFactory.createSectionWithBranches(10);
    }

    @Test
    public void saveSectionTest() {
        dao.saveOrUpdate(section);
        assertSectionSaved();
    }

    private void assertSectionSaved() {
        assertNotSame(section.getId(), 0, "Id not created");
        Section actual = retrieveActualSection();
        assertReflectionEquals(section, actual);
    }
    
    private Section retrieveActualSection() {
        return ObjectRetriever.retrieveUpdated(section, session);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveSectionWithNameNotNullViolationTest() {
        Section nullTitleSection = new Section();
        dao.saveOrUpdate(nullTitleSection);
    }

    @Test
    public void getTest() {
        givenSection();
        Section actual = dao.get(section.getId());
        assertReflectionEquals(section, actual);
    }

    private void givenSection() {
        session.save(section);
    }

    @Test
    public void getInvalidIdTest() {
        Section result = dao.get(-567890L);
        assertNull(result);
    }

    @Test
    public void updateTest() {
        givenSection();

        String newName = "new section name";
        section.setName(newName);

        dao.saveOrUpdate(section);
        
        assertNameChanged(newName);
    }

    private void assertNameChanged(String newName) {
        Section actual = retrieveActualSection();
        assertEquals(actual.getName(), newName);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void UpdateNotNullViolationTest() {
        givenSection();
        section.setName(null);
        dao.saveOrUpdate(section);
    }

    @Test
    public void getAllTest() {
        givenTwoSections();
        List<Section> sections = dao.getAll();
        assertEquals(sections.size(), 2);
    }

    private void givenTwoSections() {
        session.save(ObjectsFactory.createSection());
        session.save(ObjectsFactory.createSection());
    }

    @Test
    public void GetAllWhenTableIsEmptyTest() {
        List<Section> sections = dao.getAll();
        assertTrue(sections.isEmpty());
    }

    @Test
    public void isSectionExistTest() {
        givenSection();
        assertTrue(dao.isExist(section.getId()));
    }

    @Test
    public void notExistingSectionTest() {
        assertFalse(dao.isExist(99999L));
    }

    @Test
    public void testBranchPositions() {
        for (int i = 0; i < 5; i++) {
            List<Branch> expected = section.getBranches();
            Collections.shuffle(expected);

            dao.saveOrUpdate(section);

            section = ObjectRetriever.retrieveUpdated(section, session);
            List<Branch> actual = section.getBranches();

            assertEquals(actual, expected);
        }
    }
}
