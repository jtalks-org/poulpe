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
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.ValidationException;
import org.jtalks.poulpe.web.controller.rest.pojo.*;
import org.jtalks.poulpe.web.controller.rest.pojo.Error;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementation registration resource for users
 *
 * @author Mikhail Zaitsev
 */
public class UserRegistrationResource extends ServerResource implements RegistrationResource {

    private UserService userService;

    public UserRegistrationResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers user if the representation of the request is correct.
     * Sets statuses of response as is following:
     * 400 - if is some validation errors or if impossible to unmarshal request
     * 500 - if is some errors when is handling request
     * 200 - if user success registered
     *
     * @param represent request representation {@code User}
     * @return response representation {@code Error}
     */
    @Override
    public Representation register(Representation represent) {
        Errors errors = null;
        try {
            JaxbRepresentation<User> userRep = new JaxbRepresentation<User>(represent, User.class);
            User user = userRep.getObject();
            userService.registration(user.getUsername(), user.getPasswordHash(), user.getFirstName(), user.getLastName(), user.getEmail());
        } catch (ValidationException e) {
            errors = ifValidationException(e);
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
        } catch (IOException e) {
            errors = ifIOException();
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
        } catch (Exception e) {
            errors = ifOtherException(e);
            getResponse().setStatus(new Status(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
        Representation resultRep = null;
        if (errors == null) {
            resultRep = new StringRepresentation(" ");  //because must be an empty response
        } else {
            resultRep = new JaxbRepresentation<Errors>(errors);
            ((JaxbRepresentation) resultRep).setFormattedOutput(true);
        }
        return resultRep;
    }

    /**
     * Creates {@code Error} object if thrown the {@code ValidationException}
     *
     * @param ex the {@code ValidationException}
     * @return the object {@code Error}
     */
    private Errors ifValidationException(ValidationException ex) {
        Errors result = new Errors();
        result.setErrorList(createErrorList(ex.getTemplateMessages()));
        return result;
    }

    /**
     * Creates {@code Error} object if thrown the {@code IOException}
     *
     * @return the object {@code Error}
     */
    private Errors ifIOException() {
        Errors result = new Errors();
        List<Error> errList = new ArrayList<Error>();
        Error err = new Error();
        err.setError(" Impossible to unmarshal request");
        errList.add(err);
        result.setErrorList(errList);
        return result;
    }

    /**
     * Creates {@code Error} object if thrown the {@code Exception}
     *
     * @param ex the {@code Exception}
     * @return the object {@code Error}
     */
    private Errors ifOtherException(Exception ex) {
        Errors result = new Errors();
        List<Error> errList = new ArrayList<Error>();
        Error err = new Error();
        err.setError(ex.getMessage());
        errList.add(err);
        result.setErrorList(errList);
        return result;
    }

    /**
     * Removes '{' and '}' from code messages
     *
     * @param strings code messages
     * @return code messages without '{' and '}'
     */
    private List<Error> createErrorList(List<String> strings) {
        List<Error> result = new ArrayList<Error>();
        Error err = null;
        for (String s : strings) {
            err = new Error();
            err.setCode(s.replaceAll("[{}]", ""));
            result.add(err);
        }
        return result;
    }
}
