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

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.pages.UiPagination;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

/**
 * Tests for {@link EditGroupMembersVm}
 * 
 * @author Vyacheslav Zhivaev
 */
public class EditGroupMembersVmTest {

    // SUT
    private EditGroupMembersVm viewModel;

    @Mock GroupService groupService;
    @Mock UserService userService;
    @Mock WindowManager windowManager;
    
    private Group groupToEdit;
    private List<PoulpeUser> usersAll;
    private Set<PoulpeUser> usersSelectedInAvailable;
    private Set<PoulpeUser> usersSelectedInExist;
    private SelectedEntity<Group> selectedEntity;

    @BeforeMethod
    public void setUp() throws NotFoundException {
        MockitoAnnotations.initMocks(this);

        usersAll = TestFixtures.usersListOf(50);
        // we are assert, that half of users already in group
        List<PoulpeUser> usersAlreadyInGroup = new ArrayList<PoulpeUser>(usersAll.subList(0, usersAll.size() / 2));
        groupToEdit = createGroupWithUsers(usersAlreadyInGroup);

        usersSelectedInAvailable = Sets.newHashSet(usersAll.get(0));
        usersSelectedInExist = Sets.newHashSet(usersAlreadyInGroup.get(0));

        selectedEntity = new SelectedEntity<Group>();
        selectedEntity.setEntity(groupToEdit);

        givenGroupExistInPersistent();
        givenAvailableUsersExist();

        doNothing().when(windowManager).open(anyString());

        viewModel = new EditGroupMembersVm(windowManager, groupService, userService, selectedEntity);
        viewModel.setAvailPagination(new UiPagination());
        viewModel = spy(viewModel);

        viewModel.updateVm();

        givenUsersSelectedInView();
    }

    @Test
    public void testGetGroupToEdit() {
        assertEquals(viewModel.getGroupToEdit(), groupToEdit);
    }

    @Test
    public void testAdd() {
        viewModel.add();

        assertTrue(viewModel.getExist().containsAll(usersSelectedInAvailable));
        assertFalse(viewModel.getAvail().containsAll(usersSelectedInAvailable));
    }

    @Test
    public void testAddAll() {
        List<PoulpeUser> selected = viewModel.getAvail();

        viewModel.addAll();

        assertTrue(viewModel.getExist().containsAll(selected));
        assertTrue(viewModel.getAvail().isEmpty());
    }

    @Test
    public void testRemove() {
       givenUsersSelectedInView();

       viewModel.remove();

        assertFalse(viewModel.getExist().containsAll(usersSelectedInExist));
        assertTrue(viewModel.getAvail().containsAll(usersSelectedInExist));
    }

    @Test
    public void testRemoveAll() {
        List<PoulpeUser> selected = viewModel.getExist();

        viewModel.removeAll();

        assertTrue(viewModel.getExist().isEmpty());
        assertTrue(viewModel.getAvail().containsAll(selected));
    }

    @Test
    public void testSave() {
        viewModel.save();

        verify(groupService).saveGroup(groupToEdit);
    }

    @Test
    public void testCancel() {
        viewModel.cancel();

        vefiryNothingChanges();
    }

    @Test
    public void testGetAvailPagination(){
        int items = 100;
        UiPagination up = new  UiPagination();
        up.setItemsPerPage(items);
        viewModel.setAvailPagination(up);
        assertTrue(viewModel.getAvailPagination().getItemsPerPage()==items);
    }

    @Test
    public void testGetAvailTotalSize(){
        assertTrue(viewModel.getAvailTotalSize()>=0);
    }

    @Test
    public void testGetCommonBufferUsers(){
        List<PoulpeUser> allUser = new ArrayList<PoulpeUser>();
        allUser.addAll((List<PoulpeUser>) (List<?>) groupToEdit.getUsers());
        viewModel.setCommonBufferUsers(allUser);
        allUser = viewModel.getCommonBufferUsers();
        assertTrue(allUser.containsAll(groupToEdit.getUsers()));
    }

    @Test
    public void testItemsAvailPerPage(){
        viewModel.setItemsAvailPerPage(100);
        assertTrue(viewModel.getItemsAvailPerPage()==100);
    }

    private void vefiryNothingChanges() {
        verify(userService, never()).updateUser(any(PoulpeUser.class));

        verify(groupService, never()).saveGroup(any(Group.class));
        verify(groupService, never()).deleteGroup(any(Group.class));
    }

    private void givenGroupExistInPersistent() throws NotFoundException {
        when(groupService.get(anyLong())).thenReturn(groupToEdit);
    }

    private void givenAvailableUsersExist() {
        when(userService.withUsernamesMatching(anyString())).thenReturn(usersAll);
        when(userService.getAll()).thenReturn(usersAll);
    }

    private void givenUsersSelectedInView() {
        when(viewModel.getAvailSelected()).thenReturn(usersSelectedInAvailable);
        when(viewModel.getExistSelected()).thenReturn(usersSelectedInExist);
    }

    private Group createGroupWithUsers(List<PoulpeUser> usersInGroup) {
        Group group = TestFixtures.group();
        group.setUsers(new ArrayList<org.jtalks.common.model.entity.User>(usersInGroup));
        return group;
    }
}
