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
import static org.testng.Assert.assertNotSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.dao.JcommuneDao;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The test for the {@code JcommuneHibernateDao} implementation.
 * 
 * @author Guram Savinov
 */
@ContextConfiguration(locations = { "classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class JcommuneHibernateDaoTest extends
        AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private JcommuneDao dao;

    private Session session;
    private Jcommune jcommune;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
        jcommune = ObjectsFactory.createJcommune(5);
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
        Jcommune actual = dao.get(jcommune.getId());
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

}