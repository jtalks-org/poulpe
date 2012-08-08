package org.jtalks.poulpe.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * In standard Spring Security implementation of the strategy it was checking a lot of stuff we didn't want to check,
 * e.g. if some Principal (User) created an object, then the other Principal couldn't change its ACLs and we were
 * getting <i>Unable to locate a matching ACE for passed permissions and SIDs</i>. In our strategy Permission will be
 * granted to any principal if it is authenticated.
 *
 * @author Elena Lepaeva
 */
public class AclAuthorizationStrategyImpl implements AclAuthorizationStrategy {

    /**
     * Method checks that SecurityContextHolder contains authenticated principal.
     *
     * @param acl        access control list (is not used, may be null).
     * @param changeType AclAuthorizationStrategy change type constant (is not used, may be null).
     */
    @Override
    public void securityCheck(Acl acl, int changeType) {
        if ((SecurityContextHolder.getContext() == null)
                || (SecurityContextHolder.getContext().getAuthentication() == null)
                || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AccessDeniedException("Authenticated principal required to operate with ACLs");
        }
    }
}