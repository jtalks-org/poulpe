package org.jtalks.poulpe.web.controller.rest;


import org.apache.http.HttpStatus;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.ValidationException;
import org.jtalks.poulpe.web.controller.rest.pojo.*;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class UserRegistrationResourceTest {

    private UserRegistrationResource userRegistrationResource;

    private UserService userService ;

    public UserRegistrationResourceTest() {

    }

    @BeforeMethod
    public void beforeMethod(){
        userService = mock(UserService.class);
        userRegistrationResource = new UserRegistrationResource(userService);
        userRegistrationResource.setResponse(new Response(new Request()));
    }

    @Test
    public void testRegisterWhen200_OK() throws Exception {

        doNothing().when(userService).registration(any(PoulpeUser.class));

        Representation repres = createUserRepresentation();

        repres = userRegistrationResource.register(repres);

        assertEquals(((StringRepresentation)repres).getText(), " ");
        assertEquals(userRegistrationResource.getResponse().getStatus().getCode(), HttpStatus.SC_OK);
    }


    @Test
    public void testRegisterWhenOtherException() throws Exception {

        userRegistrationResource = new UserRegistrationResource(null);
        userRegistrationResource.setResponse(new Response(new Request()));

        Representation repres = createUserRepresentation();

        userRegistrationResource.register(repres);
        assertEquals(userRegistrationResource.getResponse().getStatus().getCode(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testRegisterWhenImpossibleUnmarshal() throws Exception {

        doNothing().when(userService).registration(any(PoulpeUser.class));

        Representation repres = userRegistrationResource.register(new StringRepresentation(""));
        Errors errors = ((JaxbRepresentation<Errors>)repres).getObject();

        assertNotNull(errors.getErrorList().get(0).getMessage());
        assertEquals(userRegistrationResource.getResponse().getStatus().getCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testRegisterWhenValidationException() throws Exception {

        doThrow(createValidationException()).when(userService).registration(any(PoulpeUser.class));

        Representation repres = createUserRepresentation();


        repres = userRegistrationResource.register(repres);
        Errors errors = ((JaxbRepresentation<Errors>)repres).getObject();

        assertTrue(errors.getErrorList().size()>0);
        for(org.jtalks.poulpe.web.controller.rest.pojo.Error e: errors.getErrorList()){
            assertNotNull(e.getCode());
        }
        assertEquals(userRegistrationResource.getResponse().getStatus().getCode(), HttpStatus.SC_BAD_REQUEST);
    }

    private Representation createUserRepresentation(){
        User user = new User();
        user.setUsername("username");
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPasswordHash("password");

        Representation result = new JaxbRepresentation<User>(user);
        return result;

    }

    private ValidationException createValidationException(){
        List<String> messages = new ArrayList<String>();
        for(int i=1; i<=5; i++){
            messages.add("code"+i);
        }
        ValidationException ex = new ValidationException(messages);
        return  ex;
    }
}
