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
import org.jtalks.poulpe.model.dao.TopicTypeDao;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * @author Vladimir Bukhtoyarov
 */
@ContextConfiguration(locations = {"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class TopicTypeDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TopicTypeDao dao;
    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
    }

    @Test
    public void testSave() {
        TopicType topicType = TestFixtures.topicType();
        dao.saveOrUpdate(topicType);

        assertNotSame(topicType.getId(), 0, "Id not created");

        session.evict(topicType);
        TopicType result = (TopicType) session.get(TopicType.class,
                topicType.getId());

        assertReflectionEquals(topicType, result);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testSaveTopicTypeWithNameNotNullViolation() {
        TopicType TopicType = new TopicType();

        dao.saveOrUpdate(TopicType);
    }

    @Test
    public void testGet() {
        TopicType TopicType = TestFixtures.topicType();
        session.save(TopicType);

        TopicType result = dao.get(TopicType.getId());

        assertNotNull(result);
        assertEquals(result.getId(), TopicType.getId());
    }

    @Test
    public void testGetInvalidId() {
        TopicType result = dao.get(-567890L);

        assertNull(result);
    }

    @Test
    public void testUpdate() {
        TopicType topicType = TestFixtures.topicType();
        session.save(topicType);

        String newTitle = "new title";
        topicType.setTitle(newTitle);
        dao.saveOrUpdate(topicType);
        session.flush();
        session.evict(topicType);
        TopicType result = (TopicType) session.get(TopicType.class, topicType.getId());

        assertEquals(result.getTitle(), newTitle);
    }

    @Test
    public void testTopicTypeConstructor() {
        String title = "Title";
        String description = "Description";
        TopicType topicType = new TopicType(title, description);
        assertEquals(title, topicType.getTitle());
        assertEquals(description, topicType.getDescription());
    }

    @Test
    public void testDelete() {
        TopicType topicType = TestFixtures.topicType();
        session.save(topicType);

        boolean result = dao.delete(topicType.getId());
        int TopicTypeCount = getCount();

        assertTrue(result, "Entity is not deleted");
        assertEquals(TopicTypeCount, 0);
    }

    @Test
    public void testDeleteInvalidId() {
        boolean result = dao.delete(-100500L);

        assertFalse(result, "Entity deleted");
    }

    @Test
    public void testGetAll() {
        TopicType topicType1 = TestFixtures.topicType();
        session.save(topicType1);
        TopicType topicType2 = TestFixtures.topicType();
        session.save(topicType2);

        List<TopicType> topicTypes = dao.getAll();

        assertEquals(topicTypes.size(), 2);
    }

    @Test
    public void testGetAllWithEmptyTable() {
        List<TopicType> TopicTypees = dao.getAll();

        assertTrue(TopicTypees.isEmpty());
    }

    @Test
    public void testIsExist() {
        TopicType TopicType = TestFixtures.topicType();
        session.save(TopicType);

        assertTrue(dao.isExist(TopicType.getId()));
    }

    @Test
    public void testIsNotExist() {
        assertFalse(dao.isExist(99999L));
    }

    private int getCount() {
        return ((Number) session.createQuery("select count(*) from TopicType").uniqueResult()).intValue();
    }
}
