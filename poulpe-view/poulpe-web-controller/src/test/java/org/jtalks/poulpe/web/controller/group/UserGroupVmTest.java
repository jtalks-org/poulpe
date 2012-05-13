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
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.ListModelList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Leonid Kazancev
 */
public class UserGroupVmTest {

    private static final String SEARCH_STRING = "searchString";

    @Mock
    private GroupService groupService;
    @Mock
    private WindowManager windowManager;
    @Mock
    private DialogManager.Performable preformable;

    private UserGroupVm viewModel;
    private SelectedEntity<Group> selectedEntity;
    private Group selectedGroup;
    private ListModelList<Group> groups;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        viewModel = new UserGroupVm(groupService, selectedEntity, windowManager);
        selectedEntity = new SelectedEntity<Group>();
        selectedGroup = new Group();
        groups = spy(new ListModelList<Group>());
        viewModel.setGroups(groups);
        viewModel.setSelectedGroup(selectedGroup);
    }

    @Test
    public void testUpdateView() {
        viewModel.updateView();
        verify(groups).clear();
        verify(groups).addAll(groupService.getAll());
    }

    @Test
    public void testSearchGroup() {
        viewModel.setSearchString(SEARCH_STRING);
        viewModel.searchGroup();
        verify(groupService).getAllMatchedByName(SEARCH_STRING);
    }

    @Test
    public void testShowGroupMemberEditWindow() {
        viewModel.showGroupMemberEditWindow();
        verify(windowManager).open(UserGroupVm.EDIT_GROUP_MEMBERS_URL);
    }

    @Test
    public void testDeleteGroup() {
        doNothing().when(groupService).deleteGroup(any(Group.class));
        viewModel.deleteGroup();
        verify(groupService).deleteGroup(selectedGroup);
        assert !((viewModel.isShowDeleteDialog()) && (viewModel.isShowEditDialog()) && (viewModel.isShowNewDialog()));
    }

    @Test
    public void testAddNewGroup() {
        viewModel.addNewGroup();
        assert (viewModel.isShowNewDialog());
    }

    @Test
    public void testSaveGroup() throws Exception {
        Group group = new Group();

        viewModel.saveGroup(group);
        verify(groupService).saveGroup(group);
        assert !((viewModel.isShowDeleteDialog()) && (viewModel.isShowEditDialog()) && (viewModel.isShowNewDialog()));
    }

    @Test
    public void testCloseDialog(){
        viewModel.closeDialog();
        assert !((viewModel.isShowDeleteDialog()) && (viewModel.isShowEditDialog()) && (viewModel.isShowNewDialog()));
    }

}
