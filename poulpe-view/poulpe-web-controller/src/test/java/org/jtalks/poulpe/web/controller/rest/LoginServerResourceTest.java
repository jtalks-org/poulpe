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

package org.jtalks.poulpe.web.controller.rest;

import org.apache.http.HttpStatus;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/** @author Guram Savinov */
public class LoginServerResourceTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";

    private LoginServerResource sut;

    @BeforeMethod
    public void setUp() throws NotFoundException {
        UserService userService = mock(UserService.class);
        PoulpeUser user = new PoulpeUser(USERNAME, "email", PASSWORD, "salt");
        user.setFirstName(FIRSTNAME);
        user.setLastName(LASTNAME);
        when(userService.authenticate(eq(USERNAME), eq(PASSWORD))).thenReturn(user);
        when(userService.authenticate(not((eq(USERNAME))), eq(PASSWORD))).thenThrow(new NotFoundException());
        sut = new LoginServerResource(userService);
        sut.setResponse(new Response(new Request()));
    }

    @Test
    public void authenticate() throws Exception {
        Authentication auth = new Authentication(USERNAME);
        auth.getCredintals().setPasswordHash(PASSWORD);
        Representation rep = new JaxbRepresentation<Authentication>(auth);
        JaxbRepresentation<Authentication> resultRep = new JaxbRepresentation<Authentication>(
                sut.authenticate(rep), Authentication.class);
        Authentication result = resultRep.getObject();
        assertEquals(result.getStatus(), "success");
        assertEquals(result.getProfile().getFirstName(), FIRSTNAME);
        assertEquals(result.getProfile().getLastName(), LASTNAME);
    }

    @Test
    public void authenticate_whenUserNotFound() throws Exception {
        Authentication auth = new Authentication("notMatchUsername");
        auth.getCredintals().setPasswordHash(PASSWORD);
        Representation rep = new JaxbRepresentation<Authentication>(auth);
        JaxbRepresentation<Authentication> resultRep = new JaxbRepresentation<Authentication>(
                sut.authenticate(rep), Authentication.class);
        Authentication result = resultRep.getObject();
        assertEquals(result.getStatus(), "fail");
        assertEquals(result.getStatusInfo(), "Incorrect username or password");
    }

    @Test
    public void testAuthenticateImplementationForGetMethod() throws Exception {
        Request req = new Request();
        req.setResourceRef("");
        sut.setRequest(req);
        sut.setQueryValue("username", USERNAME);
        sut.setQueryValue("passwordHash", PASSWORD);
        JaxbRepresentation<Authentication> resultRep = new JaxbRepresentation<Authentication>(
                sut.authenticate(), Authentication.class);
        Authentication result = resultRep.getObject();
        assertEquals(result.getStatus(), "success");
        assertEquals(result.getProfile().getFirstName(), FIRSTNAME);
        assertEquals(result.getProfile().getLastName(), LASTNAME);
        assertEquals(sut.getResponse().getStatus().getCode(), HttpStatus.SC_OK);

    }

    @Test
    public void testAuthenticateImplementationForGetMethod_whenUserNotFound() throws IOException {
        Request req = new Request();
        req.setResourceRef("");
        sut.setRequest(req);
        sut.setQueryValue("username", "notMatchUsername");
        sut.setQueryValue("passwordHash", PASSWORD);
        JaxbRepresentation<Authentication> resultRep = new JaxbRepresentation<Authentication>(
                sut.authenticate(), Authentication.class);
        Authentication result = resultRep.getObject();
        assertEquals(result.getStatus(), "fail");
        assertEquals(result.getStatusInfo(), "Incorrect username or password");
        assertEquals(sut.getResponse().getStatus().getCode(), HttpStatus.SC_NOT_FOUND);

    }

}
