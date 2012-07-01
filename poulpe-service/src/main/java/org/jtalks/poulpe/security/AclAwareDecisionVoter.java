package org.jtalks.poulpe.security;

import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.UserService;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;

/**
 * @author dionis
 *         6/26/12 10:28 PM
 */
public class AclAwareDecisionVoter implements AccessDecisionVoter {
    private static final String AUTHORIZED = "authorizedPoulpeUser";
    private final AccessDecisionVoter baseVoter;
    private final UserService userService;

    public AclAwareDecisionVoter(UserService userService) {
        this(new WebExpressionVoter(), userService);
    }

    AclAwareDecisionVoter(AccessDecisionVoter baseVoter, UserService userService) {
        this.baseVoter = baseVoter;
        this.userService = userService;
    }

    public RequestAttributes getRequestAttributes() {
        return RequestContextHolder.currentRequestAttributes();
    }

    @Override
    public int vote(final Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        int baseVoterVoteResult = baseVoter.vote(authentication, object, attributes);
        if (baseVoterVoteResult == ACCESS_GRANTED && authenticatedAsUser(authentication)) {
            return tryToAuthorize(authentication);
        } else {
            return baseVoterVoteResult;
        }
    }

    private int tryToAuthorize(Authentication authentication) {
        if (notAuthorized()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                boolean assessAllowed = userService.accessAllowedToComponentType(((UserDetails) authentication.getPrincipal()).getUsername(), ComponentType.ADMIN_PANEL);
                int permissionCheckResult = assessAllowed ? ACCESS_GRANTED : ACCESS_DENIED;
                getRequestAttributes().setAttribute(AUTHORIZED, permissionCheckResult == ACCESS_GRANTED, ServletRequestAttributes.SCOPE_SESSION);
                return permissionCheckResult;
            } else {
                return ACCESS_DENIED;
            }
        } else {
            return ACCESS_GRANTED;
        }
    }

    private boolean notAuthorized() {
        Boolean authorized = (Boolean) getRequestAttributes().getAttribute(AUTHORIZED, SCOPE_SESSION);
        return authorized == null || !authorized;
    }

    private boolean authenticatedAsUser(Authentication authentication) {
        return authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return baseVoter.supports(attribute);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return baseVoter.supports(clazz);
    }
}
