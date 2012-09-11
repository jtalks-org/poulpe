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
package org.jtalks.poulpe.logic;

import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.AclUtil;
import org.jtalks.common.security.acl.ExtendedMutableAcl;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.common.security.acl.sids.UserGroupSid;
import org.jtalks.common.security.acl.sids.UserSid;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.AnonymousGroup;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class PermissionManagerTest {
    @Mock
    GroupDao groupDao;
    @Mock
    AclManager aclManager;
    @Mock
    AclUtil aclUtil;

    PermissionManager manager;

    List<Group> groups;
    List<GroupAce> groupAces;
    List<JtalksPermission> permissions;

    @Deprecated
    public static Group randomGroup(long id) {
        Group group = new Group(RandomStringUtils.randomAlphanumeric(15), RandomStringUtils.randomAlphanumeric(20));
        group.setId(id);
        return group;
    }

    @Deprecated
    public static Group getGroupWithId(List<Group> groups, long id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    /**
     * Mockito answer for {@link GroupDao#get(Long)} which return group from defined group list.
     *
     * @author Vyacheslav Zhivaev
     */
    class GroupDaoAnswer implements Answer<Group> {

        private final List<Group> groups;

        public GroupDaoAnswer(List<Group> groups) {
            this.groups = groups;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Group answer(InvocationOnMock invocation) throws Throwable {
            long id = (Long) invocation.getArguments()[0];
            return PermissionManagerTest.getGroupWithId(groups, id);
        }

    }

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        groups = Lists.newArrayList();
        permissions = Lists.newArrayList();
        groupAces = Lists.newArrayList();

        Long targetId = 1L;
        String targetType = "BRANCH";
        ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(targetType, targetId);
        when(aclUtil.createIdentityFor(any(Entity.class))).thenReturn(objectIdentity);
        ExtendedMutableAcl mutableAcl = mock(ExtendedMutableAcl.class);
        List<AccessControlEntry> controlEntries = new ArrayList<AccessControlEntry>();
        when(mutableAcl.getEntries()).thenReturn(controlEntries);
        when(aclUtil.getAclFor(objectIdentity)).thenReturn(mutableAcl);

        manager = new PermissionManager(aclManager, groupDao, aclUtil);
    }

    @Test(dataProvider = "accessChanges")
    public void testChangeGrants(PermissionChanges changes) throws Exception {
        PoulpeBranch branch = TestFixtures.branch();

        manager.changeGrants(branch, changes);

        verify(aclManager, times(changes.getRemovedGroups().size())).
                delete(anyListOf(Sid.class), eq(listFromArray(changes.getPermission())), eq(branch));

        verify(aclManager, times(changes.getNewlyAddedGroupsAsArray().length)).
                grant(anyListOf(Sid.class), eq(listFromArray(changes.getPermission())), eq(branch));
    }

    @Test(dataProvider = "accessChanges")
    public void testChangeRestriction(PermissionChanges changes) throws Exception {
        PoulpeBranch branch = TestFixtures.branch();

        manager.changeRestrictions(branch, changes);

        verify(aclManager, times(changes.getRemovedGroups().size())).
                delete(anyListOf(Sid.class), eq(listFromArray(changes.getPermission())), eq(branch));

        verify(aclManager, times(changes.getNewlyAddedGroupsAsArray().length)).
                restrict(anyListOf(Sid.class), eq(listFromArray(changes.getPermission())), eq(branch));
    }

    @Test
    public void testGetPermissionsMapForBranch() throws Exception {
        PoulpeBranch branch = TestFixtures.branchWithId();
        givenPermissions(branch, BranchPermission.values());

        GroupsPermissions<BranchPermission> groupsPermissions = manager.getPermissionsMapFor(branch);
        verify(aclManager).getGroupPermissionsOn(branch);
        verify(aclUtil, times(BranchPermission.values().length)).getAclFor(branch);
        assertTrue(groupsPermissions.getPermissions().containsAll(permissions));
        for (GroupAce groupAce : groupAces) {
            List<Group> groups = groupsPermissions.get(groupAce.getBranchPermission(), groupAce.isGranting());
            assertNotNull(getGroupWithId(groups, groupAce.getGroupId()));
            assertTrue(groups.contains(AnonymousGroup.ANONYMOUS_GROUP));
        }
    }

    @Test
    public void testGetPermissionsMapForComponent() throws Exception {
        Component component = TestFixtures.randomComponentWithId();
        givenPermissions(component, GeneralPermission.values());

        GroupsPermissions<GeneralPermission> groupsPermissions = manager.getPermissionsMapFor(component);
        verify(aclManager).getGroupPermissionsOn(eq(component));
        assertTrue(groupsPermissions.getPermissions().containsAll(permissions));
        for (GroupAce groupAce : groupAces) {
            List<Group> groups = groupsPermissions.get(GeneralPermission.findByMask(groupAce.getBranchPermissionMask()),
                    groupAce.isGranting());
            assertNotNull(getGroupWithId(groups, groupAce.getGroupId()));
        }
    }

    @Test
    public void testChangeGrantsOfAnonymousGroup() throws Exception {
        PoulpeBranch branch = TestFixtures.branch();
        PermissionChanges changes = new PermissionChanges(BranchPermission.CLOSE_TOPICS);
        List<Group> groupList = new ArrayList<Group>();
        groupList.add(AnonymousGroup.ANONYMOUS_GROUP);
        changes.addNewlyAddedGroups(groupList);
        manager.changeGrants(branch, changes);
        List<Sid> sids = new ArrayList<Sid>();
        sids.add(UserSid.createAnonymous());

        verify(aclManager, times(changes.getRemovedGroups().size())).
                delete(eq(sids), eq(listFromArray(changes.getPermission())), eq(branch));

        verify(aclManager, times(changes.getNewlyAddedGroupsAsArray().length)).
                grant(eq(sids), eq(listFromArray(changes.getPermission())), eq(branch));
    }

    @Test
    public void testRestrictGrantsOfAnonymousGroup() throws Exception {
        PoulpeBranch branch = TestFixtures.branch();
        PermissionChanges changes = new PermissionChanges(BranchPermission.CLOSE_TOPICS);
        List<Group> groupList = new ArrayList<Group>();
        groupList.add(AnonymousGroup.ANONYMOUS_GROUP);
        changes.addNewlyAddedGroups(groupList);
        manager.changeRestrictions(branch, changes);
        List<Sid> sids = new ArrayList<Sid>();
        sids.add(UserSid.createAnonymous());

        verify(aclManager, times(changes.getRemovedGroups().size())).
                delete(eq(sids), eq(listFromArray(changes.getPermission())), eq(branch));

        verify(aclManager, times(changes.getNewlyAddedGroupsAsArray().length)).
                restrict(eq(sids), eq(listFromArray(changes.getPermission())), eq(branch));
    }

    @Test
    public void testDeleteGrantsOfAnonymousGroup() throws Exception {
        PoulpeBranch branch = TestFixtures.branch();
        PermissionChanges changes = new PermissionChanges(BranchPermission.CLOSE_TOPICS);
        changes.addRemovedGroups(Lists.newArrayList(AnonymousGroup.ANONYMOUS_GROUP));
        manager.changeGrants(branch, changes);
        List<Sid> sids = Lists.<Sid>newArrayList(UserSid.createAnonymous());

        verify(aclManager, times(changes.getRemovedGroups().size())).
                delete(eq(sids), eq(listFromArray(changes.getPermission())), eq(branch));

        verify(aclManager, times(changes.getNewlyAddedGroupsAsArray().length)).
                grant(eq(sids), eq(listFromArray(changes.getPermission())), eq(branch));
    }

    @DataProvider
    public Object[][] accessChanges() {
        PermissionChanges accessChanges = new PermissionChanges(BranchPermission.CLOSE_TOPICS);
        accessChanges.addNewlyAddedGroups(newArrayList(new Group("new1"), new Group("new2")));
        accessChanges.addRemovedGroups(newArrayList(new Group("removed1"), new Group("removed2")));
        return new Object[][]{{accessChanges}};
    }

    @DataProvider
    public Object[][] branches() {
        return new Object[][]{{TestFixtures.branch()}};
    }

    private List<Permission> listFromArray(Permission... permissions) {
        return Lists.newArrayList(permissions);
    }

    private void givenPermissions(Entity entity, JtalksPermission... permissions) {
        givenGroupAces(entity, permissions);

        Answer<Group> answer = new GroupDaoAnswer(groups);

        when(groupDao.get(anyLong())).thenAnswer(answer);
        when(aclManager.getGroupPermissionsOn(eq(entity))).thenReturn(groupAces);
    }

    private void givenGroupAces(Entity entity, JtalksPermission... permissions) {
        long entityId = entity.getId();

        AuditLogger auditLogger = new ConsoleAuditLogger();
        AclAuthorizationStrategy aclAuthorizationStrategy = new AclAuthorizationStrategyImpl(new GrantedAuthorityImpl(
                "some_role"));
        ObjectIdentity entityIdentity = new AclUtil(null).createIdentity(entityId,
                entity.getClass().getSimpleName());
        ExtendedMutableAcl mutableAcl = mock(ExtendedMutableAcl.class);
        List<AccessControlEntry> accessControlEntries = new ArrayList<AccessControlEntry>();

        Acl acl = new AclImpl(entityIdentity, entityId + 1, aclAuthorizationStrategy, auditLogger);

        long lastGroupId = 1;

        for (int i = 0; i < permissions.length; i++) {
            for (int j = 0, count = RandomUtils.nextInt(20) + 10; j < count; j++) {
                Group group = randomGroup(lastGroupId++);
                groups.add(group);

                this.permissions.add(permissions[i]);
                groupAces.add(buildGroupAce(entity, permissions[i], (i % 2 == 1), acl, new UserGroupSid(group.getId())));
            }
            AccessControlEntry controlEntry = mock(AccessControlEntry.class);
            when(controlEntry.getPermission()).thenReturn(permissions[i]);
            when(controlEntry.getSid()).thenReturn(UserSid.createAnonymous());
            when(controlEntry.isGranting()).thenReturn((i % 2 == 1));
            accessControlEntries.add(controlEntry);
        }
        when(mutableAcl.getEntries()).thenReturn(accessControlEntries);
        when(aclUtil.getAclFor(entity)).thenReturn(mutableAcl);
    }

    private GroupAce buildGroupAce(Entity entity, JtalksPermission permission, boolean isGranting, Acl acl, Sid sid) {
        AccessControlEntry accessControlEntry = new AccessControlEntryImpl(entity.getId(), acl, sid, permission,
                isGranting, false, false);
        return new GroupAce(accessControlEntry);
    }
}
