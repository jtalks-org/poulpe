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
import org.jtalks.poulpe.model.dao.BranchDao;
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
 * @author Kirill Afonin
 */
@ContextConfiguration(locations = {"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class BranchHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private BranchDao dao;
    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
    }

    /*===== Common methods =====*/

    @Test
    public void testSave() {
        Branch branch = ObjectsFactory.getDefaultBranch();
        session.save(branch.getSection());
        dao.saveOrUpdate(branch);

        assertNotSame(branch.getId(), 0, "Id not created");

        session.evict(branch);
        Branch result = (Branch) session.get(Branch.class, branch.getId());

        assertReflectionEquals(branch, result);
    }


    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testSaveBranchWithNameNotNullViolation() {
        Branch branch = new Branch();

        dao.saveOrUpdate(branch);
    }

    @Test
    public void testGet() {
        Branch branch = ObjectsFactory.getDefaultBranch();
        session.save(branch.getSection());
        session.save(branch);

        Branch result = dao.get(branch.getId());

        assertNotNull(result);
        assertEquals(result.getId(), branch.getId());
    }

    @Test
    public void testGetInvalidId() {
        Branch result = dao.get(-567890L);

        assertNull(result);
    }

    @Test
    public void testUpdate() {
        String newName = "new name";
        Branch branch = ObjectsFactory.getDefaultBranch();
        session.save(branch.getSection());
        session.save(branch);
        branch.setName(newName);

        dao.saveOrUpdate(branch);
        session.evict(branch);
        Branch result = (Branch) session.get(Branch.class, branch.getId());

        assertEquals(result.getName(), newName);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testUpdateNotNullViolation() {
        Branch branch = ObjectsFactory.getDefaultBranch();
        session.save(branch.getSection());
        session.save(branch);
        branch.setName(null);

        dao.saveOrUpdate(branch);
    }

    @Test
    public void testDelete() {
        Branch branch = ObjectsFactory.getDefaultBranch();
        session.save(branch.getSection());
        session.save(branch);

        boolean result = dao.delete(branch.getId());
        int branchCount = getCount();

        assertTrue(result, "Entity is not deleted");
        assertEquals(branchCount, 0);
    }

    @Test
    public void testDeleteInvalidId() {
        boolean result = dao.delete(-100500L);

        assertFalse(result, "Entity deleted");
    }

    @Test
    public void testGetAll() {
        Branch branch1 = ObjectsFactory.getDefaultBranch();
        session.save(branch1.getSection());
        session.save(branch1);
        Branch branch2 = ObjectsFactory.getDefaultBranch();
        session.save(branch2.getSection());
        session.save(branch2);

        List<Branch> branches = dao.getAll();

        assertEquals(branches.size(), 2);
    }

    @Test
    public void testGetAllWithEmptyTable() {
        List<Branch> branches = dao.getAll();

        assertTrue(branches.isEmpty());
    }

    @Test
    public void testIsExist() {
        Branch branch = ObjectsFactory.getDefaultBranch();
        session.save(branch.getSection());
        session.save(branch);

        assertTrue(dao.isExist(branch.getId()));
    }

    @Test
    public void testIsBranchNameExists() {
        Branch branch1 = ObjectsFactory.getDefaultBranch();
        branch1.setName("not unique");
        Branch branch2 = ObjectsFactory.getDefaultBranch();
        branch2.setName("not unique");
        session.save(branch1.getSection());
        session.save(branch1);

        boolean result = dao.isBranchDuplicated(branch2);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsNotExist() {
     assertFalse(dao.isExist(99999L));
    }

    private int getCount() {
        return ((Number) session.createQuery("select count(*) from Branch").uniqueResult()).intValue();
    }
    
    /** Tests if Hibernate mapping allows to save branches adding them to the section. */
    @Test
    public void branchSectionBidirectionalTest() {
        Branch branch = ObjectsFactory.getDefaultBranch();
        Section section = ObjectsFactory.createSection();
        session.save(section);
        section = (Section) session.load(Section.class, section.getId());
        branch.setSection(section);
        section.getBranches().add(branch);
        session.save(branch);
        assertTrue(branch.getId() != 0);
//        session.evict(section);
        section = (Section) session.load(Section.class, section.getId());
        assertEquals(section.getBranches().size(), 1);
        assertReflectionEquals(branch, section.getBranches().get(0));
    }
}
