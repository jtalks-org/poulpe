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
package org.jtalks.poulpe.web.controller.group;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectsFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

/**
 * Tests for {@link EditGroupMembersVM}
 * 
 * @author Vyacheslav Zhivaev
 * 
 */
public class EditGroupMembersVMTest {

    // SUT
    private EditGroupMembersVM viewModel;

    private PoulpeGroup groupToEdit;
    private List<User> usersAvailable;
    private Set<User> usersSelectedInAvailable;
    private Set<User> usersSelectedInExist;
    private SelectedEntity<PoulpeGroup> selectedEntity;

    @Mock
    private GroupService groupService;
    @Mock
    private UserService userService;
    @Mock
    private WindowManager windowManager;

    @BeforeMethod
    public void setUp() throws NotFoundException {
        MockitoAnnotations.initMocks(this);

        usersAvailable = ObjectsFactory.getFakeUsers(50);
        // we are assert, that half of users already in group
        List<User> usersAlreadyInGroup = usersAvailable.subList(0, usersAvailable.size() / 2);
        groupToEdit = createGroupWithUsers(usersAlreadyInGroup);

        usersSelectedInAvailable = Sets.newHashSet(usersAvailable.get(0));
        usersSelectedInExist = Sets.newHashSet(usersAlreadyInGroup.get(0));

        selectedEntity = new SelectedEntity<PoulpeGroup>();
        selectedEntity.setEntity(groupToEdit);

        givenGroupExistInPersistent();
        givenAvailableUsersExist();

        doNothing().when(windowManager).open(anyString());

        viewModel = new EditGroupMembersVM(windowManager, groupService, userService, selectedEntity);
        viewModel = spy(viewModel);

        // givenUsersSelectedInView();
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#add()}.
     */
    @Test
    public void testAdd() {
        viewModel.add();

        assertTrue(viewModel.getExist().containsAll(usersSelectedInAvailable));
        assertFalse(viewModel.getAvail().containsAll(usersSelectedInAvailable));
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#addAll()}.
     */
// @Test
    // FIXME
    public void testAddAll() {
        List<User> selected = viewModel.getAvail();

        viewModel.addAll();

        assertTrue(viewModel.getExist().containsAll(selected));
        assertTrue(viewModel.getAvail().isEmpty());
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#remove()}.
     */
    // FIXME
    // @Test
    public void testRemove() {
        givenUsersSelectedInView();

        viewModel.remove();

        assertFalse(viewModel.getExist().containsAll(usersSelectedInExist));
        assertTrue(viewModel.getAvail().containsAll(usersSelectedInExist));
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#removeAll()}.
     */
    // FIXME
    // @Test
    public void testRemoveAll() {
        List<User> selected = viewModel.getExist();

        viewModel.removeAll();

        assertTrue(viewModel.getExist().isEmpty());
        assertTrue(viewModel.getAvail().containsAll(selected));
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#save()}.
     */
    @Test
    public void testSave() {
        viewModel.save();

        verify(groupService).saveGroup(groupToEdit);
    }

    /**
     * Test method for {@link org.jtalks.poulpe.web.controller.group.EditGroupMembersVM#cancel()}.
     */
    @Test
    public void testCancel() {
        viewModel.cancel();

        vefiryNothingChanges();
    }

    private void vefiryNothingChanges() {
        verify(userService, never()).setPermanentBanStatus(anyCollectionOf(User.class), anyBoolean(), anyString());
        verify(userService, never()).setTemporaryBanStatus(anyCollectionOf(User.class), anyInt(), anyString());
        verify(userService, never()).updateUser(any(User.class));

        verify(groupService, never()).saveGroup(any(PoulpeGroup.class));
        verify(groupService, never()).deleteGroup(any(PoulpeGroup.class));
    }

    private void givenGroupExistInPersistent() throws NotFoundException {
        when(groupService.get(anyLong())).thenReturn(groupToEdit);
    }

    private void givenAvailableUsersExist() {
        when(userService.getUsersByUsernameWord(anyString())).thenReturn(usersAvailable);
        when(userService.getAll()).thenReturn(usersAvailable);
    }

    private void givenUsersSelectedInView() {
        when(viewModel.getAvailSelected()).thenReturn(usersSelectedInAvailable);
        when(viewModel.getExistSelected()).thenReturn(usersSelectedInExist);
    }

    private PoulpeGroup createGroupWithUsers(List<User> usersInGroup) {
        PoulpeGroup group = new PoulpeGroup(RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(20));
        group.setPoulpeUsers(usersInGroup);
        return group;
    }

}
