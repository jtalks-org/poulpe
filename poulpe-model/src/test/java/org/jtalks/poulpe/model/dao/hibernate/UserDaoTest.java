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

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link UserDao}
 *
 * @author Vyacheslav Zhivaev
 *
 */
@ContextConfiguration(locations = { "classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    // SUT
    @Autowired
    private UserDao dao;

    @Autowired
    private SessionFactory sessionFactory;

    private User user;
    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        user = ObjectsFactory.createUser();
        session = sessionFactory.getCurrentSession();
    }

    /**
     * Straightforward saving of the user
     */
    @Test
    public void testSave() {
        dao.saveOrUpdate(user);
        assertNotSame(user.getId(), 0, "ID is not created");

        session.evict(user);

        User savedUser = (User) session.get(User.class, user.getId());
        assertReflectionEquals(user, savedUser);
    }

    @Test
    public void testGetByUsername() {
        givenUserSavedAndEvicted(user);

        User actual = dao.getPoulpeUserByUsername(user.getUsername());

        assertEquals(actual, user);
    }

    @Test
    public void testGetByUsernamePart() {
        user.setUsername(RandomStringUtils.randomAlphanumeric(10));

        givenUserSavedAndEvicted(user);

        List<User> users = dao.getPoulpeUserByUsernamePart(user.getUsername().substring(0, 8));
        assertTrue(users.contains(user));
    }

    @Test
    public void testGetByEncodedUsername() {
        givenUserSavedAndEvicted(user);

        String encodedUsername = user.getEncodedUsername();

        assertEquals(dao.getPoulpeUserByEncodedUsername(encodedUsername), user);
    }

    /**
     * Try get all users
     */
    @Test
    public void testGetAll() {
        givenUserSavedAndEvicted(user);

        List<User> users = dao.getAllPoulpeUsers();

        assertTrue(users.size() == 1);
        assertTrue(users.contains(user));
    }
    
    @Test
    public void testGetAllBannedUsers() {
    	User bannedUser = ObjectsFactory.createUser();
    	bannedUser.setBanExpirationDate(new DateTime());
    	bannedUser.setBanReason("any reason");
    	
    	dao.saveOrUpdate(bannedUser);
        session.evict(bannedUser);
    	
    	List<User> users = dao.getAllBannedUsers();
    	
    	assertTrue(users.size() == 1);
        assertTrue(users.contains(bannedUser));
    }

    private void givenUserSavedAndEvicted(User user) {
        dao.saveOrUpdate(user);
        session.evict(user);
    }

}
