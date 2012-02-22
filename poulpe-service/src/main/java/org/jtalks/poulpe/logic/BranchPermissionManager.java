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

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Nonnull;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.BasicAclBuilder;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.common.security.acl.UserGroupSid;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeGroup;
import org.springframework.security.acls.model.AccessControlEntry;

/**
 * Responsible for allowing, restricting or deleting the permissions of the User Groups to actions related to the
 * Branches.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManager {
    private static final String UserGroupSid = null;
    private final AclManager aclManager;
    private final GroupDao groupDao;

    public BranchPermissionManager(@Nonnull AclManager aclManager, @Nonnull GroupDao groupDao) {
        this.aclManager = aclManager;
        this.groupDao = groupDao;
    }

    /**
     * Changes the granted permissions according to the specified changes.
     *
     * @param branch  the branch to change permissions to
     * @param changes contains a permission itself, a list of groups to be granted to the permission and the list of
     *                groups to remove their granted privileges
     * @see org.jtalks.poulpe.model.dto.branches.BranchAccessChanges#getNewlyAddedGroupsAsArray()
     * @see org.jtalks.poulpe.model.dto.branches.BranchAccessChanges#getRemovedGroups()
     */
    public void changeGrants(PoulpeBranch branch, BranchAccessChanges changes) {
        BasicAclBuilder aclBuilder = new BasicAclBuilder(aclManager).grant(changes.getPermission())
                .setOwner(changes.getNewlyAddedGroupsAsArray()).on(branch).flush();
        aclBuilder.delete(changes.getPermission()).setOwner(changes.getRemovedGroupsAsArray()).on(branch).flush();
    }

    /**
     * Changes the restricting permissions according to the specified changes.
     *
     * @param branch  the branch to change permissions to
     * @param changes contains a permission itself, a list of groups to be restricted from the permission and the list
     *                of groups to remove their restricting privileges
     * @see org.jtalks.poulpe.model.dto.branches.BranchAccessChanges#getNewlyAddedGroupsAsArray()
     * @see org.jtalks.poulpe.model.dto.branches.BranchAccessChanges#getRemovedGroups()
     */
    public void changeRestrictions(PoulpeBranch branch, BranchAccessChanges changes) {
        BasicAclBuilder aclBuilder = new BasicAclBuilder(aclManager).restrict(changes.getPermission())
                .setOwner(changes.getNewlyAddedGroupsAsArray()).on(branch).flush();
        aclBuilder.delete(changes.getPermission()).setOwner(changes.getRemovedGroupsAsArray()).on(branch).flush();
    }

    public BranchAccessList getGroupAccessListFor(PoulpeBranch branch) {
        BranchAccessList branchAccessList = BranchAccessList.create(BranchPermission.getAllAsList());
        List<GroupAce> groupAces = aclManager.getBranchPermissions(branch);
        for(GroupAce groupAce: groupAces){
            branchAccessList.put(groupAce.getBranchPermission(), getGroup(groupAce), groupAce.isGranting());
        }
        return branchAccessList;
    }

    private PoulpeGroup getGroup(GroupAce groupAce) {
        long groupId = extractGroupId(groupAce);
        return groupDao.get(groupId);
    }
    
    // TODO: get rid of it once GroupAce#getGroupId() is created
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
