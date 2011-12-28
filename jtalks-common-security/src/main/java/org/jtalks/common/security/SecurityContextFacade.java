package org.jtalks.common.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Default implementation of {@link SecurityContextFacade}
 *
 * @author Kirill Afonin
 */
public class SecurityContextFacade {

    /**
     * @return {@code SecurityContext} from {@code SecurityContextHolder}
     */
    public SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    /**
     * Set {@code SecurityContext} to  {@code SecurityContextHolder}
     *
     * @param securityContext {@code SecurityContext} to set.
     */
    public void setContext(SecurityContext securityContext) {
        SecurityContextHolder.setContext(securityContext);
    }
}
