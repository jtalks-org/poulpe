package org.jtalks.poulpe.service.security;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.movetocommon.AclManager;
import org.jtalks.poulpe.service.movetocommon.JtalksPermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author stanislav bashkirstsev
 */
//TODO: this is a draft, it's still need to be finished
public class BasicAclBuilder {
    /**
     * Possible actions that will be performed when builder finished.
     */
    public enum Action {
        DELETE, GRANT, REVOKE
    }

    private final List<Sid> sids = new ArrayList<Sid>();
    private final List<Permission> permissions = new ArrayList<Permission>();
    private final List<Permission> unmodifiablePermissions = Collections.unmodifiableList(permissions);
    private final AclManager aclManager;
    private final Action action;
    private Entity target;

    /**
     * Constructor.
     *
     * @param aclManager instance of manager
     * @param action     action that will be executed when you call {@link org.jtalks.poulpe.service.movetocommon.AclBuilder#on(Entity)
     */
    public BasicAclBuilder(AclManager aclManager, Action action) {
        this.aclManager = aclManager;
        this.action = action;
    }

    public BasicAclBuilder setOwner(@Nonnull User owner){
        sids.add(new PrincipalSid(owner.getUsername()));
        return this;
    }

    public BasicAclBuilder setOwner(@Nonnull Group owner){
        sids.add((new UserGroupSid(owner)));
        return this;
    }

    public BasicAclBuilder on(Entity object) {
        throwIfPermissionsOrSidsEmpty();
        target = object;
        executeUpdate();
        return this;
    }

    public BasicAclBuilder addPermission(JtalksPermission permission) {
        permissions.add(permission);
        return this;
    }

    private void throwIfPermissionsOrSidsEmpty() {
        if (sids.isEmpty() || permissions.isEmpty()) {
            throw new IllegalStateException("You can't grant permissions without sids or permissions");
        }
    }

    /**
     * Performs selected action.
     */
    private void executeUpdate() {
        if (action == Action.GRANT) {
            aclManager.grant(sids, permissions, target);
        } else if (action == Action.REVOKE) {
            aclManager.revoke(sids, permissions, target);
        } else {
            aclManager.delete(sids, permissions, target);
        }
        sids.clear();
        permissions.clear();
    }

    public boolean containsSid(String name) {
        for (Sid sid : sids) {
            if (sid instanceof PrincipalSid) {
                if (((PrincipalSid) sid).getPrincipal().equals(name)) {
                    return true;
                }
            } else if (((GrantedAuthoritySid) sid).getGrantedAuthority().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public List<Sid> getSids() {
        return new ArrayList<Sid>(sids);
    }

    public List<Permission> getPermissions() {
        return unmodifiablePermissions;
    }
}
