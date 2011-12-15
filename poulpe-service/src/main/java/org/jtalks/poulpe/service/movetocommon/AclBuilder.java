package org.jtalks.poulpe.service.movetocommon;

import org.jtalks.common.model.entity.Entity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import java.util.List;

/**
 * Builder for building ACL permissions.
 * <p/>
 * <b>Examples:</b>
 * <p>You want grant &quot;admin&quot; permission to user with name &quot;jack&quot;
 * on {@code Topic topic = ...;} object. Object must have <b>assigned id</b> and be inherited from
 * {@link org.jtalks.common.model.entity.Entity}.
 * Create builder instance({@link org.jtalks.common.service.security.AclBuilderImpl}):
 * {@code AclBuilder builder = new AclBuilderImpl(manager, AclBuilderImpl.Action.GRANT); }
 * Granting permissions:
 * {@code builder.user(&quot;jack&quot;).admin().on(topic);}
 * Permissions will be created when you call {@link AclBuilder#on(org.jtalks.common.model.entity.Entity)}
 * </p>
 * <p>
 * You want grant &quot;read&quot; and &quot;delete&quot; permissions to user with name &quot;jack&quot;
 * and role &quot;ROLE_ADMIN&quot; on {@code Topic topic = ...;} object.
 * {@code builder.user(&quot;jack&quot;).role(&quot;ROLE_ADMIN&quot;).read().delete().on(topic);}
 * </p>
 *
 * @author Kirill Afonin
 */
public interface AclBuilder {
    /**
     * Add user with given username to sids.
     *
     * @param username username
     * @return this builder instance
     */
    AclBuilder user(String username);

    /**
     * Add role with given name to sids.
     *
     * @param role role name
     * @return this builder instance
     */
    AclBuilder role(String role);

    /**
     * Admin permission.
     * This permission NOT contains other permissions.
     *
     * @return this builder instance
     */
    AclBuilder admin();

    /**
     * Read permission.
     *
     * @return this builder instance
     */
    AclBuilder read();

    /**
     * Write permission.
     *
     * @return this builder instance
     */
    AclBuilder write();

    /**
     * Delete permission.
     *
     * @return this builder instance
     */
    AclBuilder delete();

    /**
     * Create permission.
     *
     * @return this builder instance
     */
    AclBuilder create();

    /**
     * Set secured object and perform actions with permissions.
     * This method also clears builder and you can reuse it.
     *
     * @param object secured object
     * @return clean builder with same action
     */
    AclBuilder on(Entity object);

    /**
     * Check that sid (role or user) with given name in builder.
     *
     * @param name name of sid
     * @return {@code true} if sid in builder
     */
    boolean containsSid(String name);

    /**
     * Check that permission in builder.
     *
     * @param permission permission object for checking
     * @return {@code true} if permission in builder
     */
    boolean hasPermission(Permission permission);

    /**
     * Get list of sids(copy).
     *
     * @return copy of list of sids
     */
    List<Sid> getSids();

    /**
     * Get list of permissions(copy).
     *
     * @return copy of list of permissions
     */
    List<Permission> getPermissions();
}

