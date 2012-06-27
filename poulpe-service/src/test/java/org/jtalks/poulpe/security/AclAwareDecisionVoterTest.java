package org.jtalks.poulpe.security;

import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.GroupAce;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.mockito.Matchers;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.jtalks.poulpe.security.AclAwareDecisionVoter.AUTHORIZED;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;
import static org.testng.Assert.assertEquals;

/**
 * @author dionis
 *         6/27/12 1:32 AM
 */
public class AclAwareDecisionVoterTest {
    private static final List<ConfigAttribute> ATTRIBUTES = Collections.emptyList();
    private AclAwareDecisionVoter voter;
    private AclManager aclManager;
    private TransactionTemplate transactionTemplateStub;
    private AccessDecisionVoter baseVoter;
    private ComponentDao componentDao;
    private UserDao userDao;
    private RequestAttributes requestAttributes;
    private Authentication authentication;


    @BeforeMethod
    public void setUp() {
        voter = new AclAwareDecisionVoter();
        aclManager = mock(AclManager.class);
        voter.setAclManager(aclManager);
        transactionTemplateStub = new TransactionTemplate(){
            @Override
            public <T> T execute(TransactionCallback<T> action) throws TransactionException {
                return action.doInTransaction(null);
            }
        };
        voter.setTransactionTemplate(transactionTemplateStub);
        baseVoter = mock(AccessDecisionVoter.class);
        voter.setBaseVoter(baseVoter);
        componentDao = mock(ComponentDao.class);
        voter.setComponentDao(componentDao);
        userDao = mock(UserDao.class);
        voter.setUserDao(userDao);
        requestAttributes = mock(RequestAttributes.class);
        voter.setRequestAttributes(requestAttributes);

        authentication = mock(Authentication.class);
    }

    @Test
    public void notAuthorized() {
        authenticatedSuccessfully();
        when(authentication.getPrincipal()).thenReturn("anonymous");

        assertEquals(voter.vote(authentication, new Object(), ATTRIBUTES), ACCESS_GRANTED);
    }

    @Test
    public void notPoulpeUserShouldFail(){
        authenticatedSuccessfully();
        when(authentication.getPrincipal()).thenReturn(new User("user", "user@mail.com", "pass", "salt"));

        assertEquals(voter.vote(authentication, new Object(), ATTRIBUTES), ACCESS_DENIED);
    }

    @Test
    public void userHasNoTargetGroup() {
        authenticatedSuccessfully();
        userHaveNotBeenAuthorizedYet();
        unsuccessfulAuthorizationFlow();

        assertEquals(voter.vote(authentication, new Object(), ATTRIBUTES), ACCESS_DENIED);

        verify(requestAttributes).setAttribute(eq(AUTHORIZED), eq(false), eq(SCOPE_SESSION));
    }

    private void unsuccessfulAuthorizationFlow() {
        PoulpeUser poulpeUser = mock(PoulpeUser.class);
        GroupAce groupAce = mock(GroupAce.class);
        Component component = mock(Component.class);
        String username = "username";

        when(authentication.getPrincipal()).thenReturn(poulpeUser);
        when(poulpeUser.getUsername()).thenReturn(username);
        when(userDao.getByUsername(eq(username))).thenReturn(poulpeUser);
        when(poulpeUser.getGroups()).thenReturn(Collections.<Group>emptyList());
        when(componentDao.getByType(eq(ComponentType.ADMIN_PANEL))).thenReturn(component);
        when(aclManager.getGroupPermissionsOn(eq(component))).thenReturn(singletonList(groupAce));
        when(groupAce.isGranting()).thenReturn(true);
    }

