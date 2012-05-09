package org.jtalks.poulpe.web.controller.group;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Leonid Kazancev
 */
public class UserGroupVmTest {

    private static final String SEARCH_STRING = "searchString";
    private static final String GROUP_NAME = "GroupName";

    private UserGroupVm viewModel;

    @Mock
    private GroupService groupService;
    @Mock
    private EntityValidator entityValidator;
    @Mock
    private WindowManager windowManager;
    @Mock
    private DialogManager dialogManager;
    @Mock
    private ZkHelper zkHelper;
    @Mock
    private Window userDialog;
    @Mock
    private ListModelList<Group> groups;
    @Mock
    private DialogManager.Performable preformable;
    @Mock
    private Group selectedGroup;
    @Mock
    private SelectedEntity<Group> selectedEntity;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        selectedGroup = new Group();
        viewModel = new UserGroupVm(groupService, entityValidator, windowManager, dialogManager);
        viewModel.setZkHelper(zkHelper);
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
        viewModel.deleteGroup();
        verify(dialogManager).confirmDeletion(any(String.class), any(DialogManager.Performable.class));
    }

    @Test
    public void testAddGroup() {
        viewModel.addGroup();
        verify(zkHelper).wireToZul(UserGroupVm.EDIT_GROUP_URL);
    }

    @Test
    public void testEditGroup() throws Exception {
        viewModel.editGroup(selectedGroup);
        verify(zkHelper).wireToZul(UserGroupVm.EDIT_GROUP_URL);
    }

    @Test
    public void testSaveGroup() throws Exception {
        when(zkHelper.findComponent(UserGroupVm.EDIT_GROUP_DIALOG)).thenReturn(userDialog);
        when(entityValidator.validate(any(Group.class))).thenReturn(ValidationResult.EMPTY);
        Group group = new Group();

        viewModel.saveGroup(group);
        verify(groupService).saveGroup(group);
        verify(userDialog).detach();
    }

    @Test
    public void testCancelEdit() {
        when(zkHelper.findComponent(UserGroupVm.EDIT_GROUP_DIALOG)).thenReturn(userDialog);
        viewModel.cancelEdit();

        verify(userDialog).detach();
    }
}
