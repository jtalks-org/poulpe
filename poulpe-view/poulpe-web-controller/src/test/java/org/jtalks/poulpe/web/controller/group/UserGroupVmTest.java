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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
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

import java.util.ArrayList;
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
    private static final String SEARCH_STRING = "searchString", MODERAING_GROUP_ID = "moderating_group_id",
            QUERY = "from PoulpeBranch where MODERATORS_GROUP_ID=:", MODERATING_BRANCHES = "moderatingBranches";

    @Mock
    private GroupService groupService;
    @Mock
    private WindowManager windowManager;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private BindUtilsWrapper bindWrapper;


    private Session session;
    private Query query;

    private UserGroupVm viewModel;
    private SelectedEntity<Group> selectedEntity;
    private Group selectedGroup;
    private ListModelList<Group> groups;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        viewModel = spy(new UserGroupVm(groupService, selectedEntity, windowManager, sessionFactory));
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
        doReturn(new ArrayList(0)).when(viewModel).getModeratingBranches();
        viewModel.deleteGroup();
        verify(groupService).deleteGroup(selectedGroup);
        verify(viewModel).updateView();
    }

    @Test(dataProvider = "provideRandomGroupsList")
    public void testDeleteModeratorGroup(List notEmptyList) {
        doReturn(notEmptyList).when(viewModel).getModeratingBranches();
        doNothing().when(bindWrapper).postNotifyChange(any(), eq(MODERATING_BRANCHES));
        viewModel.deleteGroup();
        assertTrue(viewModel.isShowModeratingBranches());
        verify(bindWrapper).postNotifyChange(any(), eq(MODERATING_BRANCHES));

    }

    @Test
    public void getModeratingBranchesWithNullGroup() {
        viewModel.setSelectedGroup(null);
        assertTrue(viewModel.getModeratingBranches() == null);
    }

    @Test
    public void getModeratingBranchesWithNotNullGroup() throws Exception {
        viewModel.setSelectedGroup(moderatorGroup());
        doReturn(preparedSession()).when(sessionFactory).getCurrentSession();
        viewModel.getModeratingBranches();
        verify(session).flush();
        verify(session).createQuery(QUERY + MODERAING_GROUP_ID);
        verify(query).setParameter(MODERAING_GROUP_ID, selectedGroup.getId());
        verify(query).list();
    }

    @Test
    public void testShowNewGroupDialog() {
        viewModel.showNewGroupDialog();
        assertTrue(viewModel.isShowGroupDialog());
    }

    @Test
    public void testSaveGroup() throws Exception {
        Group group = new Group();
        viewModel.setSelectedGroup(group);
        viewModel.saveGroup();
        verify(groupService).saveGroup(group);
        verify(viewModel).updateView();
        assertFalse(viewModel.isShowGroupDialog());
        assertFalse(viewModel.isShowDeleteDialog());
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
        assertFalse(viewModel.isShowDeleteDialog());
    }

    @Test
    public void testIsShowNewDialog() {
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

    private Group moderatorGroup() {
        Group group = new Group("name", "description");
        PoulpeBranch poulpeBranch = new PoulpeBranch("name", "description");
        poulpeBranch.setModeratorsGroup(group);

        return group;
    }

    private Session preparedSession() {
        session = mock(org.hibernate.classic.Session.class);
        doReturn(preparedQuery()).when(session).createQuery(QUERY + MODERAING_GROUP_ID);
        return session;
    }

    private Query preparedQuery() {
        query = mock(Query.class);
        doReturn(query).when(query).setParameter(MODERAING_GROUP_ID, selectedGroup.getId());
        doReturn(new ArrayList()).when(query).list();
        return query;
    }

}
