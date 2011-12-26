package org.jtalks.poulpe.service.security;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Group;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.*;

/**
 * @author stanislav bashkirtsev
 */
public class BasicAclBuilderTest {
    private BasicAclBuilder builder;
    private AclManager manager;
    private Entity target = new Entity() {
    };

    @BeforeMethod
    public void setUp() throws Exception {
        manager = mock(AclManager.class);
        builder = new BasicAclBuilder(manager);
    }

    @Test(dataProvider = "groupsAndPermissions")
    public void testGrant(Group[] groups, Permission[] permissions) throws Exception {
        builder.setOwner(groups).grant(permissions).on(target).flush();
        verify(manager).grant(groupSids(groups), Lists.newArrayList(permissions), target);
    }


    @Test(dataProvider = "groupsAndPermissions")
    public void testDelete(Group[] groups, Permission[] permissions) throws Exception {
        builder.delete(permissions).setOwner(groups).on(target).flush();
        verify(manager).delete(groupSids(groups), newArrayList(permissions), target);
    }

    @Test(dataProvider = "groupsAndPermissions")
    public void testRestrict(Group[] groups, Permission[] permissions) throws Exception {
        builder.restrict(permissions).setOwner(groups).on(target).flush();
        verify(manager).restrict(groupSids(groups), newArrayList(permissions), target);
    }

    @Test(dataProvider = "onePermissionAndOneGroup")
    public void testRestrictGrantDeleteTogether(Permission permission, Group group) throws Exception {
        builder.restrict(permission).delete(permission).grant(permission).setOwner(group).on(target).flush();
        verify(manager, times(1)).grant(groupSids(group), newArrayList(permission), target);
        verify(manager, times(1)).restrict(groupSids(group), newArrayList(permission), target);
        verify(manager, times(1)).delete(groupSids(group), newArrayList(permission), target);
    }

    @Test(dataProvider = "twoPermissionsAndTwoGroup")
    public void testBuilderReused(Group[] groups, Permission[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            Permission permission = permissions[i];
            Group group = groups[i];
            builder.restrict(permission).delete(permission).grant(permission).setOwner(group).on(target).flush();
            verify(manager, times(1)).grant(groupSids(group), newArrayList(permission), target);
            verify(manager, times(1)).restrict(groupSids(group), newArrayList(permission), target);
            verify(manager, times(1)).delete(groupSids(group), newArrayList(permission), target);
        }
    }


    @Test(expectedExceptions = IllegalStateException.class)
    public void testFlushWithoutPermissions() throws Exception {
        builder.setOwner(new Group()).on(target).flush();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFlushWithoutSids() throws Exception {
        builder.restrict(JtalksPermission.ADMINISTRATION).on(target).flush();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFlushWithoutTarget() throws Exception {
        builder.restrict(JtalksPermission.ADMINISTRATION).setOwner(new Group()).flush();
    }

    private List<Sid> groupSids(Group... groups) {
        List<Sid> sids = newArrayList();
        for (Group group : groups) {
            sids.add(new UserGroupSid(group));
        }
        return sids;
    }

    @DataProvider(name = "onePermissionAndOneGroup")
    public Object[][] provideOnePermissionAndOneGroup() {
        return new Object[][]{{BranchPermission.CREATE_TOPICS, new Group()}};
    }

    @DataProvider(name = "twoPermissionsAndTwoGroup")
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    public Object[][] provideTwoPermissionsAndTwoGroup() {
        Group[] arrayWithTwoGroups = Group.createGroupsWithNames("Moderators", "Admins").toArray(new Group[]{});
        Permission[] arrayWithTwoPermissions = {BranchPermission.CREATE_TOPICS, BranchPermission.VIEW_TOPICS};

        return new Object[][]{{arrayWithTwoGroups, arrayWithTwoPermissions}};
    }

    @DataProvider(name = "groupsAndPermissions")
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    public Object[][] provideGroupsAndPermissions() {
        Group[] arrayWithOneGroup = Group.createGroupsWithNames("Trolls").toArray(new Group[]{});
        Permission[] arrayWithOnePermission = {BranchPermission.CREATE_TOPICS};

        Group[] arrayWithTwoGroups = Group.createGroupsWithNames("Moderators", "Admins").toArray(new Group[]{});
        Permission[] arrayWithTwoPermissions = {BranchPermission.CREATE_TOPICS, BranchPermission.VIEW_TOPICS};

        return new Object[][]{
                {arrayWithOneGroup, arrayWithOnePermission},
                {arrayWithTwoGroups, arrayWithTwoPermissions}};
    }
}
