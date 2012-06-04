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
 * @author Alexey Grigorev
 */
@ContextConfiguration(locations = { "classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {
    static final String NO_FILTER = "";
    
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

        saveAndEvict(user);
        PoulpeUser savedUser = (PoulpeUser) session.get(PoulpeUser.class, user.getId());

        assertReflectionEquals(user, savedUser);
    }

    @Test
    public void testSaveIdGeneration() {
        PoulpeUser user = ObjectsFactory.createUser();
        long initialId = 0;
        user.setId(initialId);

        saveAndEvict(user);

        assertNotSame(user.getId(), initialId, "ID is not created");
    }

    @Test
    public void testGetByUsername() {
        PoulpeUser user = ObjectsFactory.createUser();
        saveAndEvict(user);
        
        User actual = dao.getByUsername(user.getUsername());
        assertReflectionEquals(actual, user);
    }

    @Test
    public void findPoulpeUsersPaginated_withNoPagination() {
        PoulpeUser user = ObjectsFactory.createUser();
        saveAndEvict(user);
        
        String part = extractPartOfUsername(user);
        List<PoulpeUser> users = dao.findPoulpeUsersPaginated(part, Pages.NONE);

        assertTrue(users.contains(user));
    }

    private static String extractPartOfUsername(PoulpeUser user) {
        String username = user.getUsername();
        return username.substring(0, username.length() / 2);
    }
    
    @Test
    public void findPoulpeUsersPaginated_withPagination() {
        String startsWith = "SomeString";
        givenMoreWithOnePage(startsWith);
        
        int limit = 10;
        List<PoulpeUser> users = dao.findPoulpeUsersPaginated(startsWith, Pages.paginate(1, limit));

        assertEquals(users.size(), limit);
    }

    private void givenMoreWithOnePage(String startsWith) {
        int n = 20;
        
        while (n > 0) {
            PoulpeUser user = ObjectsFactory.createUser(startsWith + n);
            saveAndEvict(user);
            n--;
        }
    }

    @Test
    public void findPoulpeUsersPaginated_noFilterAndNoPagination() {
        List<PoulpeUser> users = ObjectsFactory.usersListOf(3);
        saveAndEvict(users);
        List<PoulpeUser> actual = dao.findPoulpeUsersPaginated(NO_FILTER, Pages.NONE);

        assertContainsSameElements(actual, users);
    }
    
    @Test
    public void findPoulpeUsersPaginated_noFilterAndAllOnFirstPage() {
        List<PoulpeUser> users = ObjectsFactory.usersListOf(3);
        saveAndEvict(users);
        
        List<PoulpeUser> actual = dao.findPoulpeUsersPaginated(NO_FILTER, Pages.paginate(1, 10));

        assertContainsSameElements(actual, users);
    }
    
    @Test
    public void findPoulpeUsersPaginated_noFilterAndMoreThanOnePage() {
        List<PoulpeUser> users = ObjectsFactory.usersListOf(13);
        saveAndEvict(users);
        
        int limit = 10;
        List<PoulpeUser> actual = dao.findPoulpeUsersPaginated(NO_FILTER, Pages.paginate(1, limit));
        
        assertEquals(actual.size(), limit);
    }

    
    @Test
    public void getAllUsersCount() {
        int count = 13;
        List<PoulpeUser> users = ObjectsFactory.usersListOf(count);
        saveAndEvict(users);
        
        int actual = dao.countUsernameMatches(NO_FILTER);
        assertEquals(actual, count);
    }
    
    @Test
    public void testGetAllBannedUsers() {
        List<PoulpeUser> bannedUsers = ObjectsFactory.bannedUsersListOf(3);

        saveAndEvict(bannedUsers);
        List<PoulpeUser> actual = dao.getAllBannedUsers();

        assertContainsSameElements(actual, bannedUsers);
    }

    @Test
    public void getNonBannedByUsername_withOnlyBannedUsers() {
        List<PoulpeUser> banned = ObjectsFactory.bannedUsersListOf(3);
        saveAndEvict(banned);
        
        List<PoulpeUser> actual = dao.getNonBannedByUsername(NO_FILTER, 1000);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void getNonBannedByUsername_withBothKindsOfUsers() {
        List<PoulpeUser> nonBanned = ObjectsFactory.usersListOf(3);
        List<PoulpeUser> banned = ObjectsFactory.bannedUsersListOf(3);
        
        saveAndEvict(Iterables.concat(banned, nonBanned));
        
        List<PoulpeUser> actual = dao.getNonBannedByUsername(NO_FILTER, 1000);
        assertContainsSameElements(actual, nonBanned);
    }

    @Test
    public void getNonBannedByUsername_withBannedUsers() {
        List<PoulpeUser> nonBanned = ObjectsFactory.usersListOf(3);
        saveAndEvict(nonBanned);
        
        List<PoulpeUser> actual = dao.getNonBannedByUsername(NO_FILTER, 1000);

        assertContainsSameElements(actual, nonBanned);
    }

    @Test
    public void getNonBannedByUsername_withLimit() {
        List<PoulpeUser> nonBanned = ObjectsFactory.usersListOf(10);
        saveAndEvict(nonBanned);
        
        int limit = 2;
        List<PoulpeUser> actual = dao.getNonBannedByUsername(NO_FILTER, limit);
        assertEquals(actual.size(), limit);
    }

    private void saveAndEvict(PoulpeUser user) {
        dao.saveOrUpdate(user);
        session.evict(user);
    }

    private void saveAndEvict(Iterable<PoulpeUser> users) {
        for (PoulpeUser user : users) {
            saveAndEvict(user);
        }
    }

    // TODO: move away from here
    public static <T> void assertContainsSameElements(Iterable<T> first, Iterable<T> second) {
        Set<T> set1 = Sets.newLinkedHashSet(first);
        Set<T> set2 = Sets.newLinkedHashSet(second);
        assertEquals(set1, set2);
    }
}
