package org.jtalks.poulpe.security;

import org.hibernate.Hibernate;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.List;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;

/**
 * @author dionis
 *         6/26/12 10:28 PM
 */
public class AclAwareDecisionVoter implements AccessDecisionVoter {
    static final String AUTHORIZED = "authorizedPoulpeUser";
    private AccessDecisionVoter baseVoter;
    private AclManager aclManager;
    private ComponentDao componentDao;
    private UserDao userDao;
    private TransactionTemplate transactionTemplate;
    private RequestAttributes requestAttributes;

    public RequestAttributes getRequestAttributes() {
        if (requestAttributes == null) {
            requestAttributes = RequestContextHolder.currentRequestAttributes();
        }
        return requestAttributes;
    }

    @Override
    public int vote(final Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        int vote = baseVoter.vote(authentication, object, attributes);
        if (vote == ACCESS_GRANTED && authorizationNeeded(authentication)) {
            return tryToAuthorize(authentication);
        } else {
            return vote;
        }
    }

    private int tryToAuthorize(Authentication authentication) {
        if (notAuthorized()) {
            if (authentication.getPrincipal() instanceof PoulpeUser) {
                PoulpeUser user = loadInitializedUser(authentication);
                int permissionCheckResult = checkPermissions(user);
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

    private boolean authorizationNeeded(Authentication authentication) {
        return authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails;
    }

    private PoulpeUser loadInitializedUser(final Authentication authentication) {
        return transactionTemplate.execute(new TransactionCallback<PoulpeUser>() {
            @Override
            public PoulpeUser doInTransaction(TransactionStatus status) {
                PoulpeUser userWithInitializedGroups = userDao.getByUsername(((PoulpeUser) authentication.getPrincipal()).getUsername());
                Hibernate.initialize(userWithInitializedGroups.getGroups());
                return userWithInitializedGroups;
            }
        });
    }

    private int checkPermissions(PoulpeUser user) {
        List<GroupAce> permissions = aclManager.getGroupPermissionsOn(componentDao.getByType(ComponentType.ADMIN_PANEL));
        for (GroupAce permission : permissions) {
            if (!permission.isGranting()) {
                continue;
            }
            for (Group userGroup : user.getGroups()) {
                if (permission.getGroupId() == userGroup.getId()) {
                    return ACCESS_GRANTED;
                }
            }
        }
        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return baseVoter.supports(attribute);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return baseVoter.supports(clazz);
    }

    public void setBaseVoter(AccessDecisionVoter baseVoter) {
        this.baseVoter = baseVoter;
    }

    public void setAclManager(AclManager aclManager) {
        this.aclManager = aclManager;
    }

    public void setComponentDao(ComponentDao componentDao) {
        this.componentDao = componentDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    void setRequestAttributes(RequestAttributes requestAttributes) {
        this.requestAttributes = requestAttributes;
    }
}
