package org.jtalks.poulpe.security;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.mockito.Matchers;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.jtalks.poulpe.security.AclAwareDecisionVoter.AUTHORIZED;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
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
    private AccessDecisionVoter baseVoter;
    private UserService userService;
    private RequestAttributes requestAttributes;
    private Authentication authentication;
    private PoulpeUser poulpeUser;


    @BeforeMethod
    public void setUp() {
        voter = new AclAwareDecisionVoter();
        baseVoter = mock(AccessDecisionVoter.class);
        voter.setBaseVoter(baseVoter);
        userService = mock(UserService.class);
        voter.setUserService(userService);
        requestAttributes = mock(RequestAttributes.class);
        voter.setRequestAttributes(requestAttributes);
        poulpeUser = mock(PoulpeUser.class);

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
    public void firstUnsuccessfulAttempt() {
        authenticatedSuccessfully();
        userHaveNotBeenAuthorizedYet();
        unsuccessfulAuthorizationFlow();

        assertEquals(voter.vote(authentication, new Object(), ATTRIBUTES), ACCESS_DENIED);

        verify(requestAttributes).setAttribute(eq(AUTHORIZED), eq(false), eq(SCOPE_SESSION));
    }

    private void unsuccessfulAuthorizationFlow() {
        when(authentication.getPrincipal()).thenReturn(poulpeUser);
        when(userService.accessAllowedToComponentType(eq(poulpeUser), eq(ComponentType.ADMIN_PANEL))).thenReturn(false);
    }

    private void successfulAuthorizationFlow() {
        when(authentication.getPrincipal()).thenReturn(poulpeUser);
        when(userService.accessAllowedToComponentType(eq(poulpeUser), eq(ComponentType.ADMIN_PANEL))).thenReturn(true);
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
