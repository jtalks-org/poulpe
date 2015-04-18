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

import org.jtalks.common.model.entity.Branch;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.AclUtil;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.common.security.acl.builders.AclBuilders;
import org.jtalks.common.security.acl.sids.UniversalSid;
import org.jtalks.common.security.acl.sids.UserSid;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.AnonymousGroup;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.GroupsPermissions;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import org.jtalks.common.model.permissions.ProfilePermission;

/**
 * Responsible for allowing, restricting or deleting the permissions of the User Groups to actions.
 *
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class PermissionManager {
    private final AclManager aclManager;
    private final AclUtil aclUtil;
    private final GroupDao groupDao;

    /**
     * Constructs {@link PermissionManager} with given {@link AclManager} and {@link GroupDao}
     *
     * @param aclManager manager instance
     * @param groupDao   group dao instance
     */
    public PermissionManager(@Nonnull AclManager aclManager, @Nonnull GroupDao groupDao,
                             @Nonnull AclUtil aclUtil) {
        this.aclManager = aclManager;
        this.groupDao = groupDao;
        this.aclUtil = aclUtil;
    }

    /**
     * Changes the granted permissions according to the specified changes.
     *
     * @param entity  the entity to change permissions to
     * @param changes contains a permission itself, a list of groups to be granted to the permission and the list of
     *                groups to remove their granted privileges
     * @see org.jtalks.poulpe.model.dto.PermissionChanges#getNewlyAddedGroupsAsArray()
     * @see org.jtalks.poulpe.model.dto.PermissionChanges#getRemovedGroups()
     */
    public void changeGrants(Entity entity, PermissionChanges changes) {
        for (Group group : changes.getNewlyAddedGroupsAsArray()) {
            changeGrantsOfGroup(group, changes.getPermission(), entity, true);
        }
        for (Group group : changes.getRemovedGroupsAsArray()) {
            deleteGrantsOfGroup(group, changes.getPermission(), entity);
        }
    }

    /**
     * Changes the restricting permissions according to the specified changes.
     *
     * @param entity  the entity to change permissions to
     * @param changes contains a permission itself, a list of groups to be restricted from the permission and the list
     *                of groups to remove their restricting privileges
     * @see org.jtalks.poulpe.model.dto.PermissionChanges#getNewlyAddedGroupsAsArray()
     * @see org.jtalks.poulpe.model.dto.PermissionChanges#getRemovedGroups()
     */
    public void changeRestrictions(Entity entity, PermissionChanges changes) {
        for (Group group : changes.getNewlyAddedGroupsAsArray()) {
            changeGrantsOfGroup(group, changes.getPermission(), entity, false);
        }
        for (Group group : changes.getRemovedGroupsAsArray()) {
            deleteGrantsOfGroup(group, changes.getPermission(), entity);
        }
    }

    /**
     * Gets {@link org.jtalks.poulpe.model.dto.GroupsPermissions} for provided {@link Component}.
     *
     * @param component the component to obtain PermissionsMap for
     * @return {@link org.jtalks.poulpe.model.dto.GroupsPermissions} for {@link Component}
     */
    public GroupsPermissions<GeneralPermission> getPermissionsMapFor(Component component) {
        return getPermissionsMapFor(GeneralPermission.getAllAsList(), component);
    }
    
    /**
     * Gets {@link PermissionsMap} for provided list of {@link Group}'s.
     *
     * 
     * @param groups the List {@link Group}'s to obtain PermissionsMap for
     * @return {@link PermissionsMap} for {@link Group}
     */
    public GroupsPermissions<ProfilePermission> getPermissionsMapFor(List<Group> groups) {
        GroupsPermissions<ProfilePermission> permissions = new GroupsPermissions<ProfilePermission>(ProfilePermission.getAllAsList());
        for (Group group: groups){
            GroupsPermissions<ProfilePermission> pmGroup = getPermissionsMapFor(ProfilePermission.getAllAsList(), group);
            for (ProfilePermission permission:pmGroup.getPermissions()){
                for(Group groupInsert : pmGroup.getAllowed(permission)){
                    permissions.addAllowed(permission,groupInsert);
                }
                for(Group groupInsert : pmGroup.getRestricted(permission)){
                    permissions.addRestricted(permission,groupInsert);
                }
            }
        }
        return permissions;
    } 
    
    /**
     * Gets {@link org.jtalks.poulpe.model.dto.GroupsPermissions} for provided {@link Entity}.
     *
     * @param permissions the list of permissions to get
     * @param entity      the entity to get for
     * @return {@link org.jtalks.poulpe.model.dto.GroupsPermissions} for provided {@link Entity}
     */
    public <T extends JtalksPermission> GroupsPermissions<T> getPermissionsMapFor(List<T> permissions, Entity entity) {
        GroupsPermissions<T> groupsPermissions = new GroupsPermissions<T>(permissions);
        List<GroupAce> groupAces = aclManager.getGroupPermissionsOn(entity);
        for (T permission : permissions) {
            for (GroupAce groupAce : groupAces) {
                if (groupAce.getPermissionMask() == permission.getMask()) {
                    groupsPermissions.add(permission, getGroup(groupAce), groupAce.isGranting());
                }
            }
            for (AccessControlEntry controlEntry : aclUtil.getAclFor(entity).getEntries()) {
                if (controlEntry.getPermission().equals(permission)
                        && ((UniversalSid)controlEntry.getSid()).getSidId().equals(UserSid.createAnonymous().getSidId())) {
                    groupsPermissions.add(permission, AnonymousGroup.ANONYMOUS_GROUP, controlEntry.isGranting());
                }
            }
        }
        return groupsPermissions;
    }

    /**
     * @param groupAce from which if of group should be extracted
     * @return {@link Group} extracted from {@link GroupAce}
     */
    private Group getGroup(GroupAce groupAce) {
        return groupDao.get(groupAce.getGroupId());
    }

    /**
     * Changes the granted permission for group. If group is AnonymousGroup method changes permissions
     * for Anonymous Sid.
     *
     * @param group      user group
     * @param permission permission
     * @param entity     the entity to change permissions to
     * @param granted    permission is granted or restricted
     */
    private void changeGrantsOfGroup(Group group, JtalksPermission permission, Entity entity, boolean granted) {
        if (group instanceof AnonymousGroup) {
            changeGrantsOfAnonymousGroup(permission, entity, granted);
        } else {
            AclBuilders builders = new AclBuilders();
            if (granted) {
                builders.newBuilder(aclManager).grant(permission).to(group).on(entity).flush();
            } else {
                builders.newBuilder(aclManager).restrict(permission).to(group).on(entity).flush();
            }
        }
    }

    /**
     * Changes permissions for Anonymous Sid.
     *
     * @param permission permission
     * @param entity     the entity to change permissions to
     * @param granted    permission is granted or restricted
     */
    private void changeGrantsOfAnonymousGroup(JtalksPermission permission, Entity entity, boolean granted) {
        List<Permission> jtalksPermissions = new ArrayList<Permission>();
        jtalksPermissions.add(permission);
        List<Sid> sids = new ArrayList<Sid>();
        sids.add(UserSid.createAnonymous());
        if (granted) {
            aclManager.grant(sids, jtalksPermissions, entity);
        } else {
            aclManager.restrict(sids, jtalksPermissions, entity);
        }
    }

    /**
     * Deletes the granted permission for group. If group is AnonymousGroup method deletes permissions
     * for Anonymous Sid.
     *
     * @param group      user group
     * @param permission permission
     * @param entity     the entity to change permissions to
     */
    private void deleteGrantsOfGroup(Group group,
                                     JtalksPermission permission,
                                     Entity entity) {
        if (group instanceof AnonymousGroup) {
            deleteGrantsOfAnonymousGroup(permission, entity);
        } else {
            AclBuilders builders = new AclBuilders();
            builders.newBuilder(aclManager).delete(permission).from(group).on(entity).flush();
        }
    }

    /**
     * Deletes permissions for Anonymous Sid.
     *
     * @param permission permission
     * @param entity     the entity to change permissions to
     */
    private void deleteGrantsOfAnonymousGroup(JtalksPermission permission,
                                              Entity entity) {
        List<Permission> jtalksPermissions = new ArrayList<Permission>();
        jtalksPermissions.add(permission);
        List<Sid> sids = new ArrayList<Sid>();
        sids.add(UserSid.createAnonymous());
        aclManager.delete(sids, jtalksPermissions, entity);
    }
}
