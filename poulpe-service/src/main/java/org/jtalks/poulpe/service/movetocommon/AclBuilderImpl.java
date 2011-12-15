package org.jtalks.poulpe.service.movetocommon;


import org.jtalks.common.model.entity.Entity;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kirill Afonin
 */
public class AclBuilderImpl implements AclBuilder {
    /**
     * Possible actions that will be performed when builder finished.
     */
    public enum Action {
        DELETE, GRANT, REVOKE
    }

    private List<Sid> sids = new ArrayList<Sid>();
    private List<Permission> permissions = new ArrayList<Permission>();
    private Entity target;
    private AclManager aclManager;
    private Action action;

    /**
     * Constructor.
     *
     * @param aclManager instance of manager
     * @param action     action that will be executed when you call
     *                   {@link AclBuilder#on(Entity)
     */
    public AclBuilderImpl(AclManager aclManager, Action action) {
        this.aclManager = aclManager;
        this.action = action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder user(String username) {
        sids.add(new PrincipalSid(username));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder role(String role) {
        sids.add(new GrantedAuthoritySid(role));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder admin() {
        permissions.add(BasePermission.ADMINISTRATION);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder read() {
        permissions.add(BasePermission.READ);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder write() {
        permissions.add(BasePermission.WRITE);
        return this;
    }

    /**
     * Restricts
     * @return
     * @see BranchPermissions.RESTRICT
     */
    public AclBuilder restrict() {
        permissions.add(BranchPermissions.RESTRICT);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder delete() {
        permissions.add(BasePermission.DELETE);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder create() {
        permissions.add(BasePermission.CREATE);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder on(Entity object) {
        target = object;
        if (sids.isEmpty() || permissions.isEmpty()) {
            throw new IllegalStateException("You can't grant permissions without sids or permissions");
        }
        executeUpdate();
        return this;
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

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sid> getSids() {
        return new ArrayList<Sid>(sids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Permission> getPermissions() {
        return new ArrayList<Permission>(permissions);
    }
}
