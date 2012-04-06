package org.jtalks.poulpe.web.controller.branch;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectsFactory;
import org.jtalks.poulpe.web.controller.zkmacro.PermissionManagementBlock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link BranchPermissionManagementVm}.
 * 
 * @author Vyacheslav Zhivaev
 * 
 */
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

        PermissionsMap<BranchPermission> permissionsMap = new PermissionsMap<BranchPermission>();
        permissionsMap.addAllowed(allowedPermission, allowedGroup);
        permissionsMap.addRestricted(restrictedPermission, restrictedGroup);

        when(branchService.getPermissionsFor(branch)).thenReturn(permissionsMap);

        sut = new BranchPermissionManagementVm(windowManager, branchService, selectedEntity);
    }

    /**
     * Checks that we are have all data passed in VM.
     */
    @Test
    public void testGetBlocks() {
        List<PermissionManagementBlock> blocks = sut.getBlocks();

        for (PermissionManagementBlock block : blocks) {
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
    public void testGetBranch() {
        assertEquals(sut.getBranch(), branch);
    }

    /**
     * Check that dialog really opens.
     */
    @Test
    public void testShowGroupsDialog() {
        sut.showGroupsDialog(allowedPermission, "allow");
        sut.showGroupsDialog(restrictedPermission, "restrict");

        verify(windowManager, times(2)).open(anyString());
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testShowGroupsDialog_IllegalFormat() {
        sut.showGroupsDialog(restrictedPermission, "HERE_ILLEGAL_FORMATTED_STRING");

        verify(windowManager, never()).open(anyString());
    }

}
