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