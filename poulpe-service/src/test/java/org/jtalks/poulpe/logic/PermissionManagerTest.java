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

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.AclUtil;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.common.security.acl.sids.UserGroupSid;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class PermissionManagerTest {

    public static PoulpeBranch randomBranch() {
        return new PoulpeBranch(RandomStringUtils.randomAlphanumeric(15), RandomStringUtils.randomAlphanumeric(20));
    }

    public static Group randomGroup(long id) {
        Group group = new Group(RandomStringUtils.randomAlphanumeric(15), RandomStringUtils.randomAlphanumeric(20));
        group.setId(id);
        return group;
    }

    public static Component randomComponent() {
        ComponentType[] types = ComponentType.values();
        return new Component(RandomStringUtils.randomAlphanumeric(15), RandomStringUtils.randomAlphanumeric(20),
                types[RandomUtils.nextInt(types.length)]);
    }

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
     * 
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

    @Mock
    GroupDao groupDao;
    @Mock
    AclManager aclManager;

    PermissionManager manager;

    List<Group> groups;
    List<GroupAce> groupAces;
    List<JtalksPermission> permissions;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        groups = Lists.newArrayList();
        permissions = Lists.newArrayList();
        groupAces = Lists.newArrayList();

        manager = new PermissionManager(aclManager, groupDao);
    }

    @Test(dataProvider = "accessChanges")
    public void testChangeGrants(PermissionChanges changes) throws Exception {
        PoulpeBranch branch = randomBranch();

        manager.changeGrants(branch, changes);

        verify(aclManager).delete(getRemovedSids(changes), listFromArray(changes.getPermission()), branch);
        verify(aclManager).grant(getNewlyAddedSids(changes), listFromArray(changes.getPermission()), branch);
    }

    @Test(dataProvider = "accessChanges")
    public void testChangeRestriction(PermissionChanges changes) throws Exception {
        PoulpeBranch branch = randomBranch();

        manager.changeRestrictions(branch, changes);

        verify(aclManager).delete(getRemovedSids(changes), listFromArray(changes.getPermission()), branch);
        verify(aclManager).restrict(getNewlyAddedSids(changes), listFromArray(changes.getPermission()), branch);
    }

    @Test
    public void testGetPermissionsMapForBranch() throws Exception {
        PoulpeBranch branch = randomBranch();

        givenPermissions(branch, BranchPermission.values());

        PermissionsMap<BranchPermission> permissionsMap = manager.getPermissionsMapFor(branch);

        verify(aclManager).getGroupPermissionsOn(eq(branch));

        assertTrue(permissionsMap.getPermissions().containsAll(permissions));

        for (GroupAce groupAce : groupAces) {
            List<Group> groups = permissionsMap.get(groupAce.getBranchPermission(), groupAce.isGranting());
            assertNotNull(getGroupWithId(groups, groupAce.getGroupId()));
        }
    }

    @Test
    public void testGetPermissionsMapForComponent() throws Exception {
        Component component = randomComponent();

        givenPermissions(component, GeneralPermission.values());

        PermissionsMap<GeneralPermission> permissionsMap = manager.getPermissionsMapFor(component);

        verify(aclManager).getGroupPermissionsOn(eq(component));

        assertTrue(permissionsMap.getPermissions().containsAll(permissions));

        for (GroupAce groupAce : groupAces) {
            List<Group> groups = permissionsMap.get(
                    GeneralPermission.findByMask(groupAce.getBranchPermissionMask()), groupAce.isGranting());
            assertNotNull(getGroupWithId(groups, groupAce.getGroupId()));
        }
    }

    @DataProvider
    public Object[][] accessChanges() {
        PermissionChanges accessChanges = new PermissionChanges(BranchPermission.CREATE_TOPICS);
        accessChanges.addNewlyAddedGroups(newArrayList(new Group("new1"), new Group("new2")));
        accessChanges.addRemovedGroups(newArrayList(new Group("removed1"), new Group("removed2")));
        return new Object[][] { { accessChanges } };
    }

    @DataProvider
    public Object[][] branches() {
        return new Object[][] { { randomBranch() } };
    }

    private List<UserGroupSid> getNewlyAddedSids(PermissionChanges accessChanges) {
        return UserGroupSid.create(accessChanges.getNewlyAddedGroupsAsArray());
    }

    private List<UserGroupSid> getRemovedSids(PermissionChanges accessChanges) {
        return UserGroupSid.create(accessChanges.getRemovedGroupsAsArray());
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
        ObjectIdentity entityIdentity = AclUtil.createObjectIdentityUtils().createIdentity(entityId,
                entity.getClass().getSimpleName());

        Acl acl = new AclImpl(entityIdentity, entityId + 1, aclAuthorizationStrategy, auditLogger);

        long lastGroupId = 1;

        for (int i = 0; i < permissions.length; i++) {
            for (int j = 0, count = RandomUtils.nextInt(20) + 10; j < count; j++) {
                Group group = randomGroup(lastGroupId++);
                groups.add(group);

                this.permissions.add(permissions[i]);
                groupAces
                        .add(buildGroupAce(entity, permissions[i], (i % 2 == 1), acl, new UserGroupSid(group.getId())));
            }
        }
    }

    private GroupAce buildGroupAce(Entity entity, JtalksPermission permission, boolean isGranting, Acl acl, Sid sid) {
        AccessControlEntry accessControlEntry = new AccessControlEntryImpl(entity.getId(), acl, sid,
                permission, true, true, false);
        return new GroupAce(accessControlEntry);
    }
}
