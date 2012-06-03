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
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.pages.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

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

    @Test
    public void testSave() {
        PoulpeUser user = ObjectsFactory.createUser();

        givenUserSavedAndEvicted(user);
        PoulpeUser savedUser = (PoulpeUser) session.get(PoulpeUser.class, user.getId());

        assertReflectionEquals(user, savedUser);
    }

    @Test
    public void testSaveIdGeneration() {
        PoulpeUser user = ObjectsFactory.createUser();
        long initialId = 0;
        user.setId(initialId);

        givenUserSavedAndEvicted(user);

        assertNotSame(user.getId(), initialId, "ID is not created");
    }

    @Test
    public void testGetPoulpeUserByUsername() {
        PoulpeUser user = ObjectsFactory.createUser();
        givenUserSavedAndEvicted(user);
        
        PoulpeUser actual = dao.getPoulpeUserByUsername(user.getUsername());
        assertReflectionEquals(actual, user);
    }

    @Test
    public void testGetByUsername() {
        PoulpeUser user = ObjectsFactory.createUser();
        givenUserSavedAndEvicted(user);
        
        User actual = dao.getByUsername(user.getUsername());
        assertReflectionEquals(actual, user);
    }

    @Test
    public void testGetByUsernamePart() {
        PoulpeUser user = ObjectsFactory.createUser();
        givenUserSavedAndEvicted(user);
        
        String part = extractPartOfUsername(user);
        List<PoulpeUser> users = dao.getPoulpeUserByUsernamePart(part);

        assertTrue(users.contains(user));
    }

    private static String extractPartOfUsername(PoulpeUser user) {
        String username = user.getUsername();
        return username.substring(0, username.length() / 2);
    }

    @Test
    public void testGetByEncodedUsername() {
        PoulpeUser user = ObjectsFactory.createUser();

        givenUserSavedAndEvicted(user);
        PoulpeUser actual = dao.getPoulpeUserByEncodedUsername(user.getEncodedUsername());

        assertReflectionEquals(actual, user);
    }

    @Test
    public void testGetAllPoulpeUsers() {
        List<PoulpeUser> users = ObjectsFactory.usersListOf(3);
        givenUsersSavedAndEvicted(users);
        List<PoulpeUser> actual = dao.getAllPoulpeUsers();

        assertContainsSameElements(actual, users);
    }
    
    @Test
    public void testGetAllPoulpeUsersPaginated_AllOnFirstPage() {
        List<PoulpeUser> users = ObjectsFactory.usersListOf(3);
        givenUsersSavedAndEvicted(users);
        List<PoulpeUser> actual = dao.getAllPoulpeUsersPaginated(Pages.paginate(1, 10));

        assertContainsSameElements(actual, users);
    }
    
    @Test
    public void testGetAllPoulpeUsersPaginated_MoreThenForOnePage() {
        List<PoulpeUser> users = ObjectsFactory.usersListOf(13);
        givenUsersSavedAndEvicted(users);
        int itemsPerPage = 10;
        
        List<PoulpeUser> actual = dao.getAllPoulpeUsersPaginated(Pages.paginate(1, itemsPerPage));
        assertContainsOnlyFristNElements(users, actual, itemsPerPage);
    }

    private static void assertContainsOnlyFristNElements(List<PoulpeUser> users, List<PoulpeUser> actual, int itemsPerPage) {
        assertContainsSameElements(actual, Iterables.limit(users, itemsPerPage));
    }
    
    @Test
    public void testGetAllBannedUsers() {
        List<PoulpeUser> bannedUsers = ObjectsFactory.bannedUsersListOf(3);

        givenUsersSavedAndEvicted(bannedUsers);
        List<PoulpeUser> actual = dao.getAllBannedUsers();

        assertContainsSameElements(actual, bannedUsers);
    }

    @Test
    public void testGetNonBannedByUsernameWithOnlyBanned() {
        List<PoulpeUser> banned = ObjectsFactory.bannedUsersListOf(3);
        givenUsersSavedAndEvicted(banned);
        
        // TODO: unclear what "" means
        List<PoulpeUser> actual = dao.getNonBannedByUsername("", 1000);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetNonBannedByUsernameWithVarious() {
        List<PoulpeUser> nonBanned = ObjectsFactory.usersListOf(3);
        List<PoulpeUser> banned = ObjectsFactory.bannedUsersListOf(3);
        
        givenUsersSavedAndEvicted(Iterables.concat(banned, nonBanned));
        
        List<PoulpeUser> actual = dao.getNonBannedByUsername("", 1000);
        assertContainsSameElements(actual, nonBanned);
    }

    @Test
    public void testGetNonBannedByUsernameWithOnlyNonBanned() {
        List<PoulpeUser> nonBanned = ObjectsFactory.usersListOf(3);
        givenUsersSavedAndEvicted(nonBanned);
        
        List<PoulpeUser> actual = dao.getNonBannedByUsername("", 1000);

        assertContainsSameElements(actual, nonBanned);
    }

    @Test
    public void testGetNonBannedByUsernameWithLimit() {
        List<PoulpeUser> nonBanned = ObjectsFactory.usersListOf(2);
        givenUsersSavedAndEvicted(nonBanned);
        
        List<PoulpeUser> actual = dao.getNonBannedByUsername("", 2);
        assertContainsSameElements(actual, nonBanned);
    }

    private void givenUserSavedAndEvicted(PoulpeUser user) {
        dao.saveOrUpdate(user);
        session.evict(user);
    }

    private void givenUsersSavedAndEvicted(Iterable<PoulpeUser> users) {
        for (PoulpeUser user : users) {
            givenUserSavedAndEvicted(user);
        }
    }

    // TODO: move away from here
    public static <T> void assertContainsSameElements(Iterable<T> first, Iterable<T> second) {
        Set<T> set1 = Sets.newLinkedHashSet(first);
        Set<T> set2 = Sets.newLinkedHashSet(second);
        assertEquals(set1, set2);
    }
}
