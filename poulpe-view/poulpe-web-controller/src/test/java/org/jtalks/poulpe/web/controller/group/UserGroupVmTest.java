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

import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.ListModelList;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author Leonid Kazancev
 */
public class UserGroupVmTest {
    @SuppressWarnings("JpaQlInspection")
    private static final String SEARCH_STRING = "searchString", SHOW_DELETE_CONFIRM_DIALOG = "showDeleteConfirmDialog",
            SELECTED_GROUP = "selectedGroup", MODERATING_BRANCHES = "moderatedBranches";

    @Mock
    private GroupService groupService;
    @Mock
    private WindowManager windowManager;
    @Mock
    private BindUtilsWrapper bindWrapper;

    private UserGroupVm viewModel;
    private SelectedEntity<Group> selectedEntity;
    private Group selectedGroup;
    private ListModelList<Group> groups;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        viewModel = spy(new UserGroupVm(groupService, selectedEntity, windowManager));
        selectedEntity = new SelectedEntity<Group>();
        selectedGroup = new Group();
        groups = new ListModelList<Group>();
        viewModel.setGroups(groups);
        viewModel.setSelectedGroup(selectedGroup);
        viewModel.setBindWrapper(bindWrapper);
    }

    @Test(dataProvider = "provideRandomGroupsList")
    public void testUpdateView(List<Group> groupList) {
        groups.add(new Group("1"));
        doReturn(groupList).when(groupService).getAll();

        viewModel.updateView();
        assertEquals(groups.size(), 2);
        assertSame(groups.get(0), groupList.get(0));
        assertSame(groups.get(1), groupList.get(1));
    }

    @Test
    public void testSearchGroup() {
        viewModel.setSearchString(SEARCH_STRING);
        viewModel.searchGroup();
        verify(groupService).getByName(SEARCH_STRING);
    }

    @Test
    public void testShowGroupMemberEditWindow() {
        viewModel.showGroupMemberEditWindow();
        verify(windowManager).open(EditGroupMembersVm.EDIT_GROUP_MEMBERS_URL);
    }

    @Test
    public void testDeleteNotModeratorGroup() {
        doNothing().when(groupService).deleteGroup(any(Group.class));
        doNothing().when(bindWrapper).postNotifyChange(any(), eq(SELECTED_GROUP), eq(SHOW_DELETE_CONFIRM_DIALOG));
        viewModel.deleteGroup();
        verify(groupService).deleteGroup(selectedGroup);
        verify(viewModel).updateView();
        verify(viewModel).closeDialog();
        verify(bindWrapper).postNotifyChange(any(), eq(SELECTED_GROUP), eq(SHOW_DELETE_CONFIRM_DIALOG));
    }

    @Test(dataProvider = "provideRandomGroupsList")
    public void testDeleteModeratorGroup(List notEmptyList) {
        doReturn(notEmptyList).when(groupService).getModeratedBranches(selectedGroup);
        viewModel.deleteGroup();
        verify(groupService, times(0)).deleteGroup(selectedGroup);
        verify(bindWrapper).postNotifyChange(any(), eq(MODERATING_BRANCHES));
    }

    @Test
    public void testShowDeleteDialogWithNotModeratorGroup() {
        viewModel.setSelectedGroup(null);
        viewModel.showGroupDeleteConfirmDialog();
        assertTrue(viewModel.getModeratedBranches() == null);
        assertFalse(viewModel.isShowDeleteModeratorGroupDialog());
    }

    @Test(dataProvider = "provideRandomGroupsList")
    public void testShowDeleteDialogWithModeratorGroup(List notEmptyList) {
        doReturn(notEmptyList).when(groupService).getModeratedBranches(selectedGroup);
        doNothing().when(bindWrapper).postNotifyChange(any(), eq(MODERATING_BRANCHES));
        viewModel.showGroupDeleteConfirmDialog();
        assertTrue(viewModel.isShowDeleteModeratorGroupDialog());
        verify(bindWrapper).postNotifyChange(any(), eq(MODERATING_BRANCHES));
    }

    @Test
    public void testShowNewGroupDialog() {
        viewModel.setSelectedGroup(null);
        viewModel.showNewGroupDialog();
        assertTrue(viewModel.isShowGroupDialog());
        assertNotNull(viewModel.getSelectedGroup());
    }

    @Test
    public void testShowDeleteConfirmDialog() {
        viewModel.showGroupDeleteConfirmDialog();
        assertTrue(viewModel.isShowDeleteConfirmDialog());
    }

    @Test
    public void testSaveGroup() throws Exception {
        Group group = new Group();
        viewModel.setSelectedGroup(group);
        viewModel.saveGroup();
        verify(groupService).saveGroup(group);
        verify(viewModel).updateView();
        assertFalse(viewModel.isShowGroupDialog());
        assertFalse(viewModel.isShowDeleteConfirmDialog());
    }

    @Test
    public void testOpenDialog() {
        viewModel.showNewGroupDialog();
        assertTrue(viewModel.isShowGroupDialog());
    }

    @Test
    public void testCloseDialog() {
        viewModel.showNewGroupDialog();
        viewModel.closeDialog();
        assertFalse(viewModel.isShowGroupDialog());
        assertFalse(viewModel.isShowDeleteConfirmDialog());
    }

    @Test
    public void testIsShowGroupDialog() {
        viewModel.showNewGroupDialog();
        assertTrue(viewModel.isShowGroupDialog());
        assertFalse(viewModel.isShowGroupDialog());
    }

    @Test
    public void testGetGroups() {
        assertEquals(viewModel.getGroups(), groups);
        verify(viewModel).updateView();
    }

    @Test
    public void testShowEditDialog() {
        viewModel.showEditDialog();
        assertTrue(viewModel.isShowGroupDialog());
    }


    @DataProvider
    public Object[][] provideRandomGroupsList() {
        return new Object[][]{{Arrays.asList(new Group("2"), new Group("3"))}};
    }

}
