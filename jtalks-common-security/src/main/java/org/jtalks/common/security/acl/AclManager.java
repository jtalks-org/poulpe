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
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains coarse-grained operations with Spring ACL to manage the permissions of Groups & Users for the actions on
 * entities like PoulpeBranch or Topic.
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

    public List<GroupAce> getBranchPermissions(PoulpeBranch branch) {
        MutableAcl branchAcl = aclUtil.getAclFor(branch);
        List<AccessControlEntry> originalAces = branchAcl.getEntries();
        List<GroupAce> resultingAces = new ArrayList<GroupAce>(originalAces.size());
        for (AccessControlEntry entry : originalAces) {
            resultingAces.add(new GroupAce(entry));
        }
        return resultingAces;
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
    public void restrict(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
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
