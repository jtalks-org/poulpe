package org.jtalks.poulpe.web.controller.rest.authenticator;


import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Verifier;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class PropertyVerifierTest {
    ComponentService componentService;
    PropertyVerifier propertyVerifier;

    @BeforeMethod
    public void setUp() throws Exception {
        componentService = mock(ComponentService.class);
        propertyVerifier = new PropertyVerifier(componentService);
    }

    @Test(enabled = false)
    public void emptyUsernameAndPasswordMeanNoAuthentication() throws Exception {
        Component component = new Component();
        component.addProperty(PropertyVerifier.USERNAME_PROP, "");
        component.addProperty(PropertyVerifier.PASSWORD_PROP, "");

        when(componentService.getByType(ComponentType.ADMIN_PANEL)).thenReturn(component);

        assertEquals(propertyVerifier.verify(new Request(), new Response(null)), Verifier.RESULT_VALID);
    }

    @Test
    public void emptyUsernameMeansNoAuthentication() throws Exception {
        Component component = new Component();
        component.addProperty(PropertyVerifier.USERNAME_PROP, "");
        component.addProperty(PropertyVerifier.PASSWORD_PROP, "134");

        when(componentService.getByType(ComponentType.ADMIN_PANEL)).thenReturn(component);

        assertEquals(propertyVerifier.verify(new Request(), new Response(null)), Verifier.RESULT_VALID);
    }

    @Test(enabled = false)
    public void emptyPasswordMeansNoAuthentication() throws Exception {
        Component component = new Component();
        component.addProperty(PropertyVerifier.USERNAME_PROP, "1234");
        component.addProperty(PropertyVerifier.PASSWORD_PROP,"");

        when(componentService.getByType(ComponentType.ADMIN_PANEL)).thenReturn(component);

        assertEquals(propertyVerifier.verify(new Request(),new Response(null)), Verifier.RESULT_VALID);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSetLocalSecrets() throws Exception {
        propertyVerifier.setLocalSecrets(null);
    }
}
