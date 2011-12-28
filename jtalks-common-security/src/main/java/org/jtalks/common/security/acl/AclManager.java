package org.jtalks.common.security.acl;

import org.jtalks.common.model.entity.Entity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import java.util.List;

/**
 * Interface that contains operations with ACL.
 *
 * @author Kirill Afonin
 */
public interface AclManager {
    /**
     * Grant permissions from list to every sid in list on {@code target} object.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    void grant(List<Sid> sids, List<Permission> permissions, Entity target);

    /**
     * Revoke permissions from lists for every sid in list on {@code target} entity
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    void restrict(List<Sid> sids, List<Permission> permissions, Entity target);

    /**
     * Delete permissions from list for every sid in list on {@code target} object.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    void delete(List<Sid> sids, List<Permission> permissions, Entity target);

    /**
     * Delete object from acl. All permissions will be removed.
     *
     * @param clazz object {@code Class}
     * @param id    object id
     */
    void deleteFromAcl(Class clazz, long id);
}
