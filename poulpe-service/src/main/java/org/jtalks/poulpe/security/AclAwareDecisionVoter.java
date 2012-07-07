package org.jtalks.poulpe.security;

import com.google.common.annotations.VisibleForTesting;
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
 * A custom SpringSecurity Voter that checks whether user has enough rights to the Admin Panel component: <ul><li>If
 * User is not a part of any group that has granting permissions to the Poulpe component, then she is not
 * authorized.</li><li>If User is in a group that has administration permissions on the Poulpe component, then she is
 * authorized</li><li>If User is in the group that is restricted to have access to Poulpe component, then she is not
 * allowed to log in even if she is in another group that is allowed. Restricted permission is stronger than granting
 * one.</li></ul><br/>Note, that if a Poulpe component does not exist, then no user is authorized to log in, thus people
 * are forced to change database manually to create the component. But when Poulpe is deployed first time, the
 * respective component is created and a user with predefined credentials is also created, which is described <a
 * href="http://jira.jtalks.org/browse/POULPE-335">here</a>.
 *
 * @author dionis
 * @see <a href="http://jira.jtalks.org/browse/POULPE-296">Related JIRA</a>
 */
public class AclAwareDecisionVoter implements AccessDecisionVoter {
    private static final String AUTHORIZED = "authorizedPoulpeUser";
    private final AccessDecisionVoter baseVoter;
    private final UserService userService;

    public AclAwareDecisionVoter(UserService userService) {
        this(new WebExpressionVoter(), userService);
    }

    @VisibleForTesting
    AclAwareDecisionVoter(AccessDecisionVoter baseVoter, UserService userService) {
        this.baseVoter = baseVoter;
        this.userService = userService;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        int baseVoterVoteResult = baseVoter.vote(authentication, object, attributes);
        if (baseVoterVoteResult == ACCESS_GRANTED && authenticatedAsUser(authentication)) {
            return authorizeAndSaveDecisionIntoSession(authentication);
        } else {
            return baseVoterVoteResult;
        }
    }

    private int authorizeAndSaveDecisionIntoSession(Authentication authentication) {
        if (alreadyAuthorized()) {
            return ACCESS_GRANTED;
        }
        boolean allow = userService.accessAllowedToComponentType(usernameOf(authentication), ComponentType.ADMIN_PANEL);
        getRequestAttributes().setAttribute(AUTHORIZED, allow, ServletRequestAttributes.SCOPE_SESSION);
        return allow ? ACCESS_GRANTED : ACCESS_DENIED;
    }

    /**
     * Checks whether the user was already authorized or not yet. This information is kept in the HTTP session.
     *
     * @return true if the user was authorized before and the authorization was successful, otherwise returns false
     */
    private boolean alreadyAuthorized() {
        Boolean authorized = (Boolean) getRequestAttributes().getAttribute(AUTHORIZED, SCOPE_SESSION);
        return authorized != null && authorized;
    }

    private String usernameOf(Authentication authentication) {
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    /**
     * Since SpringSecurity by default thinks of anonymous user as the one that is authenticated, we need to check a bit
     * more. First we check whether she's authenticated by SpringSecurity per se, and then we check whether {@link
     * UserDetails} is created which indicates that it's not an anonymous user.
     *
     * @param authentication to check whether it's an authenticated user and that it's not anonymous
     * @return true if the user is authenticated by SpringSecurity as an existing user, false if it's an anonymous or
     *         not authenticated user
     */
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

    @VisibleForTesting
    RequestAttributes getRequestAttributes() {
        return RequestContextHolder.currentRequestAttributes();
    }
}
