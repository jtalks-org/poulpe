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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.dao.SectionDao;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * The test for {@link SectionHibernateDao}.
 *
 * @author Dmitriy Sukharev
 * @author Vahluev Vyacheslav
 * @author Alexey Grigorev
 * @author Guram Savinov
 */
@ContextConfiguration(locations = {"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class SectionHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private SectionDao dao;

    private Session session;
    private PoulpeSection section;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
        section = TestFixtures.sectionWithBranches(10);
    }

    @Test
    public void saveSectionTest() {
        dao.saveOrUpdate(section);
        session.flush();
        assertSectionSaved();
    }

    private void assertSectionSaved() {
        assertNotSame(section.getId(), 0, "Id not created");
        PoulpeSection actual = retrieveActualSection();
        assertReflectionEquals(section, actual);
    }

    private PoulpeSection retrieveActualSection() {
        return ObjectRetriever.retrieveUpdated(section, session);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void saveSectionWithNameNotNullViolationTest() {
        PoulpeSection nullTitleSection = new PoulpeSection();
        dao.saveOrUpdate(nullTitleSection);
    }

    @Test
    public void getTest() {
        givenSection();
        PoulpeSection actual = dao.get(section.getId());
        assertReflectionEquals(section, actual);
    }

    private void givenSection() {
        session.save(section);
    }

    @Test
    public void getInvalidIdTest() {
        PoulpeSection result = dao.get(-567890L);
        assertNull(result);
    }

    @Test
    public void updateTest() {
        givenSection();

        String newName = "new section name";
        section.setName(newName);

        dao.saveOrUpdate(section);
        session.flush();
        assertNameChanged(newName);
    }

    private void assertNameChanged(String newName) {
        PoulpeSection actual = retrieveActualSection();
        assertEquals(actual.getName(), newName);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void UpdateNotNullViolationTest() {
        givenSection();
        section.setName(null);
        dao.saveOrUpdate(section);
        session.flush();
    }

    @Test
    public void getAllTest() {
        givenTwoSections();
        List<PoulpeSection> sections = dao.getAll();
        assertEquals(sections.size(), 2);
    }

    private void givenTwoSections() {
        session.save(TestFixtures.section());
        session.save(TestFixtures.section());
    }

    @Test
    public void GetAllWhenTableIsEmptyTest() {
        List<PoulpeSection> sections = dao.getAll();
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
            List<PoulpeBranch> expected = section.getPoulpeBranches();
            Collections.shuffle(expected);

            dao.saveOrUpdate(section);
            session.flush();

            section = ObjectRetriever.retrieveUpdated(section, session);
            List<PoulpeBranch> actual = section.getPoulpeBranches();

            assertEquals(actual, expected);
        }
    }
}
