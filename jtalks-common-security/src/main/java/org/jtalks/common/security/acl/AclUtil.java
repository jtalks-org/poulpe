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

import com.google.common.base.Predicate;
import org.jtalks.common.model.entity.Entity;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

/**
 * The fine grained utilities to work with Spring ACL.
 *
 * @author stanislav bashkirtsev
 */
public class AclUtil {
    private final MutableAclService mutableAclService;

    public AclUtil(@Nonnull MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    /**
     * Get existing ACL record for the entity. If ACL does not exist it will be created.
     *
     * @param entity entity to get is {@link ObjectIdentity} which is an ACL id of the entry and find/create its ACL
     *               object
     * @return Access Control List for the specified entity
     */
    public ExtendedMutableAcl getAclFor(Entity entity) {
        ObjectIdentity oid = createIdentityFor(entity);
        return getAclFor(oid);
    }

    /**
     * Get existing ACL for identity. If ACL does not exist it will be created.
     *
     * @param oid object identity to get its ACL
     * @return ACL or the specified object identity
     */
    public ExtendedMutableAcl getAclFor(ObjectIdentity oid) {
        try {
            return ExtendedMutableAcl.castAndCreate(mutableAclService.readAclById(oid));
        } catch (NotFoundException nfe) {
            return ExtendedMutableAcl.castAndCreate(mutableAclService.createAcl(oid));
        }
    }

    /**
     * Creates {@code ObjectIdentity} for {@code securedObject}.
     *
     * @param securedObject object to create its object identity (which is an ID for the Spring ACL that identifies the
     *                      objects Sids have permissions for)
     * @return identity with {@code securedObject} class name and id
     * @throws IllegalArgumentException if the specified entity doesn't have the id (it's {@code id <= 0})
     */
    public ObjectIdentity createIdentityFor(Entity securedObject) {
        if (securedObject.getId() <= 0) {
            throw new IllegalArgumentException("Object id must be assigned before creating acl.");
        }
        return new ObjectIdentityImpl(securedObject.getClass(), securedObject.getId());
    }

    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     * @return the ACL that serves the {@code sids}
     */
    public ExtendedMutableAcl grant(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
        return applyPermissionsToSids(sids, permissions, target, true);
    }

    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     * @return the mutable acl that was created/retrieved while the operation
     */
    public ExtendedMutableAcl restrict(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
        return applyPermissionsToSids(sids, permissions, target, false);
    }

    public ExtendedMutableAcl delete(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
        ObjectIdentity oid = createIdentityFor(target);
        ExtendedMutableAcl acl = ExtendedMutableAcl.castAndCreate(mutableAclService.readAclById(oid));
        deletePermissionsFromAcl(acl, sids, permissions);
        return acl;
    }

    /**
     * Delete permissions from {@code acl} for every sid.
     *
     * @param acl         the acl to remove the sid permissions from it
     * @param sids        list of sids to remove their permissions
     * @param permissions list of permissions to remove them
     */
    public void deletePermissionsFromAcl(
            ExtendedMutableAcl acl, List<? extends Sid> sids, List<Permission> permissions) {
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
     * @return the ACL that manages the specified {@code target} and its Sids & Permissions
     */
    private ExtendedMutableAcl applyPermissionsToSids(
            List<? extends Sid> sids, List<Permission> permissions, Entity target, boolean granting) {
        ExtendedMutableAcl acl = getAclFor(target);
        deletePermissionsFromAcl(acl, sids, permissions);
        acl.addPermissions(sids, permissions, granting);
        return acl;
    }


    /**
     * Gets the list of Sids and Permissions into the constructor and filters out those {@link AccessControlEntry} whose
     * Sid & Permission is not in the specified lists.
     *
     * @see com.google.common.collect.Iterators#filter(java.util.Iterator, com.google.common.base.Predicate)
     */
    private static class BySidAndPermissionFilter implements Predicate<AccessControlEntry> {
        private final List<? extends Sid> sids;
        private final List<Permission> permissions;

        /**
         * @param sids        to find {@link AccessControlEntry}s that contain them
         * @param permissions to find the {@link AccessControlEntry}s where specified {@code sids} have these
         *                    permissions
         */
        private BySidAndPermissionFilter(@Nonnull List<? extends Sid> sids, @Nonnull List<Permission> permissions) {
            this.sids = sids;
            this.permissions = permissions;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean apply(@Nullable AccessControlEntry input) {
            if (input == null) {
                return false;
            }
            return sids.contains(input.getSid()) && permissions.contains(input.getPermission());
        }

        /**
         * Always return {@code false}, we don't need this functionality.
         *
         * @param object who cares
         * @return always {@code false}
         */
        @Override
        public boolean equals(@Nullable Object object) {
            return false;
        }
    }
}