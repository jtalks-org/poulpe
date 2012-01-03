package org.jtalks.common.security.acl;

import com.google.common.base.Predicate;
import org.jtalks.common.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author stanislav bashkirtsev
 */
public class AclUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MutableAclService mutableAclService;

    public AclUtil(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    /**
     * Get existing ACL record for the entity. If ACL does not exist it will be created.
     *
     * @param entity entity to get is {@link org.springframework.security.acls.model.ObjectIdentity} which is an ACL id
     *               of the entry and find/create its ACL object
     * @return Access Control List for the specified entity
     */
    public ExtendedMutableAcl getAclFor(Entity entity) {
        ObjectIdentity oid = createIdentityFor(entity);
        return getAclFor(oid);
    }

    /**
     * Get existing ACL for identity. If ACL does not exist it will be created.
     *
     * @param oid object identity
     * @return ACL fro this object identity
     */
    public ExtendedMutableAcl getAclFor(ObjectIdentity oid) {
        try {
            return ExtendedMutableAcl.castAndCreate(mutableAclService.readAclById(oid));
        } catch (NotFoundException nfe) {
            return ExtendedMutableAcl.castAndCreate(mutableAclService.createAcl(oid));
        }
    }

    /**
     * Creates {@code ObjectIdentity} for {@code securedObject}
     *
     * @param securedObject object
     * @return identity with {@code securedObject} class name and id
     */
    public ObjectIdentity createIdentityFor(Entity securedObject) {
        if (securedObject.getId() <= 0) {
            throw new IllegalStateException("Object id must be assigned before creating acl.");
        }
        return new ObjectIdentityImpl(securedObject.getClass(), securedObject.getId());
    }

    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     */
    public ExtendedMutableAcl grantPermissionsToSids(List<Sid> sids, List<Permission> permissions, Entity target) {
        return applyPermissionsToSids(sids, permissions, target, true);
    }

    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     */
    public ExtendedMutableAcl restrictPermissionsToSids(List<Sid> sids, List<Permission> permissions, Entity target) {
        return applyPermissionsToSids(sids, permissions, target, false);
    }

    public ExtendedMutableAcl deletePermissionsFromTarget(List<Sid> sids, List<Permission> permissions, Entity target) {
        ObjectIdentity oid = createIdentityFor(target);
        ExtendedMutableAcl acl = ExtendedMutableAcl.castAndCreate(mutableAclService.readAclById(oid));
        deletePermissionsFromAcl(acl, sids, permissions);
        return acl;
    }

    /**
     * Delete permissions from {@code acl} for every sid.
     *
     * @param acl         provided acl
     * @param sids        list of sids
     * @param permissions list of permissions
     */
    public void deletePermissionsFromAcl(ExtendedMutableAcl acl, List<Sid> sids, List<Permission> permissions) {
        List<AccessControlEntry> allEntries = acl.getEntries(); // it's a copy
        List<AccessControlEntry> filtered = newArrayList(filter(allEntries, new BySidAndPermissionFilter(sids, permissions)));
        acl.delete(filtered);
    }


    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     * @param granting    grant if true, restrict if false
     */
    private ExtendedMutableAcl applyPermissionsToSids(List<Sid> sids, List<Permission> permissions, Entity target, boolean granting) {
        ExtendedMutableAcl acl = getAclFor(target);
        deletePermissionsFromAcl(acl, sids, permissions);
        int aclIndex = acl.getEntries().size();
        for (Sid recipient : sids) {
            for (Permission permission : permissions) {
                // add permission to acl for recipient
                acl.insertAce(aclIndex++, permission, recipient, granting);
                logger.debug("Added permission mask {} for Sid {} securedObject {} id {}", new Object[]{
                        permission.getMask(), recipient, target.getClass().getSimpleName(), target.getId()});
            }
        }
        return acl;
    }


    /**
     * Gets the list of Sids and Permissions into the constructor and filters out those {@link AccessControlEntry} whose
     * Sid & Permission is not in the specified lists.
     *
     * @see com.google.common.collect.Iterators#filter(java.util.Iterator, com.google.common.base.Predicate)
     */
    private static class BySidAndPermissionFilter implements Predicate<AccessControlEntry> {
        private final List<Sid> sids;
        private final List<Permission> permissions;

        private BySidAndPermissionFilter(List<Sid> sids, List<Permission> permissions) {
            this.sids = sids;
            this.permissions = permissions;
        }

        @Override
        public boolean apply(@Nullable AccessControlEntry input) {
            if (input == null) {
                return false;
            }
            return sids.contains(input.getSid()) && permissions.contains(input.getPermission());
        }

        @Override
        public boolean equals(@Nullable Object object) {
            return false;
        }
    }
}