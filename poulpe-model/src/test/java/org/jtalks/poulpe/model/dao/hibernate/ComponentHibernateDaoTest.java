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
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.ComponentBase;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * The test for the {@code ComponentHibernateDao} implementation.
 *
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 * @author Guram Savinov
 */
@ContextConfiguration(locations = {"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ComponentHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;

    private ComponentHibernateDao dao;
    private Session session;

    private Jcommune forum;

    @BeforeMethod
    public void setUp() throws Exception {
        dao = new ComponentHibernateDao(sessionFactory);
        session = sessionFactory.getCurrentSession();
        forum = createForum();
    }

    private static Jcommune createForum() {
        return TestFixtures.jcommuneWithSections(10);
    }

    @Test
    public void testSave() {
        dao.saveOrUpdate(forum);
        session.flush();
        Component actual = ObjectRetriever.retrieveUpdated(forum, session);
        assertReflectionEquals(forum, actual);
    }

    @Test
    public void testGet() {
        session.save(forum);
        Component actual = dao.get(forum.getId());
        assertReflectionEquals(forum, actual);
    }

    @Test
    public void testUpdate() {
        String newName = "new Jcommune name";

        session.save(forum);
        forum.setName(newName);
        dao.saveOrUpdate(forum);
        session.flush();

        String actual = ObjectRetriever.retrieveUpdated(forum, session).getName();
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

    private Component createArticle() {
        return TestFixtures.component(ComponentType.ARTICLE);
    }

    @Test
    public void testGetAvailableTypes() {
        Set<ComponentType> availableTypes = dao.getAvailableTypes();

        assertAllTypesAvailable(availableTypes);
    }

    private void assertAllTypesAvailable(Set<ComponentType> availableTypes) {
        List<ComponentType> allActualTypes = Arrays.asList(ComponentType.values());
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
        List<PoulpeSection> expected = forum.getSections();
        Collections.shuffle(expected);

        dao.saveOrUpdate(forum);
        session.flush();

        forum = ObjectRetriever.retrieveUpdated(forum, session);
        List<PoulpeSection> actual = forum.getSections();

        assertEquals(actual, expected);
    }

    @Test
    public void testGetForum() {
        givenTwoComponents();
        Component actual = dao.getByType(ComponentType.FORUM);
        assertReflectionEquals(forum, actual);
    }

    @Test
    public void deleteForum() {
        givenForum();
        dao.delete(forum);
        assertForumDeleted(dao.getAvailableTypes());
    }

    private void assertForumDeleted(Set<ComponentType> availableTypes) {
        assertTrue(availableTypes.contains(forum.getComponentType()));
    }

    @Test
    public void getBaseComponent() {
        ComponentBase expected = givenBaseComponent();
        ComponentBase actual = dao.getBaseComponent(ComponentType.FORUM);
        assertReflectionEquals(expected, actual);
    }

    private ComponentBase givenBaseComponent() {
        ComponentBase baseComponent = new ComponentBase(ComponentType.FORUM);
        session.save(baseComponent);
        return baseComponent;
    }

}
