package org.jtalks.poulpe.web.controller.branch;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.dto.branches.GroupAccessList;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectsFactory;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementBlock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BranchPermissionManagementVmTest {

    // SUT
    private BranchPermissionManagementVm sut;

    // context related
    @Mock
    private WindowManager windowManager;
    @Mock
    private BranchService branchService;

    // context related
    private PoulpeBranch branch;
    private SelectedEntity<Object> selectedEntity;

    // sample/affected data
    private BranchPermission allowedPermission;
    private BranchPermission restrictedPermission;
    private PoulpeGroup allowedGroup;
    private PoulpeGroup restrictedGroup;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        selectedEntity = new SelectedEntity<Object>();
        selectedEntity.setEntity(branch);

        allowedPermission = BranchPermission.CREATE_TOPICS;
        restrictedPermission = BranchPermission.VIEW_TOPICS;

        allowedGroup = ObjectsFactory.fakeGroup();
        restrictedGroup = ObjectsFactory.fakeGroup();

        Map<BranchPermission, GroupAccessList> addToAccessList = new HashMap<BranchPermission, GroupAccessList>();
        BranchAccessList groupAccessList = new BranchAccessList(addToAccessList);
        groupAccessList.addAllowed(allowedPermission, allowedGroup);
        groupAccessList.addRestricted(restrictedPermission, restrictedGroup);
        when(branchService.getGroupAccessListFor(branch)).thenReturn(groupAccessList);

        sut = new BranchPermissionManagementVm(windowManager, branchService, selectedEntity);
    }

    /**
     * Checks that we are have all data passed in VM.
     */
    @Test
    public void getBlocksTest() {
        List<BranchPermissionManagementBlock> blocks = sut.getBlocks();

        for (BranchPermissionManagementBlock block : blocks) {
            JtalksPermission permission = block.getPermission();

            assertTrue(permission == allowedPermission || permission == restrictedPermission);

            if (permission == allowedPermission) {
                assertTrue(block.getAllowRow().getGroups().contains(allowedGroup));
            } else {
                assertTrue(block.getRestrictRow().getGroups().contains(restrictedGroup));
            }
        }
    }

    /**
     * What we are provide, that we must get back in same state.
     */
    @Test
    public void getBranchTest() {
        assertEquals(sut.getBranch(), branch);
    }

    /**
     * Check that dialog really opens.
     */
    @Test
    public void showGroupsDialogTest() {
        // provide data which already passed in VM through service layer

        sut.showGroupsDialog("permissionName=" + allowedPermission.getName() + ",mode=allow");
        sut.showGroupsDialog("permissionName=" + restrictedPermission.getName() + ",mode=restrict");

        verify(windowManager, times(2)).open(anyString());
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void showGroupsDialogIllegalFromatTest() {
        sut.showGroupsDialog("permissionName=" + restrictedPermission.getName() + ",mode=HERE_ILLEGAL_FORMATTED_STRING");

        verify(windowManager, never()).open(anyString());
    }

}
