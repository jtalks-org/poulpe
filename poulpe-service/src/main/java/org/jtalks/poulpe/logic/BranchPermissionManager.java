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

import com.google.common.collect.Table;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.BasicAclBuilder;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.permissions.BranchPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Responsible for allowing, restricting or deleting the permissions of the User Groups to actions related to the
 * Branches.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
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
    public void changeGrants(Branch branch, BranchAccessChanges changes) {
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
    public void changeRestrictions(Branch branch, BranchAccessChanges changes) {
        BasicAclBuilder aclBuilder = new BasicAclBuilder(aclManager).restrict(changes.getPermission())
                .setOwner(changes.getNewlyAddedGroupsAsArray()).on(branch).flush();
        aclBuilder.delete(changes.getPermission()).setOwner(changes.getRemovedGroupsAsArray()).on(branch).flush();
    }

    public BranchAccessList getGroupAccessListFor(Branch branch) {
        BranchAccessList branchAccessList = BranchAccessList.create(BranchPermission.getAllAsList());
        Table<Integer, Long, Boolean> branchPermissions = aclManager.getBranchPermissions(branch);
        for (BranchPermission permission : branchAccessList.getPermissions()) {
            Map<Long, Boolean> row = branchPermissions.row(permission.getMask());
            for (Map.Entry<Long, Boolean> entry : row.entrySet()) {
                Group group;
                group = groupDao.get(entry.getKey());
                if (group != null) {
                    branchAccessList.put(permission, group, entry.getValue());
                } else {
                    logger.warn("A group with ID {} was removed, but this ID is still registered as a Permission owner in " +
                            "ACL tables.", entry.getKey());
                }
            }
        }
        return branchAccessList;
    }
}
