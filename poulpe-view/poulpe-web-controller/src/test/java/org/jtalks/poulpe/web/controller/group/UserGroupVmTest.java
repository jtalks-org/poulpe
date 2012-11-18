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
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import org.zkoss.zul.ListModelList;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.*;

/**
* @author Leonid Kazancev
*/
public class UserGroupVmTest {
    private static final String SEARCH_STRING = "searchString", SHOW_DELETE_CONFIRM_DIALOG = "showDeleteConfirmDialog";

    @Mock
    private GroupService groupService;
    @Mock
    private WindowManager windowManager;
    @Mock
    private BranchService branchService;
    
   
    @Mock
    private BindUtilsWrapper bindUtilsWrapper;

    private BranchGroupMap branches;
    private UserGroupVm viewModel;
    private UserGroupVm userGroupVm;
    private SelectedEntity<Group> selectedEntity;
    private Group selectedGroup;
    private ListModelList<Group> groups;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        selectedEntity = new SelectedEntity<Group>();
        userGroupVm = new UserGroupVm(groupService, selectedEntity, windowManager, branchService);

        groups = spy(new ListModelList<Group>());
        userGroupVm.setGroups(groups);

        selectedGroup = new Group();
        userGroupVm.setSelectedGroup(selectedGroup);
        viewModel = spy(userGroupVm);
    }



    @Test(dataProvider = "provideRandomGroupsList")
    public void testUpdateView(List<Group> groupList) {
        groups.add(new Group("1"));
        doReturn(groupList).when(groupService).getAll();
        viewModel.setGroups(groups);
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
    public void testDeleteNotModeratorGroup() throws NoSuchFieldException, IllegalAccessException, NotFoundException {
        givenBindWrapper();
        viewModel = spy(userGroupVm);
        doNothing().when(groupService).deleteGroup(any(Group.class));
        viewModel.deleteGroup();
        verify(bindUtilsWrapper).postNotifyChange(any(UserGroupVm.class), eq(SHOW_DELETE_CONFIRM_DIALOG));
        verify(groupService).deleteGroup(selectedGroup);
        assertNull(viewModel.getSelectedGroup());
        verify(viewModel).updateView();
        verify(viewModel).closeDialog();

    }

    @Test(dataProvider = "provideRandomGroupsList")
    public void testDeleteModeratorGroup(List notEmptyList) throws NotFoundException {
        doReturn(notEmptyList).when(groupService).getModeratedBranches(selectedGroup);
        viewModel.deleteGroup();
        verify(groupService, times(0)).deleteGroup(selectedGroup);
    }

    @Test
    public void testShowDeleteDialogWithNotModeratorGroup() {
        viewModel.setSelectedGroup(null);
        viewModel.showGroupDeleteConfirmDialog();
        assertTrue(viewModel.getModeratedBranches() == null);
        assertFalse(viewModel.isShowDeleteModeratorGroupDialog());
    }

    @Test(dataProvider = "provideRandomBranchesList")
    public void testShowDeleteDialogWithModeratorGroup(List notEmptyList) throws NoSuchFieldException, IllegalAccessException {
        doReturn(notEmptyList).when(groupService).getModeratedBranches(selectedGroup);
        givenBranches();
        viewModel  = spy(userGroupVm);
        viewModel.showGroupDeleteConfirmDialog();

        verify(viewModel).setSelectedModeratorGroupForAllBranches(selectedGroup);
         assertEquals(viewModel.getSelectedModeratorGroupForAllBranches(), selectedGroup);
        assertTrue(viewModel.isShowDeleteModeratorGroupDialog());
    }

    @Test
    public void testShowNewGroupDialog() {
        viewModel.setSelectedGroup(null);
        viewModel.showNewGroupDialog();
        assertTrue(viewModel.isShowGroupDialog());
        assertNotNull(viewModel.getSelectedGroupForDialog());
    }

    @Test
    public void testShowDeleteConfirmDialog() {
        viewModel.showGroupDeleteConfirmDialog();
        assertTrue(viewModel.isShowDeleteConfirmDialog());
    }

    @Test(dataProvider = "provideRandomBranchesAndGroupsList")
    public void testSetSelectedGroupForAllBranches(List<PoulpeBranch> branchesList, List<Group> groupsList) throws NoSuchFieldException, IllegalAccessException {
        branches = spy(new BranchGroupMap(branchesList, groupsList, branchService));
        givenBranches();

        viewModel = spy(userGroupVm);

        viewModel.setSelectedModeratorGroupForAllBranches(selectedGroup);
        viewModel.saveModeratorForBranches();
        verify(branches).setSelectedGroupForAllBranches(selectedGroup);
        assertEquals(viewModel.getSelectedModeratorGroupForAllBranches(),selectedGroup);
    }

    @Test(dataProvider = "provideRandomBranchesAndGroupsList")
    public void testSaveModeratorForBranches(List<PoulpeBranch> branchesList, List<Group> groupsList) throws NoSuchFieldException, IllegalAccessException {
        doReturn(branchesList).when(groupService).getModeratedBranches(selectedGroup);
        branches = spy(new BranchGroupMap(branchesList, groupsList,branchService));
        Group moderatorGroup = new Group("moderator");
        givenBranches();
        givenBranchesCollection();
        viewModel = spy(userGroupVm);
        viewModel.init();
        viewModel.setSelectedGroup(moderatorGroup);
        viewModel.saveModeratorForBranches();

        verify(branches).setModeratingGroupForAllBranches(moderatorGroup);
        assertFalse(viewModel.isShowDeleteModeratorGroupDialog());
    }

    @Test(dataProvider = "provideRandomBranchesAndGroupsList")
    public void testSaveModeratorForCurrentBranch(List<PoulpeBranch> branchesList, List<Group> groupsList) throws NoSuchFieldException, IllegalAccessException {
        branches = spy(new BranchGroupMap(branchesList, groupsList,branchService));
        givenBranches();
        givenBranchesCollection();
        viewModel = spy(userGroupVm);
        viewModel.init();

        Group oldModeratorGroup = groupsList.get(0);
        Group wantBecameModeratorGroup = groupsList.get(1);

        PoulpeBranch keyBranch = branchesList.get(0);
        keyBranch.setModeratorsGroup(oldModeratorGroup);
        viewModel.setSelectedGroup(oldModeratorGroup);
        viewModel.getBranches().setSelectedGroupForAllBranches(wantBecameModeratorGroup);

        viewModel.saveModeratorForCurrentBranch(keyBranch);

        verify(keyBranch).setModeratorsGroup(wantBecameModeratorGroup);
        verify(branches).setModeratingGroupForCurrentBranch(oldModeratorGroup, keyBranch);
        assertFalse(viewModel.isShowDeleteModeratorGroupDialog());
    }

    @Test
    public void testSaveGroup() throws Exception {
        Group group = new Group();
        viewModel.setSelectedGroupForDialog(group);
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

    @DataProvider
    public Object[][] provideRandomBranchesList() {
        return new Object[][]{{Arrays.asList(spy(new PoulpeBranch("2")), spy(new PoulpeBranch("3")))}};
    }


    @DataProvider
    public Object[][] provideRandomBranchesAndGroupsList() {
        List<PoulpeBranch> branchesList = Lists.newArrayList();
        branchesList.addAll(Arrays.asList(spy(new PoulpeBranch("2")), spy(new PoulpeBranch("3"))));

        List<Group> groupsList = Lists.newArrayList();
        groupsList.addAll(Arrays.asList(new Group("2"), new Group("3")));

        Object[][] branchGroup = {{branchesList,groupsList}};

        return (branchGroup);
    }




    public void  givenBindWrapper() throws NoSuchFieldException, IllegalAccessException {
        Class vm =  userGroupVm.getClass();
        Field bindWrapperField = vm.getDeclaredField("bindWrapper");
        bindWrapperField.setAccessible(true);
        bindWrapperField.set(userGroupVm, bindUtilsWrapper);
    }
    
    public void givenBranches()  throws NoSuchFieldException, IllegalAccessException {
        Class vm =  userGroupVm.getClass();
        Field bindWrapperField = vm.getDeclaredField("branches");
        bindWrapperField.setAccessible(true);
        bindWrapperField.set(userGroupVm, branches);
    }
    
    public void givenBranchesCollection() throws NoSuchFieldException, IllegalAccessException {
        Class vm = BranchGroupMap.class;
        Field bindWrapperField = vm.getDeclaredField("branchesCollection");
        bindWrapperField.setAccessible(true);
        List<ModeratingGroupComboboxRow> branchesCollection = spy((List<ModeratingGroupComboboxRow>)bindWrapperField.get(branches));
        bindWrapperField.set(branches, branchesCollection);
    }

    private Group someGroup() {
        return new Group("someGroup");
    }

    private PoulpeBranch someBranch() {
        return spy(new PoulpeBranch("someBranch"));
    }
    


}
