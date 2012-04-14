package org.jtalks.poulpe.web.controller.component;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionForEntity;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.GroupService;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.utils.ObjectsFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * Tests for {@link EditGroupsForComponentPermissionVm}.
 * 
 * @author Vyacheslav Zhivaev
 * 
 */
public class EditGroupsForComponentPermissionVmTest {

    // limits dataProvider out
    private final int DATA_PROVIDER_LIMIT = 20;

    // SUT
    private EditGroupsForComponentPermissionVm viewModel;

    @Mock
    private WindowManager windowManager;
    @Mock
    private ComponentService componentService;
    @Mock
    private GroupService groupService;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "dataProvider")
    public void testCancelWithoutChanges(PermissionForEntity permissionForEntity,
            PermissionsMap<GeneralPermission> permissionsMap) {
        initTest(permissionForEntity, permissionsMap);

        viewModel.cancel();

        vefiryNothingChanges();
    }

    @Test(dataProvider = "dataProvider")
    public void testCancelWithChanges(PermissionForEntity permissionForEntity,
            PermissionsMap<GeneralPermission> permissionsMap) {
        initTest(permissionForEntity, permissionsMap);

        viewModel.addAll();
        viewModel.cancel();

        vefiryNothingChanges();
    }

    @Test(dataProvider = "dataProvider")
    public void testSaveWithoutChanges(PermissionForEntity permissionForEntity,
            PermissionsMap<GeneralPermission> permissionsMap) {
        initTest(permissionForEntity, permissionsMap);

        viewModel.save();

        vefiryNothingChanges();
    }

    @Test(dataProvider = "dataProvider")
    public void testSaveWithChanges(PermissionForEntity permissionForEntity,
            PermissionsMap<GeneralPermission> permissionsMap) {
        initTest(permissionForEntity, permissionsMap);

        viewModel.removeAll();
        viewModel.save();

        vefiryPermissionsChanged(permissionForEntity);

        verify(windowManager).open(anyString());
    }

    @DataProvider
    public Object[][] dataProvider() {
        List<PermissionForEntity> permissionsForEntity = Lists.newArrayList();
        PermissionsMap<GeneralPermission> permissionsMap = new PermissionsMap<GeneralPermission>(
                GeneralPermission.getAllAsList());

        // target entity (Component)
        ComponentType[] componentTypes = ComponentType.values();
        Component target = ObjectsFactory.createComponent(RandomUtils.nextLong(),
                RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(20),
                componentTypes[RandomUtils.nextInt(componentTypes.length)]);

        // permissions to work with
        GeneralPermission[] permissions = GeneralPermission.values();

        // building fixtures
        int count = Math.min(DATA_PROVIDER_LIMIT, permissions.length);
        for (int i = 0; i < count; i++) {
            permissionsForEntity.add(new PermissionForEntity(target, true, permissions[i]));
            permissionsForEntity.add(new PermissionForEntity(target, false, permissions[i]));

            for (int j = 0, countj = RandomUtils.nextInt(4) + 2; j < countj; j++) {
                permissionsMap.add(permissions[i], ObjectsFactory.fakeGroup(), ObjectsFactory.fakeGroup());
            }
        }

        // converting fixtures to usable state, adding same PermissionsMap for each PermissionForEntity
        int permissionsCount = permissionsForEntity.size();
        Object[][] result = new Object[permissionsCount][2];

        for (int i = 0; i < permissionsCount; i++) {
            result[i][0] = permissionsForEntity.get(i);
            result[i][1] = permissionsMap;
        }

        return result;
    }

    private void initTest(PermissionForEntity permissionForEntity, PermissionsMap<GeneralPermission> permissionsMap) {
        when(componentService.getPermissionsMapFor(eq((Component) permissionForEntity.getTarget()))).thenReturn(
                permissionsMap);

        viewModel = new EditGroupsForComponentPermissionVm(windowManager, componentService, groupService,
                ObjectsFactory.createSelectedEntity((Object) permissionForEntity));

        viewModel.initVm();
    }

    private void vefiryPermissionsChanged(PermissionForEntity permissionForEntity) {
        Component target = (Component) permissionForEntity.getTarget();

        if (permissionForEntity.isAllowed()) {
            verify(componentService).changeGrants(eq(target), any(PermissionChanges.class));
        } else {
            verify(componentService).changeRestrictions(eq(target), any(PermissionChanges.class));
        }
    }

    private void vefiryNothingChanges() {
        verify(componentService, never()).changeGrants(any(Component.class), any(PermissionChanges.class));
        verify(componentService, never()).changeRestrictions(any(Component.class), any(PermissionChanges.class));
        verify(componentService, never()).saveComponent(any(Component.class));

        verify(groupService, never()).saveGroup(any(PoulpeGroup.class));
        verify(groupService, never()).deleteGroup(any(PoulpeGroup.class));
    }
}
