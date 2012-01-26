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
package org.jtalks.common.security.acl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Branch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Contains coarse-grained operations with Spring ACL to manage the permissions of Groups & Users for the actions on
 * entities like Branch or Topic.
 *
 * @author Kirill Afonin
 */
public class AclManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MutableAclService mutableAclService;
    private AclUtil aclUtil;

    public AclManager(@Nonnull MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
        aclUtil = new AclUtil(mutableAclService);
    }

    /**
     * Gets each group that has some permissions on actions with the specified branch. So the resulting table will
     * contain the mask of permission, the group id, and the flag whether the permission is granting. So considering
     * that in the database there are records like this:
     * <pre>
     * --------------------------------------------------------------
     * | Permission mask | User Group ID | Granting                 |
     * |------------------------------------------------------------|
     * | 3 (mask: 00011) |      1        | true (allows action)     |
     * | 3 (mask: 00011) |      2        | false (restricts action) |
     * | 5 (mask: 00101) |      1        | true (allows action)     |
     * --------------------------------------------------------------
     * </pre> We'll get results like this:
     * <pre>
     * ----------------------------------
     * |                 | 1    |   2   |
     * |--------------------------------|
     * | 3 (mask: 00011) | true | false |
     * | 5 (mask: 00101) | true |       |
     * ----------------------------------
     * </pre>
     * In this case we have 2 permissions and 2 groups, and both the permissions are granted to the group with id 1 and
     * the first permission is restricted to the group with id 2.
     *
     * @param branch a branch to get all ACL entries for it (all the groups that have granting or restricting
     *               permissions on it)
     * @return a table where the row IDs are permission masks, columns are group IDs, and the values are flags whether
     *         the permission is granting to the group or restricting:
     */
    public Table<Integer, Long, Boolean> getBranchPermissions(Branch branch) {
        Table<Integer, Long, Boolean> groupIds = HashBasedTable.create();
        MutableAcl branchAcl = aclUtil.getAclFor(branch);
        for (AccessControlEntry entry : branchAcl.getEntries()) {
            String groupId = ((UserGroupSid) entry.getSid()).getGroupId();
            int mask = entry.getPermission().getMask();
            groupIds.put(mask, Long.parseLong(groupId), entry.isGranting());
        }
        return groupIds;
    }

    /**
     * Grant permissions from list to every sid in list on {@code target} object.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    public void grant(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
        MutableAcl acl = aclUtil.grant(sids, permissions, target);
        mutableAclService.updateAcl(acl);
    }

    /**
     * Revoke permissions from lists for every sid in list on {@code target} entity
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    public void restrict(List<Sid> sids, List<Permission> permissions, Entity target) {
        MutableAcl acl = aclUtil.restrict(sids, permissions, target);
        mutableAclService.updateAcl(acl);
    }

    /**
     * Delete permissions from list for every sid in list on {@code target} object.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    public void delete(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
        MutableAcl acl = aclUtil.delete(sids, permissions, target);
        mutableAclService.updateAcl(acl);
    }

    /**
     * Delete object from acl. All permissions will be removed.
     *
     * @param clazz object {@code Class}
     * @param id    object id
     */
    public void deleteFromAcl(Class clazz, long id) {
        if (id <= 0) {
            throw new IllegalStateException("Object id must be greater then 0.");
        }
        ObjectIdentity oid = new ObjectIdentityImpl(clazz, id);
        mutableAclService.deleteAcl(oid, true);
        logger.debug("Deleted securedObject" + clazz.getSimpleName() + " with id:" + id);
    }

    @VisibleForTesting
    void setAclUtil(AclUtil aclUtil) {
        this.aclUtil = aclUtil;
    }
}
