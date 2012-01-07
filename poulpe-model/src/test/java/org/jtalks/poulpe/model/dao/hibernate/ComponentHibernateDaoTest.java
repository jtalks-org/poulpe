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
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The test for the {@code ComponentHibernateDao} implementation.
 * 
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 * @author Guram Savinov
 */
@ContextConfiguration(locations = { "classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ComponentHibernateDaoTest extends
        AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;

    private ComponentHibernateDao dao;
    private Session session;

    private Component forum;
    private Jcommune jcommune;

    @BeforeMethod
    public void setUp() throws Exception {
        dao = new ComponentHibernateDao();
        dao.setSessionFactory(sessionFactory);

        session = sessionFactory.getCurrentSession();
        jcommune = ObjectsFactory.createJcommune(10);
    }

    @Test
    public void testSave() {
        dao.saveOrUpdate(jcommune);
        assertNotSame(jcommune.getId(), 0, "Id not created");
        Jcommune actual = ObjectRetriever.retrieveUpdated(jcommune, session);
        assertReflectionEquals(jcommune, actual);
    }

    @Test
    public void testGet() {
        session.save(jcommune);
        Jcommune actual = (Jcommune) dao.get(jcommune.getId());
        assertReflectionEquals(jcommune, actual);
    }

    @Test
    public void testUpdate() {
        String newName = "new Jcommune name";

        session.save(jcommune);
        jcommune.setName(newName);
        dao.saveOrUpdate(jcommune);

        String actual = ObjectRetriever.retrieveUpdated(jcommune, session)
                .getName();
        assertEquals(actual, newName);
    }

    @Test
    public void testGetAll() {
        givenTwoComponents();

        List<Component> cList = dao.getAll();

        assertEquals(cList.size(), 2);
    }

    private void givenTwoComponents() {
        givenForum();
        givenArticle();
    }

    private void givenForum() {
        forum = createForum();
        session.save(forum);
    }

    private void givenArticle() {
        session.save(createArticle());
    }

    private Component createForum() {
        return ObjectsFactory.createComponent(ComponentType.FORUM);
    }

    private Component createArticle() {
        return ObjectsFactory.createComponent(ComponentType.ARTICLE);
    }

    @Test
    public void testGetAvailableTypes() {
        Set<ComponentType> availableTypes = dao.getAvailableTypes();

        assertAllTypesAvailable(availableTypes);
    }

    private void assertAllTypesAvailable(Set<ComponentType> availableTypes) {
        List<ComponentType> allActualTypes = Arrays.asList(ComponentType
                .values());
        assertEquals(availableTypes.size(), allActualTypes.size());
        assertTrue(availableTypes.containsAll(allActualTypes));
    }

    @Test
    public void testGetAvailableTypesAfterInsert() {
        givenForum();

        Set<ComponentType> availableTypes = dao.getAvailableTypes();

        assertForumUnavailable(availableTypes);
    }

    private void assertForumUnavailable(Set<ComponentType> availableTypes) {
        assertFalse(availableTypes.contains(forum.getComponentType()));
    }

    @Test
    public void testSectionPositions() {
        for (int i = 0; i < 5; i++) {
            List<Section> expected = jcommune.getSections();
            Collections.shuffle(expected);

            dao.saveOrUpdate(jcommune);

            jcommune = ObjectRetriever.retrieveUpdated(jcommune, session);
            List<Section> actual = jcommune.getSections();

            assertEquals(actual, expected);
        }
    }
}
