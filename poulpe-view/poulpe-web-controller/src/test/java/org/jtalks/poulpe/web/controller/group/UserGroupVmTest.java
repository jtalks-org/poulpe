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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
* @author Leonid Kazancev
*/
public class UserGroupVmTest {

    private static final String SEARCH_STRING = "searchString";
    private static final String GROUP_NAME = "GroupName";

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
        viewModel = new UserGroupVm(groupService, windowManager);
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
        viewModel.setSelectedEntity(selectedEntity);
        viewModel.showGroupMemberEditWindow();
        verify(windowManager).open(UserGroupVm.EDIT_GROUP_MEMBERS_URL);
    }

    @Test
    public void testDeleteGroup() {
        doNothing().when(groupService).deleteGroup(any(Group.class));
        viewModel.deleteGroup();
        verify(groupService).deleteGroup(selectedGroup);
    }

    @Test
    public void testAddNewGroup() {
        viewModel.addNewGroup();
        assert(viewModel.getShowNewDialog());
    }

    @Test
    public void testSaveGroup() throws Exception {
        Group group = new Group();

        viewModel.saveGroup(group);
        verify(groupService).saveGroup(group);
        assert!((viewModel.getShowDeleteDialog())&&(viewModel.getShowEditDialog())&&(viewModel.getShowNewDialog()));
    }

}
