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
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.AclUtil;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.common.security.acl.builders.AclBuilders;
import org.jtalks.common.security.acl.sids.UserSid;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.entity.Component;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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
        boolean deletePermission;
        for (Group group : changes.getNewlyAddedGroupsAsArray()) {
            deletePermission = false;
            changeGrantsOfGroup(group, changes.getPermission(), entity, true, deletePermission);
        }
        for (Group group : changes.getRemovedGroupsAsArray()) {
            deletePermission = true;
            changeGrantsOfGroup(group, changes.getPermission(), entity, true, deletePermission);
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
        boolean deletePermission;
        for (Group group : changes.getNewlyAddedGroupsAsArray()) {
            deletePermission = false;
            changeGrantsOfGroup(group, changes.getPermission(), entity, false, deletePermission);
        }
        for (Group group : changes.getRemovedGroupsAsArray()) {
            deletePermission = true;
            changeGrantsOfGroup(group, changes.getPermission(), entity, false, deletePermission);
        }
    }

    /**
     * @param branch object identity
     * @return {@link BranchPermissions} for given branch
     */
    public PermissionsMap<BranchPermission> getPermissionsMapFor(Branch branch) {
        return getPermissionsMapFor(BranchPermission.getAllAsList(), branch);
    }

    /**
     * Gets {@link PermissionsMap} for provided {@link Component}.
     *
     * @param component the component to obtain PermissionsMap for
     * @return {@link PermissionsMap} for {@link Component}
     */
    public PermissionsMap<GeneralPermission> getPermissionsMapFor(Component component) {
        return getPermissionsMapFor(GeneralPermission.getAllAsList(), component);
    }

    /**
     * Gets {@link PermissionsMap} for provided {@link Entity}.
     *
     * @param permissions the list of permissions to get
     * @param entity      the entity to get for
     * @return {@link PermissionsMap} for provided {@link Entity}
     */
    public <T extends JtalksPermission> PermissionsMap<T> getPermissionsMapFor(List<T> permissions, Entity entity) {
        PermissionsMap<T> permissionsMap = new PermissionsMap<T>(permissions);
        List<GroupAce> groupAces = aclManager.getGroupPermissionsOn(entity);
        for (T permission : permissions) {
            for (GroupAce groupAce : groupAces) {
                if (groupAce.getBranchPermissionMask() == permission.getMask()) {
                    permissionsMap.add(permission, getGroup(groupAce), groupAce.isGranting());
                }
            }
            for (AccessControlEntry controlEntry : aclUtil.getAclFor(aclUtil.createIdentityFor(entity)).getEntries()) {
                if (controlEntry.getPermission().equals(permission)
                        && controlEntry.getSid().getSidId().equals(UserSid.createAnonymous().getSidId())) {
                    permissionsMap.add(permission, AnonymousGroup.ANONYMOUS_GROUP, controlEntry.isGranting());
                }
            }
        }
        return permissionsMap;
    }

    /**
     * @param groupAce from which if of group should be extracted
     * @return {@link Group} extracted from {@link GroupAce}
     */
    private Group getGroup(GroupAce groupAce) {
        return groupDao.get(groupAce.getGroupId());
    }

    /**
     * Changes the granted permission for group.
     *
     * @param group      user group
     * @param permission permission
     * @param entity     the entity to change permissions to
     * @param granted    permission is granted or restricted
     * @param delete     needs delete permission
     */
    private void changeGrantsOfGroup(Group group,
                                     JtalksPermission permission,
                                     Entity entity,
                                     boolean granted,
                                     boolean delete) {
        AclBuilders builders = new AclBuilders();
        if (group.getName().equals(AnonymousGroup.ANONYMOUS_GROUP.getName())) {
            List<Permission> jtalksPermissions = new ArrayList<Permission>();
            jtalksPermissions.add(permission);
            List<Sid> sids = new ArrayList<Sid>();
            sids.add(UserSid.createAnonymous());
            if (delete) {
                aclManager.delete(sids, jtalksPermissions, entity);
            } else if (granted) {
                aclManager.grant(sids, jtalksPermissions, entity);
            } else {
                aclManager.restrict(sids, jtalksPermissions, entity);
            }
        } else {
            if (delete) {
                builders.newBuilder(aclManager).delete(permission).from(group).on(entity).flush();
            } else if (granted) {
                builders.newBuilder(aclManager).grant(permission).to(group).on(entity).flush();
            } else {
                builders.newBuilder(aclManager).restrict(permission).to(group).on(entity).flush();
            }
        }
    }
}
