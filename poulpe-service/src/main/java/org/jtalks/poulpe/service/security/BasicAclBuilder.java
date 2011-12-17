package org.jtalks.poulpe.service.security;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.movetocommon.AclManager;
import org.jtalks.poulpe.service.movetocommon.JtalksPermission;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import ru.javatalks.utils.general.Assert;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;

/**
 * This builder is dedicated to easily define what objects ({@link Sid}s) will own {@link Permission}s to do actions on
 * domain objects ({@link #on(Entity)}. <br/> If you want to add handy methods (e.g. fluent methods for giving
 * permissions), extend this class.
 *
 * @author stanislav bashkirstsev
 * @see <a href="http://static.springsource.org/spring-security/site/docs/3.0.x/reference/domain-acls.html">Spring
 *      ACL documentation</a>
 */
@NotThreadSafe
public class BasicAclBuilder {
    private final List<Permission> permissionsToRevoke = new ArrayList<Permission>();
    private final List<Permission> permissionsToGrant = new ArrayList<Permission>();
    private final AclManager aclManager;
    private Entity target;
    private Sid sid;

    /**
     * Constructs the builder with mandatory {@link AclManager}, builder will delegate the saving of its state to the
     * acl manager at some point.
     *
     * @param aclManager the ACL manager that will be used to save the state of the builder to the DB
     */
    public BasicAclBuilder(@Nonnull AclManager aclManager) {
        Assert.throwIfNull(aclManager, "acl manager");
        this.aclManager = aclManager;
    }

    /**
     * Sets the user that will be granted (or revoked from) the permissions.
     *
     * @param owner the user to grant to/revoke from the permissions
     * @return this
     */
    public BasicAclBuilder setOwner(@Nonnull User owner) {
        sid = new PrincipalSid(owner.getUsername());
        return this;
    }

    /**
     * Sets the user group that will be granted (or revoke from) the permissions. Thus, all the users in that group will
     * be granted as well.
     *
     * @param owner the user group that is the owner of the permissions
     * @return this
     */
    public BasicAclBuilder setOwner(@Nonnull Group owner) {
        sid = new UserGroupSid(owner);
        return this;
    }

    /**
     * Adds a permission to the list of {@link Sid}s permissions, in other words grants the permission to the user (even
     * if it effectively is a restriction, it's still 'granting' since a restriction is described with the same class as
     * regular permission).
     *
     * @param permission the permission to grant to the owner ({@link Sid})
     * @return this
     */
    public BasicAclBuilder grant(@Nonnull JtalksPermission permission) {
        permissionsToGrant.add(permission);
        return this;
    }

    /**
     * Adds a permission to the list of {@link Sid}s permissions, in other words grants the permission to the user (even
     * if it effectively is a restriction, it's still 'granting' since a restriction is described with the same class as
     * regular permission).
     *
     * @param permission the permission to grant to the owner ({@link Sid})
     * @return this
     */
    public BasicAclBuilder revoke(@Nonnull JtalksPermission permission) {
        permissionsToRevoke.add(permission);
        return this;
    }

    /**
     * Performs updates (flushes the state to the {@link AclManager} which should save everything to DB) and clears the
     * state of the builder so that you can reuse it for next objects.
     *
     * @param object an object the permission is given on. So {@link Sid}s have {@link Permission}s to do something on
     *               this object.
     * @return this
     */
    public BasicAclBuilder on(Entity object) {
        throwIfPermissionsOrSidsEmpty();
        target = object;
        executeUpdate();
        return this;
    }

    /**
     * Flushes the state to the {@link AclManager} (which in turns should flush everything to DB) and then clears the
     * state.
     */
    private void executeUpdate() {
        executeGrant(permissionsToGrant);
        executeRevoke(permissionsToRevoke);
        clearState();
    }

    private void executeRevoke(List<Permission> permissionsToRevoke) {
        if (!permissionsToRevoke.isEmpty()) {
            aclManager.delete(Lists.newArrayList(sid), permissionsToRevoke, target);
        }
    }

    private void executeGrant(List<Permission> permissionsToGrant) {
        if (!permissionsToGrant.isEmpty()) {
            aclManager.grant(Lists.newArrayList(sid), permissionsToGrant, target);
        }
    }

    /**
     * Clears the state of the builder (cleans all the permissions, sids, target) so that the object can be reused.
     *
     * @return this
     */
    public BasicAclBuilder clearState() {
        permissionsToGrant.clear();
        permissionsToRevoke.clear();
        target = null;
        sid = null;
        return this;
    }

    private void throwIfPermissionsOrSidsEmpty() throws IllegalArgumentException {
        if (sid == null || (permissionsToGrant.isEmpty() || permissionsToRevoke.isEmpty())) {
            throw new IllegalStateException("You can't grant permissions without sids or permissions");
        }
    }

}
