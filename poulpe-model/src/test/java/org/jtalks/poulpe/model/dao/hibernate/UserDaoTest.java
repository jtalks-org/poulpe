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

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
        givenUserSavedAndEvicted();

        User actual = dao.getPoulpeUserByUsername(user.getUsername());

        assertEquals(actual, user);
    }

    @Test
    public void testGetByUsernamePart() {
        user.setUsername(RandomStringUtils.randomAlphanumeric(10));

        givenUserSavedAndEvicted();

        List<User> users = dao.getPoulpeUserByUsernamePart(user.getUsername().substring(0, 8));
        assertTrue(users.contains(user));
    }

    @Test
    public void testIsUserWithUsernameExist() {
        givenUserSavedAndEvicted();

        String username = user.getUsername();

        assertTrue(dao.isUserWithUsernameExist(username));
        assertFalse(dao.isUserWithUsernameExist(username + "_"));
    }

    @Test
    public void testGetByEncodedUsername() {
        givenUserSavedAndEvicted();

        String encodedUsername = user.getEncodedUsername();

        assertEquals(dao.getPoulpeUserByEncodedUsername(encodedUsername), user);
    }

    @Test
    public void testIsUserWithEmailExist() {
        givenUserSavedAndEvicted();

        String email = user.getEmail();

        assertTrue(dao.isUserWithEmailExist(email));
        assertFalse(dao.isUserWithEmailExist(email + "_"));
    }

    /**
     * Try get all users
     */
    @Test
    public void testGetAll() {
        givenUserSavedAndEvicted();

        List<User> users = dao.getAllPoulpeUsers();

        assertTrue(users.size() == 1);
        assertTrue(users.contains(user));
    }

    private void givenUserSavedAndEvicted() {
        dao.saveOrUpdate(user);
        session.evict(user);
    }

}
