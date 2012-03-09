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

import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jtalks.common.model.entity.Branch;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.ComponentPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.BasicAclBuilder;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.common.security.acl.UserGroupSid;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.dto.branches.BranchPermissions;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.springframework.security.acls.model.AccessControlEntry;

/**
 * Responsible for allowing, restricting or deleting the permissions of the User Groups to actions.
 * 
 * @author stanislav bashkirtsev
 * @author Vyacheslav Zhivaev
 */
public class PermissionManager {
    private final AclManager aclManager;
    private final GroupDao groupDao;

    /**
     * Constructs {@link PermissionManager} with given {@link AclManager} and {@link GroupDao}
     * 
     * @param aclManager manager instance
     * @param groupDao group dao instance
     */
    public PermissionManager(@Nonnull AclManager aclManager, @Nonnull GroupDao groupDao) {
        this.aclManager = aclManager;
        this.groupDao = groupDao;
    }

    /**
     * Changes the granted permissions according to the specified changes.
     * 
     * @param entity the entity to change permissions to
     * @param changes contains a permission itself, a list of groups to be granted to the permission and the list of
     * groups to remove their granted privileges
     * @see org.jtalks.poulpe.model.dto.PermissionChanges#getNewlyAddedGroupsAsArray()
     * @see org.jtalks.poulpe.model.dto.PermissionChanges#getRemovedGroups()
     */
    public void changeGrants(Entity entity, PermissionChanges changes) {
        BasicAclBuilder aclBuilder = new BasicAclBuilder(aclManager).grant(changes.getPermission())
                .setOwner(changes.getNewlyAddedGroupsAsArray()).on(entity).flush();
        aclBuilder.delete(changes.getPermission()).setOwner(changes.getRemovedGroupsAsArray()).on(entity).flush();
    }

    /**
     * Changes the restricting permissions according to the specified changes.
     * 
     * @param entity the entity to change permissions to
     * @param changes contains a permission itself, a list of groups to be restricted from the permission and the list
     * of groups to remove their restricting privileges
     * @see org.jtalks.poulpe.model.dto.PermissionChanges#getNewlyAddedGroupsAsArray()
     * @see org.jtalks.poulpe.model.dto.PermissionChanges#getRemovedGroups()
     */
    public void changeRestrictions(Entity entity, PermissionChanges changes) {
        BasicAclBuilder aclBuilder = new BasicAclBuilder(aclManager).restrict(changes.getPermission())
                .setOwner(changes.getNewlyAddedGroupsAsArray()).on(entity).flush();
        aclBuilder.delete(changes.getPermission()).setOwner(changes.getRemovedGroupsAsArray()).on(entity).flush();
    }

    /**
     * @param branch object identity
     * @return {@link BranchPermissions} for given branch
     */
    // TODO: fix AclManager.getBranchPermissions()
    public BranchPermissions getGroupAccessListFor(Branch entity) {
        BranchPermissions branchAccessList = BranchPermissions.create(BranchPermission.getAllAsList());
        List<GroupAce> groupAces = aclManager.getBranchPermissions(entity);
        for (GroupAce groupAce : groupAces) {
            branchAccessList.put(groupAce.getBranchPermission(), getGroup(groupAce), groupAce.isGranting());
        }
        return branchAccessList;
    }

    /**
     * Gets {@link PermissionsMap} for provided {@link Component}.
     * 
     * @param component the component to obtain PermissionsMap for
     * @return {@link PermissionsMap} for {@link Component}
     */
    public PermissionsMap<ComponentPermission> getPermissionsMapFor(Component component) {
        return getPermissionsMapFor(Arrays.asList(ComponentPermission.values()), component);
    }

    /**
     * Gets {@link PermissionsMap} for provided {@link Entity}.
     * 
     * @param permissions the list of permissions to get
     * @param entity the entity to get for
     * @return {@link PermissionsMap} for provided {@link Entity}
     */
    public <T extends JtalksPermission> PermissionsMap<T> getPermissionsMapFor(List<T> permissions, Entity entity) {
        PermissionsMap<T> permissionsMap = PermissionsMap.create(permissions);
        List<GroupAce> groupAces = aclManager.getBranchPermissions((Branch) entity);
        Map<Integer, GroupAce> groupAcesByMask = index(groupAces, on(GroupAce.class).getBranchPermissionMask());
        for (T permission : permissions) {
            GroupAce groupAce = groupAcesByMask.get(permission.getMask());
            if (groupAce != null) {
                permissionsMap.put(permission, getGroup(groupAce), groupAce.isGranting());
            }
        }
        return permissionsMap;
    }

    /**
     * @param groupAce from which if of group should be extracted
     * @return {@link PoulpeGroup} extracted from {@link GroupAce}
     */
    private PoulpeGroup getGroup(GroupAce groupAce) {
        long groupId = extractGroupId(groupAce);
        return groupDao.get(groupId);
    }

    // TODO: get rid of it once GroupAce#getGroupId() is created!!!!!
    private static long extractGroupId(GroupAce groupAce) {
        try {
            Class<GroupAce> groupAceClass = GroupAce.class;
            Field aceField = groupAceClass.getDeclaredField("ace");
            aceField.setAccessible(true);

            AccessControlEntry ace = (AccessControlEntry) aceField.get(groupAce);
            UserGroupSid sid = (UserGroupSid) ace.getSid();

            return Long.parseLong(sid.getGroupId());

        } catch (Exception e) {
            throw new RuntimeException("Error accessing to ace private field, nested exception: ", e);
        }
    }

}