    private void successfulAuthorizationFlow() {
        PoulpeUser poulpeUser = mock(PoulpeUser.class);
        GroupAce groupAce = mock(GroupAce.class);
        Component component = mock(Component.class);
        Group group = mock(Group.class);
        String username = "username";
        long id = 42L;
        when(group.getId()).thenReturn(id);
        when(groupAce.getGroupId()).thenReturn(id);

        when(authentication.getPrincipal()).thenReturn(poulpeUser);
        when(poulpeUser.getUsername()).thenReturn(username);
        when(userDao.getByUsername(eq(username))).thenReturn(poulpeUser);
        when(poulpeUser.getGroups()).thenReturn(singletonList(group));
        when(componentDao.getByType(eq(ComponentType.ADMIN_PANEL))).thenReturn(component);
        when(aclManager.getGroupPermissionsOn(eq(component))).thenReturn(singletonList(groupAce));
        when(groupAce.isGranting()).thenReturn(true);
    }

    private void userHaveNotBeenAuthorizedYet() {
        when(requestAttributes.getAttribute(eq(AUTHORIZED), eq(SCOPE_SESSION))).thenReturn(null);
    }

    @Test
    public void negativeResultOfAuthorizationAfterNegativeAuthorizationResultRememberedInSession() {
        authenticatedSuccessfully();
        previousAttemptToAuthorizeFailed();
        unsuccessfulAuthorizationFlow();

        assertEquals(voter.vote(authentication, new Object(), ATTRIBUTES), ACCESS_DENIED);

        verify(requestAttributes).setAttribute(eq(AUTHORIZED), eq(false), eq(SCOPE_SESSION));
    }

    @Test
    public void positiveResultOfAuthorizationAfterNegativeAuthorizationResultRememberedInSession() {
        authenticatedSuccessfully();
        previousAttemptToAuthorizeFailed();
        successfulAuthorizationFlow();

        assertEquals(voter.vote(authentication, new Object(), ATTRIBUTES), ACCESS_GRANTED);

        verify(requestAttributes).setAttribute(eq(AUTHORIZED), eq(true), eq(SCOPE_SESSION));
    }

    private void previousAttemptToAuthorizeFailed() {
        when(requestAttributes.getAttribute(eq(AUTHORIZED), eq(SCOPE_SESSION))).thenReturn(false);
    }

    @Test
    public void successfulAuthorizationShouldBeRememberedInSession() {
        authenticatedSuccessfully();
        userHaveNotBeenAuthorizedYet();
        successfulAuthorizationFlow();

        assertEquals(voter.vote(authentication, new Object(), ATTRIBUTES), ACCESS_GRANTED);

        verify(requestAttributes).setAttribute(eq(AUTHORIZED), eq(true), eq(SCOPE_SESSION));
    }

    @Test
    public void successfulAuthorizationResultShouldBeRememberedInSession() {
        PoulpeUser poulpeUser = mock(PoulpeUser.class);
        authenticatedSuccessfully();
        when(authentication.getPrincipal()).thenReturn(poulpeUser);
        userWasAuthorizedSuccessfully();

        assertEquals(voter.vote(authentication, new Object(), ATTRIBUTES), ACCESS_GRANTED);
    }

    @Test
    public void supportsAttributeShouldBeDelegatedToBaseVoter(){
        ConfigAttribute configAttribute = mock(ConfigAttribute.class);
        voter.supports(configAttribute);

        verify(baseVoter).supports(eq(configAttribute));
    }

    @Test
    public void supportsClassShouldBeDelegatedToBaseVoter(){
        Class<?> clazz = Object.class;
        voter.supports(clazz);

        verify(baseVoter).supports(eq(clazz));
    }

    private void userWasAuthorizedSuccessfully() {
        when(requestAttributes.getAttribute(eq(AUTHORIZED), eq(SCOPE_SESSION))).thenReturn(true);
    }

    private void authenticatedSuccessfully() {
        when(baseVoter.vote(eq(authentication), anyObject(), Matchers.<Collection<ConfigAttribute>>any())).thenReturn(ACCESS_GRANTED);
        when(authentication.isAuthenticated()).thenReturn(true);
    }
}
