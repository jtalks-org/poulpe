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
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Vyacheslav Zhivaev
 */
@ContextConfiguration(locations = { "classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    // SUT
    @Autowired
    private UserDao dao;

    @Autowired
    private SessionFactory sessionFactory;

    private Session session;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
    }

    /**
     * Straightforward saving of the user
     */
    @Test
    public void testSave() {
        PoulpeUser user = ObjectsFactory.createUser("testSave");

        givenUserSavedAndEvicted(user);
        PoulpeUser savedUser = (PoulpeUser) session.get(PoulpeUser.class, user.getId());

        assertReflectionEquals(user, savedUser);
    }

    @Test
    public void testSaveIdGeneration() {
        long initialId = 0;

        PoulpeUser user = ObjectsFactory.createUser("testSaveIdGeneration");
        user.setId(initialId);

        givenUserSavedAndEvicted(user);

        assertNotSame(user.getId(), initialId, "ID is not created");
    }

    @Test
    public void testGetByUsername() {
        PoulpeUser user = ObjectsFactory.createUser("testGetByUsername");

        givenUserSavedAndEvicted(user);
        PoulpeUser actual = dao.getPoulpeUserByUsername(user.getUsername());

        assertReflectionEquals(actual, user);
    }

    @Test
    public void testGetByUsernamePart() {
        String username = "testGetByUsernamePart";

        PoulpeUser user = ObjectsFactory.createUser(username);

        givenUserSavedAndEvicted(user);
        List<PoulpeUser> users = dao.getPoulpeUserByUsernamePart(username.substring(0, username.length() / 2));

        assertTrue(users.contains(user));
    }

    @Test
    public void testGetByEncodedUsername() {
        PoulpeUser user = ObjectsFactory.createUser("testGetByEncodedUsername");

        givenUserSavedAndEvicted(user);
        PoulpeUser actual = dao.getPoulpeUserByEncodedUsername(user.getEncodedUsername());

        assertReflectionEquals(actual, user);
    }

    /**
     * Try get all users
     */
    @Test
    public void testGetAllPoulpeUsers() {
        List<PoulpeUser> users = ObjectsFactory.createUsers("testGetAllPoulpeUsers1", "testGetAllPoulpeUsers2",
                "testGetAllPoulpeUsers3");

        givenUsersSavedAndEvicted(users);
        List<PoulpeUser> actual = dao.getAllPoulpeUsers();

        assertListsHasSameElements(actual, users);
    }

    @Test
    public void testGetAllBannedUsers() {
        List<PoulpeUser> bannedUsers = ObjectsFactory.createBannedUsers("testGetAllBannedUsers1", "testGetAllBannedUsers2",
                "testGetAllBannedUsers3");

        givenUsersSavedAndEvicted(bannedUsers);
        List<PoulpeUser> actual = dao.getAllBannedUsers();

        assertListsHasSameElements(actual, bannedUsers);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetNonBannedByUsernameWithOnlyBanned() {
        List<PoulpeUser> banned = ObjectsFactory.createBannedUsers("user0", "user1", "user2");

        givenUsersSavedAndEvicted(banned);
        List<PoulpeUser> actual = dao.getNonBannedByUsername("", 1000);

        assertListsHasSameElements(actual, ListUtils.EMPTY_LIST);
    }

    @Test
    public void testGetNonBannedByUsernameWithVarious() {
        List<PoulpeUser> nonBanned = ObjectsFactory.createUsers("user3", "user4", "user5");

        @SuppressWarnings("unchecked")
        List<PoulpeUser> all = ListUtils.union(ObjectsFactory.createBannedUsers("user6", "user7", "user8"), nonBanned);

        givenUsersSavedAndEvicted(all);
        List<PoulpeUser> actual = dao.getNonBannedByUsername("", 1000);

        assertListsHasSameElements(actual, nonBanned);
    }

    @Test
    public void testGetNonBannedByUsernameWithOnlyNonBanned() {
        List<PoulpeUser> nonBanned = ObjectsFactory.createUsers("user9", "user10", "user11");

        givenUsersSavedAndEvicted(nonBanned);
        List<PoulpeUser> actual = dao.getNonBannedByUsername("", 1000);

        assertListsHasSameElements(actual, nonBanned);
    }

    @Test
    public void testGetNonBannedByUsernameWithLimit() {
        List<PoulpeUser> nonBanned = ObjectsFactory.createUsers("user9", "user10", "user11");

        givenUsersSavedAndEvicted(nonBanned);
        List<PoulpeUser> actual = dao.getNonBannedByUsername("", 2);

        assertEquals(actual.size(), 2);
        assertTrue(nonBanned.containsAll(actual));
    }

    private void givenUserSavedAndEvicted(PoulpeUser user) {
        dao.saveOrUpdate(user);
        session.evict(user);
    }

    private void givenUsersSavedAndEvicted(List<PoulpeUser> users) {
        for (PoulpeUser user : users) {
            givenUserSavedAndEvicted(user);
        }
    }

    public static <T> void assertListsHasSameElements(List<T> first, List<T> second) {
        assertEquals(first.size(), second.size());
        assertTrue(first.containsAll(second));
    }
}
