package org.jtalks.common.security.acl;

import com.google.common.collect.Lists;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Group;
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
 * @see <a href="http://static.springsource.org/spring-security/site/docs/3.0.x/reference/domain-acls.html">Spring ACL
 *      documentation</a>
 */
@NotThreadSafe
public class BasicAclBuilder {
    private final List<Permission> permissionsToDelete = new ArrayList<Permission>();
    private final List<Permission> permissionsToGrant = new ArrayList<Permission>();
    private final List<Permission> permissionsToRestrict = new ArrayList<Permission>();
    private final AclManager aclManager;
    private final List<Sid> sids = new ArrayList<Sid>();
    private Entity target;

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
     * @param owner the user to grant to/restrict from the permissions
     * @return this
     */
    public BasicAclBuilder setOwner(@Nonnull User owner) {
        sids.add(new PrincipalSid(owner.getUsername()));
        return this;
    }

    /**
     * Sets the user group that will be granted (or restrict from) the permissions. Thus, all the users in that group
     * will be granted as well.
     *
     * @param owners the user groups that are the owner of the permissions
     * @return this
     */
    public BasicAclBuilder setOwner(@Nonnull Group... owners) {
        for (Group group : owners) {
            sids.add(new UserGroupSid(group));
        }
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
    public BasicAclBuilder grant(@Nonnull Permission... permission) {
        permissionsToGrant.addAll(Lists.newArrayList(permission));
        return this;
    }


    /**
     * Restrict the owner (recipient, {@link Sid}) to perform the specified actions (permissions).
     *
     * @param permission the permission to restrict to the owner ({@link Sid})
     * @return this
     */
    public BasicAclBuilder restrict(@Nonnull Permission... permission) {
        permissionsToRestrict.addAll(Lists.newArrayList(permission));
        return this;
    }

    /**
     * Removes the specified permission from the ACL tables.
     *
     * @param permission the permission to take away from the owner ({@link Sid})
     * @return this
     */
    public BasicAclBuilder delete(@Nonnull Permission... permission) {
        permissionsToDelete.addAll(Lists.newArrayList(permission));
        return this;
    }

    /**
     * Flushes all the changes to the manager (which should save everything to DB).
     *
     * @return this
     */
    public BasicAclBuilder flush() {
        executeUpdate();
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
    public BasicAclBuilder on(@Nonnull Entity object) {
        target = object;
        return this;
    }

    /**
     * Flushes the state to the {@link AclManager} (which in turns should flush everything to DB) and then clears the
     * state.
     */
    private void executeUpdate() {
        throwIfPermissionsOrSidsEmpty();
        executeGrant(permissionsToGrant);
        executeDelete(permissionsToDelete);
        executeRestrict(permissionsToRestrict);
        clearState();
    }

    private void executeRestrict(List<Permission> permissionsToRestrict) {
        if (!permissionsToRestrict.isEmpty()) {
            aclManager.restrict(clone(sids), clone(permissionsToRestrict), target);
        }
    }

    private void executeDelete(List<Permission> permissionsToRevoke) {
        if (!permissionsToRevoke.isEmpty()) {
            aclManager.delete(clone(sids), clone(permissionsToRevoke), target);
        }
    }

    private void executeGrant(List<Permission> permissionsToGrant) {
        if (!permissionsToGrant.isEmpty()) {
            aclManager.grant(clone(sids), clone(permissionsToGrant), target);
        }
    }

    private <T> List<T> clone(List<T> list) {
        return new ArrayList<T>(list);
    }

    /**
     * Clears the state of the builder (cleans all the permissions, sids, target) so that the object can be reused.
     *
     * @return this
     */
    public BasicAclBuilder clearState() {
        permissionsToGrant.clear();
        permissionsToDelete.clear();
        permissionsToRestrict.clear();
        sids.clear();
        target = null;
        return this;
    }

    private void throwIfPermissionsOrSidsEmpty() throws IllegalArgumentException {
        assertAllPermissionsEmpty();
        if (sids.isEmpty()) {
            throw new IllegalStateException("You can't grant permissions without sids");
        }
        if(target == null){
            throw new IllegalStateException("The target is null! Set the target before flushing");
        }
    }

    private void assertAllPermissionsEmpty() {
        if (permissionsToDelete.isEmpty() && permissionsToGrant.isEmpty() && permissionsToRestrict.isEmpty()) {
            throw new IllegalStateException("No permissions to flush! Specify at least one Permission.");
        }
    }

}
