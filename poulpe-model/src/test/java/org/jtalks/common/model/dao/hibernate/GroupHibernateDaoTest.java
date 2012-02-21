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
package org.jtalks.common.model.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dao.hibernate.ObjectsFactory;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration(locations = {"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class GroupHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private GroupDao dao;

    private PoulpeGroup group;
    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
    }

    @Test
    /**
     * Straightforward saving of the group
     */
    public void testSave() {
        PoulpeGroup testGroup = ObjectsFactory.createGroup();
        session.save(testGroup);

        assertNotSame(testGroup.getId(), 0, "ID is not created");

        session.evict(testGroup);
        PoulpeGroup savedGroup = (PoulpeGroup) session.get(PoulpeGroup.class, testGroup.getId());

        assertReflectionEquals(testGroup, savedGroup);
    }

    @Test
    public void testGetAll() {
        group = ObjectsFactory.createGroup();
        session.save(group);
        List<PoulpeGroup> list = dao.getAll();
        assertTrue(list.contains(group));
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testSaveGroupWithNullName() {
        PoulpeGroup testGroup = new PoulpeGroup();
        dao.saveOrUpdate(testGroup);
    }

    public void testGetById() {
        PoulpeGroup group = ObjectsFactory.createGroup();
        session.save(group);

        PoulpeGroup result = dao.get(group.getId());

        assertNotNull(result);
        assertEquals(result.getId(), group.getId());
    }

    @Test
    public void testGetInvalidId() {
        PoulpeGroup result = dao.get(-567890L);
        assertNull(result);
    }

    @Test
    public void testGetMatchedByName() {
        group = ObjectsFactory.createGroup();
        session.save(group);
        List<PoulpeGroup> list = dao.getMatchedByName(group.getName());
        assertTrue(list.contains(group));

    }

    @Test
    public void testGetMatchedByNameWhenNameIsNull() {
        List<PoulpeGroup> listAll = dao.getAll();
        List<PoulpeGroup> listReturned = dao.getMatchedByName(null);
        assertEquals(listAll, listReturned);
    }

}
