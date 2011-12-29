package org.jtalks.common.security.acl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Branch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;

import java.util.List;

/**
 * Interface that contains operations with ACL.
 *
 * @author Kirill Afonin
 */
public class AclManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MutableAclService mutableAclService;

    /**
     * Constructor creates instance.
     *
     * @param mutableAclService spring security service for ACLs
     */
    public AclManager(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    /**
     * Gets each group that has some permissions on actions with the specified branch. So the resulting table will
     * contain the mask of permission, the group id, and the flag whether the permission is granting. So this results in
     * something like this:
     * <pre>
     * --------------------------------------------------------------
     * | Permission mask | User Group ID | Granting                 |
     * -------------------------------------------------------------|
     * | 3 (mask: 00011) |      1        | true (allows action)     |
     * | 3 (mask: 00011) |      2        | false (restricts action) |
     * | 5 (mask: 00101) |      1        | true( allows action)     |
     * --------------------------------------------------------------
     * </pre>In this case we have 2 permissions and 2 groups, and both the permissions are granted to the group with id
     * 1 and the first permission is restricted to the group with id 2.
     *
     * @param branch a branch to get all ACL entries for it (all the groups that have granting or restricting
     *               permissions on it)
     * @return a table where the rows are permission masks, columns are group IDs, and the values are flags whether the
     *         permission is granting to the group or restricting
     */
    public Table<Integer, Long, Boolean> getBranchPermissions(Branch branch) {
        Table<Integer, Long, Boolean> groupIds = HashBasedTable.create();
        MutableAcl branchAcl = getAclFor(branch);
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
    public void grant(List<Sid> sids, List<Permission> permissions, Entity target) {
        MutableAcl acl = getAclFor(target);
        applyPermissionsToSids(sids, permissions, target, acl, true);
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
        MutableAcl acl = getAclFor(target);
        applyPermissionsToSids(sids, permissions, target, acl, false);
        mutableAclService.updateAcl(acl);
    }

    /**
     * Delete permissions from list for every sid in list on {@code target} object.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    public void delete(List<Sid> sids, List<Permission> permissions, Entity target) {
        ObjectIdentity oid = createIdentityFor(target);
        MutableAcl acl = (MutableAcl) mutableAclService.readAclById(oid);
        deletePermissionsFromAcl(acl, sids, permissions);
        mutableAclService.updateAcl(acl);
    }

    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     * @param acl         ACL of this object
     * @param granting    grant if true, restrict if false
     */
    private void applyPermissionsToSids(List<Sid> sids, List<Permission> permissions, Entity target, MutableAcl acl, boolean granting) {
        deletePermissionsFromAcl(acl, sids, permissions);
        int aclIndex = acl.getEntries().size();
        for (Sid recipient : sids) {
            for (Permission permission : permissions) {
                // add permission to acl for recipient
                acl.insertAce(aclIndex++, permission, recipient, granting);
                logger.debug("Added permission mask {} for Sid {} securedObject {} id {}",
                        new Object[]{permission.getMask(), recipient, target.getClass().getSimpleName(), target
                                .getId()});
            }
        }
    }

    /**
     * Get existing ACL record for the entity. If ACL does not exist it will be created.
     *
     * @param entity entity to get is {@link ObjectIdentity} which is an ACL id of the entry and find/create its ACL
     *               object
     * @return Access Control List for the specified entity
     */
    protected MutableAcl getAclFor(Entity entity) {
        ObjectIdentity oid = createIdentityFor(entity);
        return getAclFor(oid);
    }

    /**
     * Get existing ACL for identity. If ACL does not exist it will be created.
     *
     * @param oid object identity
     * @return ACL fro this object identity
     */
    private MutableAcl getAclFor(ObjectIdentity oid) {
        try {
            return (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            return mutableAclService.createAcl(oid);
        }
    }

    /**
     * Delete permissions from {@code acl} for every sid.
     *
     * @param acl         provided acl
     * @param sids        list of sids
     * @param permissions list of permissions
     */
    private void deletePermissionsFromAcl(MutableAcl acl, List<Sid> sids, List<Permission> permissions) {
        List<AccessControlEntry> entries = acl.getEntries(); // it's a copy
        int i = 0;
        for (AccessControlEntry entry : entries) { // search for sid-permission pair
            for (Sid recipient : sids) {
                for (Permission permission : permissions) {
                    if (entry.getSid().equals(recipient) && entry.getPermission().equals(permission)) {
                        acl.deleteAce(i); // delete from original list
                        i--; // because list item deleted in original list
                    }
                }
            }
            i++;
        }
    }

//    private void deletePermissionFromAcl(MutableAcl acl, List<Sid> sids, Permission permission){
//        List<AccessControlEntry> entries = acl.getEntries();
//        for(int i = 0; i < entries.size(); i++){
//            AccessControlEntry entry = entries.get(i);
//            entry.getSid();
//            acl.deleteAce();
//        }
//    }

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

    /**
     * Creates {@code ObjectIdentity} for {@code securedObject}
     *
     * @param securedObject object
     * @return identity with {@code securedObject} class name and id
     */
    private ObjectIdentity createIdentityFor(Entity securedObject) {
        if (securedObject.getId() <= 0) {
            throw new IllegalStateException("Object id must be assigned before creating acl.");
        }
        return new ObjectIdentityImpl(securedObject.getClass(), securedObject.getId());
    }
}
