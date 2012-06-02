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

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationException;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.entity.TopicType;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

/**
 * Test for {@link TransactionalGroupService}
 */
public class TransactionalGroupServiceTest {
    private TransactionalGroupService service;

    @Mock
    GroupDao dao;
    @Mock
    EntityValidator entityValidator;

    private Group group = new Group("new group");

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        service = new TransactionalGroupService(dao, entityValidator);
    }

    @Test
    public void deleteGroup() {
        service.deleteGroup(group);
        verify(dao).delete(group);
    }

    @Test
    public void getAll() {
        service.getAll();
        verify(dao).getAll();
    }

    @Test
    public void testGetBannedUsersGroup() throws Exception {
        Group expectedGroup = new Group();
        doReturn(Arrays.asList(expectedGroup)).when(dao).getMatchedByName("Banned Users");
        Group bannedUsersGroup = service.getBannedUsersGroup();
        assertSame(bannedUsersGroup, expectedGroup);
    }

    @Test
    public void testGetBannedUsersGroup_withEmpty() throws Exception {
        doReturn(new ArrayList()).when(dao).getMatchedByName("Banned Users");
        Group bannedUsersGroup=service.getBannedUsersGroup();
        verify(dao).saveOrUpdate(any(Group.class));
        assertEquals(bannedUsersGroup.getName(),"Banned Users");
    }

    @Test
    public void getAllMatchedByName() {
        service.getAllMatchedByName("name");
        verify(dao).getMatchedByName("name");
    }

    @Test
    public void saveGroup() {
        service.saveGroup(group);
        verifyEntityValidated();
    }

    private void verifyEntityValidated() {
        verify(entityValidator).throwOnValidationFailure(group);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void saveNotValidGroup() {
        givenConstraintsViolations();
        service.saveGroup(group);
    }

    private void givenConstraintsViolations() {
        Set<ValidationError> dontCare = Collections.emptySet();
        doThrow(new ValidationException(dontCare)).when(entityValidator).throwOnValidationFailure(any(TopicType.class));
    }
}
