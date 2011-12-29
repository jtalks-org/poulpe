package org.jtalks.poulpe.logic;

import com.google.common.collect.Table;
import org.jtalks.common.security.acl.AclManagerImpl;
import org.jtalks.common.security.acl.BasicAclBuilder;
import org.jtalks.poulpe.model.dao.GroupDao;
import org.jtalks.poulpe.model.dto.branches.BranchAccessChanges;
import org.jtalks.poulpe.model.dto.branches.BranchAccessList;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.permissions.JtalksPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Responsible for allowing, restricting or deleting the permissions of the User Groups to actions related to the
 * Branches.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AclManagerImpl aclManager;
    private final GroupDao groupDao;

    public BranchPermissionManager(AclManagerImpl aclManager, GroupDao groupDao) {
        this.aclManager = aclManager;
        this.groupDao = groupDao;
    }

    public void grantPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        new BasicAclBuilder(aclManager).grant(permission).setOwner(groups.toArray(new Group[]{})).on(branch).flush();

    }

    public void restrictPermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        new BasicAclBuilder(aclManager).restrict(permission).setOwner(groups.toArray(new Group[]{})).on(branch).flush();
    }

    public void deletePermissions(Branch branch, JtalksPermission permission, Collection<Group> groups) {
        new BasicAclBuilder(aclManager).delete(permission).setOwner(groups.toArray(new Group[]{})).on(branch).flush();
    }

    public void changeGrants(Branch branch, BranchAccessChanges changes) {
        BasicAclBuilder aclBuilder = new BasicAclBuilder(aclManager).grant(changes.getPermission())
                .setOwner(changes.getNewlyAddedPermissionsAsArray()).on(branch).flush();
        aclBuilder.delete(changes.getPermission()).setOwner(changes.getRemovedPermissionsAsArray()).on(branch).flush();
    }

    public void changeRestrictions(BranchAccessChanges changes) {

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
