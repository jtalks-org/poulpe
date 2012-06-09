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

import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.common.model.entity.Rank;
import org.jtalks.poulpe.model.dao.RankDao;
import org.jtalks.poulpe.test.fixtures.Fixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for RankHiberanateDao
 * @author Pavel Vervenko
 */
@ContextConfiguration(locations = {"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class RankHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private RankDao dao;
    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
    }

    @Test
    public void testSave() {
        Rank rank = Fixtures.createRank();
        dao.saveOrUpdate(rank);

        assertNotSame(rank.getId(), 0, "Id not created");

        session.evict(rank);
        Rank result = (Rank) session.get(Rank.class, rank.getId());

        assertReflectionEquals(rank, result);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testSaveBranchWithNameNotNullViolation() {
        Rank rank = new Rank("");
        rank.setRankName(null);

        dao.saveOrUpdate(rank);
    }

    @Test
    public void testGet() {
        Rank rank = Fixtures.createRank();
        session.save(rank);

        Rank result = dao.get(rank.getId());

        assertNotNull(result);
        assertEquals(result.getId(), rank.getId());
    }

    @Test
    public void testUpdate() {
        String newName = "new name";
        Rank rank = Fixtures.createRank();
        session.save(rank);
        rank.setRankName(newName);

        dao.saveOrUpdate(rank);
        session.evict(rank);
        Rank result = (Rank) session.get(Rank.class, rank.getId());

        assertEquals(result.getRankName(), newName);
    }

    @Test
    public void testDelete() {
        Rank rank = Fixtures.createRank();
        session.save(rank);

        boolean result = dao.delete(rank.getId());
        int rankCount = dao.getAll().size();

        assertTrue(result, "Entity is not deleted");
        assertEquals(rankCount, 0);
    }

    @Test
    public void testGetAll() {
        Rank rank1 = Fixtures.createRank();
        session.save(rank1);
        Rank rank2 = Fixtures.createRank();
        session.save(rank2);

        List<Rank> branches = dao.getAll();

        assertEquals(branches.size(), 2);
    }

    @Test
    public void testIsExist() {
        Rank rank = Fixtures.createRank();
        session.save(rank);

        assertTrue(dao.isExist(rank.getId()));
    }
    
    @Test
    public void testIsRankNameExists() {
        Rank rank = Fixtures.createRank();
        session.save(rank);
        
        assertTrue(dao.isRankNameExists(rank.getRankName()));
    }
}
