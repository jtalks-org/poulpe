package org.jtalks.poulpe.service.security;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.model.entity.Branch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementetion of {@link AclManager} interface. Manage ACLs using Spring Security facilities.
 *
 * @author Kirill Afonin
 */
public class AclManagerImpl implements AclManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MutableAclService mutableAclService;

    /**
     * Constructor creates instance.
     *
     * @param mutableAclService spring security service for ACLs
     */
    public AclManagerImpl(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    public Map<Long, Boolean> getGroups(Branch branch) {
        Map<Long, Boolean> groupIds = new HashMap<Long, Boolean>();
        ObjectIdentity branchIdentity = createIdentityFor(branch);
        MutableAcl branchAcl = getAclFor(branchIdentity);
        for (AccessControlEntry entry : branchAcl.getEntries()) {
            String groupId = ((UserGroupSid) entry.getSid()).getGroupId();
            groupIds.put(Long.parseLong(groupId), entry.isGranting());
        }
        return groupIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void grant(List<Sid> sids, List<Permission> permissions, Entity target) {
        ObjectIdentity oid = createIdentityFor(target);
        MutableAcl acl = getAclFor(oid);
        applyPermissionsToSids(sids, permissions, target, acl, true);
        mutableAclService.updateAcl(acl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restrict(List<Sid> sids, List<Permission> permissions, Entity target) {
        ObjectIdentity oid = createIdentityFor(target);
        MutableAcl acl = getAclFor(oid);
        applyPermissionsToSids(sids, permissions, target, acl, false);
        mutableAclService.updateAcl(acl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * Get existing ACL for identity. If ACL does not exist it will be created.
     *
     * @param oid object identity
     * @return ACL fro this object identity
     */
    private MutableAcl getAclFor(ObjectIdentity oid) {
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }
        return acl;
    }

    /**
     * Delete permissions from {@code acl} for every sid.
     *
     * @param acl         provided acl
     * @param sids        list of sids
     * @param permissions list of permissions
     */
    private void deletePermissionsFromAcl(MutableAcl acl, List<Sid> sids, List<Permission> permissions) {
        List<AccessControlEntry> entries = acl.getEntries(); // it's copy
        int i = 0;
        // search for sid-permission pair
        for (AccessControlEntry entry : entries) {
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

    /**
     * {@inheritDoc}
     */
    @Override
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