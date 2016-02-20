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
package org.jtalks.poulpe.service.transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.logic.UserBanner;
import org.jtalks.poulpe.model.pages.Pages;
import org.jtalks.poulpe.model.sorting.UserSearchRequest;
import org.jtalks.poulpe.service.exceptions.ValidationException;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.Validator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.jtalks.poulpe.model.sorting.UserSearchRequest.BY_USERNAME;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Class for testing {@code TransactionalUserService} functionality.
 *
 * @author Guram Savinov
 * @author Vyacheslav Zhivaev
 * @author maxim reshetov
 */

public class TransactionalUserServiceTest {
    private static final String USERNAME = "username";
    private static final String HASED_PASSWORD = "password";
    private static final PoulpeUser POULPE_USER = new PoulpeUser(USERNAME, "email", HASED_PASSWORD, "salt");
    public static final PoulpeUser ANOTHER_USER = new PoulpeUser(
            USERNAME.toUpperCase(), "email", HASED_PASSWORD, "salt"
    );

    // sut
    private TransactionalUserService userService;

    // dependencies
    private UserDao userDao;

    final String searchString = "searchString";
    private ComponentDao componentDaoMock;
    private AclManager aclManagerMock;
    private Validator validator;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDao = mock(UserDao.class);
        componentDaoMock = mock(ComponentDao.class);
        aclManagerMock = mock(AclManager.class);
        validator = mock(Validator.class);
        userService = new TransactionalUserService(userDao, mock(UserBanner.class), aclManagerMock, componentDaoMock, validator);
    }

    @Test
    public void getAll() {
        String NO_FILTER = "";
        userService.getAll();
        verify(userDao).findPoulpeUsersPaginated(NO_FILTER, Pages.NONE);
    }

    @Test
    public void countUsernameMatches() {
        userService.countUsernameMatches(searchString);
        verify(userDao).countUsernameMatches(searchString);
    }

    @Test
    public void findUsersPaginated() {
        int page = 1, limit = 10;
        userService.findUsersPaginated(searchString, page, limit);
        verify(userDao).findPoulpeUsersPaginated(searchString, Pages.paginate(page, limit));
    }

    @Test
    public void testFindUsersBySearchRequest(){
        int page = 1, limit = 10;
        UserSearchRequest request = new UserSearchRequest(true,Pages.paginate(page, limit),BY_USERNAME,"");
        userService.findUsersBySearchRequest(request);
        verify(userDao).findPoulpeUsersBySearchRequest(request);
    }

    @Test
    public void testGetUsersByUsernameWord() {
        userService.withUsernamesMatching(searchString);
        verify(userDao).findPoulpeUsersPaginated(searchString, Pages.NONE);
    }

    @Test
    public void testUpdateUser() {
        PoulpeUser user = user();
        userService.updateUser(user);
        verify(userDao).saveOrUpdate(user);
    }

    @Test
    public void TestFindUsersNotInList(){
        List<PoulpeUser> users = new ArrayList<PoulpeUser>();
        users.add(user());
        userService.findUsersNotInList(searchString, users);
        verify(userDao).findUsersNotInList(searchString, users, Pages.NONE);
    }

    @Test
    public void testGetAllBannedUsers() {
        List<PoulpeUser> bannedUsers = userService.getAllBannedUsers();
        assertEquals(bannedUsers, new ArrayList<PoulpeUser>());
    }

    @Test
    public void testGetNonBannedByUsername() {
        List<PoulpeUser> nonBannedUsers = userService.loadNonBannedUsersByUsername(searchString, Pages.paginate(0, 1000));
        assertEquals(nonBannedUsers, new ArrayList<PoulpeUser>());
    }

    @Test
    public void accessNotAllowedBecauseNoComponentTypeRegistered() {
        ComponentType componentType = ComponentType.FORUM;

        when(componentDaoMock.getByType(eq(componentType))).thenReturn(null);

        assertFalse(userService.accessAllowedToComponentType(USERNAME, componentType));
    }

    @Test
    public void accessNotAllowedBecauseNoGroupPermissionsAssociatedWithComponent() {
        ComponentType componentType = ComponentType.FORUM;
        Component component = mock(Component.class);

        when(componentDaoMock.getByType(eq(componentType))).thenReturn(component);
        when(aclManagerMock.getGroupPermissionsOn(eq(component))).thenReturn(Collections.<GroupAce>emptyList());

        assertFalse(userService.accessAllowedToComponentType(USERNAME, componentType));
    }

    @Test
    public void accessNotAllowedBecausePermissionNotFound() {
        ComponentType componentType = ComponentType.FORUM;
        Component component = mock(Component.class);

        when(userDao.getByUsername(eq(USERNAME))).thenReturn(createPoulpeUserWithPredefinedNameAndGroups(Collections.<Group>emptyList()));
        when(componentDaoMock.getByType(eq(componentType))).thenReturn(component);
        when(aclManagerMock.getGroupPermissionsOn(eq(component))).thenReturn(Collections.<GroupAce>emptyList());

        assertFalse(userService.accessAllowedToComponentType(USERNAME, componentType));
    }

    @Test
    public void accessNotAllowedBecausePermissionIsNotGranting() {
        long groupId = 42L;

        ComponentType componentType = ComponentType.FORUM;
        Component component = mock(Component.class);
        GroupAce groupAce = mock(GroupAce.class);

        Group group = createGroupWithId(groupId);
        when(groupAce.getGroupId()).thenReturn(groupId);

        when(userDao.getByUsername(eq(USERNAME))).thenReturn(createPoulpeUserWithPredefinedNameAndGroups(singletonList(group)));
        when(componentDaoMock.getByType(eq(componentType))).thenReturn(component);
        when(aclManagerMock.getGroupPermissionsOn(eq(component))).thenReturn(singletonList(groupAce));
        when(groupAce.isGranting()).thenReturn(false);

        assertFalse(userService.accessAllowedToComponentType(USERNAME, componentType));
    }

    @Test
    public void accessNotAllowedBecauseAtLeastOneOfTheGroupsIsRestrictiong(){
        long groupId = 42L;
        long groupId2 = 666L;

        ComponentType componentType = ComponentType.FORUM;
        Component component = mock(Component.class);
        GroupAce groupAce = mock(GroupAce.class);
        GroupAce groupAce2 = mock(GroupAce.class);

        Group group = createGroupWithId(groupId);
        Group group2 = createGroupWithId(groupId2);

        when(groupAce.getGroupId()).thenReturn(groupId);
        when(groupAce2.getGroupId()).thenReturn(groupId2);

        when(userDao.getByUsername(eq(USERNAME))).thenReturn(createPoulpeUserWithPredefinedNameAndGroups(asList(group, group2)));
        when(componentDaoMock.getByType(eq(componentType))).thenReturn(component);
        when(aclManagerMock.getGroupPermissionsOn(eq(component))).thenReturn(asList(groupAce, groupAce2));
        when(groupAce.isGranting()).thenReturn(true);
        when(groupAce2.isGranting()).thenReturn(false);

        assertFalse(userService.accessAllowedToComponentType(USERNAME, componentType));
    }

    @Test
    public void accessAllowed() {
        long groupId = 42L;

        ComponentType componentType = ComponentType.FORUM;
        Component component = mock(Component.class);
        GroupAce groupAce = mock(GroupAce.class);
        Group group = createGroupWithId(groupId);

        when(groupAce.getGroupId()).thenReturn(groupId);
        when(userDao.getByUsername(eq(USERNAME))).thenReturn(createPoulpeUserWithPredefinedNameAndGroups(singletonList(group)));
        when(componentDaoMock.getByType(eq(componentType))).thenReturn(component);
        when(aclManagerMock.getGroupPermissionsOn(eq(component))).thenReturn(singletonList(groupAce));
        when(groupAce.isGranting()).thenReturn(true);

        assertTrue(userService.accessAllowedToComponentType(USERNAME, componentType));
    }

    @Test
    public void testUpdateUsers(){
        doNothing().when(userDao).saveOrUpdate((PoulpeUser)any());
        Group group = createGroupWithId(1);
        List<User> users = new ArrayList<User>();
        for(int i=0; i<5; i++){
            users.add(user());
        }
        group.setUsers(users);
        userService.updateUsersAtGroup(users, group);
        for(User u :users){
            assertEquals(u.getGroups().get(0),group);
        }

        users = new ArrayList<User>();
        for(int i=0; i<5; i++){
            users.add(user());
            users.get(i).getGroups().add(group);
        }
        group.setUsers(new ArrayList<User>());
        userService.updateUsersAtGroup(users, group);
        for(User u :users){
            assertTrue(u.getGroups().isEmpty());
        }
    }
    
    @Test
    public void authenticate() throws NotFoundException {
        when(userDao.findPoulpeUsersBySearchRequest(any(UserSearchRequest.class)))
                .thenReturn(Collections.singletonList(POULPE_USER));

        assertEquals(userService.authenticate(USERNAME, HASED_PASSWORD), POULPE_USER);

        verify(userDao).findPoulpeUsersBySearchRequest(any(UserSearchRequest.class));
        verify(userDao, never()).getByUsername(eq(USERNAME));
    }

    @Test
    public void authenticateCaseSensitise() throws NotFoundException {
        when(userDao.findPoulpeUsersBySearchRequest(any(UserSearchRequest.class)))
                .thenReturn(Arrays.asList(POULPE_USER, ANOTHER_USER));

        assertEquals(userService.authenticate(USERNAME, HASED_PASSWORD), POULPE_USER);

        verify(userDao).findPoulpeUsersBySearchRequest(any(UserSearchRequest.class));
    }

    @Test
    public void authenticateCaseSensitise_whenUsernameNotFound() throws NotFoundException {
        String searchUsername = StringUtils.capitalize(USERNAME);
        when(userDao.findPoulpeUsersBySearchRequest(any(UserSearchRequest.class)))
                .thenReturn(Arrays.asList(POULPE_USER, ANOTHER_USER));

        try {
            userService.authenticate(searchUsername, HASED_PASSWORD);
            fail("NotFoundException should be thrown when case sensitive username is not found");
        } catch (NotFoundException e) {
        }
        verify(userDao).findPoulpeUsersBySearchRequest(any(UserSearchRequest.class));
    }

    @Test
        public void authenticate_whenUsernameNotFound() throws NotFoundException {
        when(userDao.findPoulpeUsersBySearchRequest(any(UserSearchRequest.class)))
                .thenReturn(Collections.<PoulpeUser>emptyList());

        try {
            userService.authenticate(USERNAME, HASED_PASSWORD);
            fail("NotFoundException should be thrown when case insensitive username is not found");
        } catch (NotFoundException e) {
        }

        verify(userDao).findPoulpeUsersBySearchRequest(any(UserSearchRequest.class));
    }

    @Test
    public void authenticate_whenPasswordNotMatch() throws NotFoundException {
        when(userDao.findPoulpeUsersBySearchRequest(any(UserSearchRequest.class)))
                .thenReturn(Collections.singletonList(POULPE_USER));

        try {
            userService.authenticate(USERNAME, "notMatchPassword");
            fail("NotFoundException should be thrown when provided password doesn't match required one");
        } catch (NotFoundException e) {
        }
        verify(userDao).findPoulpeUsersBySearchRequest(any(UserSearchRequest.class));
    }

    @Test
    public void testRegistration() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(null);
        when(userDao.getByEmail(any(String.class))).thenReturn(null);
        doNothing().when(userDao).save(any(PoulpeUser.class));
        userService.registration(user());
    }

    @Test(expectedExceptions = org.springframework.transaction.NoTransactionException.class)
    public void testDryRunRegistration() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(null);
        when(userDao.getByEmail(any(String.class))).thenReturn(null);
        doNothing().when(userDao).save(any(PoulpeUser.class));
        userService.dryRunRegistration(user());
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testRegistrationUsernameAlreadyExist() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(user());
        when(userDao.getByEmail(any(String.class))).thenReturn(null);
        doNothing().when(userDao).save(any(PoulpeUser.class));

        userService.registration(user());
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testDryRunRegistrationUsernameAlreadyExist() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(user());
        when(userDao.getByEmail(any(String.class))).thenReturn(null);
        doNothing().when(userDao).save(any(PoulpeUser.class));

        userService.dryRunRegistration(user());
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testRegistrationEmailAlreadyExist() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(null);
        when(userDao.getByEmail(any(String.class))).thenReturn(user());
        doNothing().when(userDao).save(any(PoulpeUser.class));

        userService.registration(user());
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testDryRunRegistrationEmailAlreadyExist() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(null);
        when(userDao.getByEmail(any(String.class))).thenReturn(user());
        doNothing().when(userDao).save(any(PoulpeUser.class));

        userService.dryRunRegistration(user());
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testRegistrationAnyValidationErrors() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(null);
        when(userDao.getByEmail(any(String.class))).thenReturn(null);

        ConstraintViolationException ex = new ConstraintViolationException(constraintViolations());
        doThrow(ex).when(userDao).save(any(PoulpeUser.class));

        userService.registration(user());
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testDryRunRegistrationAnyValidationErrors() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(null);
        when(userDao.getByEmail(any(String.class))).thenReturn(null);

        ConstraintViolationException ex = new ConstraintViolationException(constraintViolations());
        doThrow(ex).when(userDao).save(any(PoulpeUser.class));

        userService.dryRunRegistration(user());
    }

    private Group createGroupWithId(long groupId) {
        Group group = new Group();
        group.setId(groupId);
        return group;
    }

    private PoulpeUser createPoulpeUserWithPredefinedNameAndGroups(List<Group> groups) {
        PoulpeUser user = new PoulpeUser();
        user.setUsername(USERNAME);
        user.setGroups(groups);
        return user;
    }


    public static PoulpeUser user() {
        return new PoulpeUser(RandomStringUtils.randomAlphanumeric(10), "USERNAME@mail.com", "PASSWORD", "salt");
    }

    private static Set<ConstraintViolation<?>> constraintViolations(){
        Set<ConstraintViolation<?>> result = new HashSet<ConstraintViolation<?>>();
        for(int i=1; i<=5; i++){
            result.add(new ConstraintViolationImpl("message"+i,"message"+i,PoulpeUser.class,null,null,null,null,null,null));
        }
        return result;
    }
}
