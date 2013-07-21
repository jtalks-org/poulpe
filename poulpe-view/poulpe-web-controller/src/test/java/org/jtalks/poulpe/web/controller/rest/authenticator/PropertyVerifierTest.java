package org.jtalks.poulpe.web.controller.rest.authenticator;


import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.service.ComponentService;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Verifier;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class PropertyVerifierTest {

    ComponentService componentService = mock(ComponentService.class);

    PropertyVerifier propertyVerifier = new PropertyVerifier(componentService);

    @Test
    public void testVerifyIfEmpty() throws Exception {

        Component component = new Component();
        component.addProperty(PropertyVerifier.USERNAME_PROP,"");
        component.addProperty(PropertyVerifier.PASSWORD_PROP,"");

        when(componentService.getByType(ComponentType.ADMIN_PANEL)).thenReturn(component);

        assertEquals(propertyVerifier.verify(new Request(),new Response(null)), Verifier.RESULT_VALID);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSetLocalSecrets() throws Exception {
        propertyVerifier.setLocalSecrets(null);
    }
}
