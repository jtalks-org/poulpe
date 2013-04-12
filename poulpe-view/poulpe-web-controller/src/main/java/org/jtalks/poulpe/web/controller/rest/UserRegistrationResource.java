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
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

/**
 * Implementation registration resource for users
 *
 * @author Mikhail Zaitsev
 */
public class UserRegistrationResource extends ServerResource implements RegistrationResource{

    private UserService userService;

    public UserRegistrationResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers user
     *
     * @param represent request representation
     * @return response representation
     */
    @Override
    public Representation register(Representation represent) {
        Registration registration = new Registration();
        try {
            JaxbRepresentation<User> userRep = new JaxbRepresentation<User>(represent, User.class);
            User user = userRep.getObject();
            userService.registration(user.getUsername(),user.getPasswordHash(),user.getFirstName(),user.getLastName(),user.getEmail());
        }catch (ValidationException e) {
            registration = ifValidationException(e);
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
        }catch (Exception e) {
            registration =ifOtherException(e);
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
        }
        JaxbRepresentation resultRep = new JaxbRepresentation<Registration>(registration);
        resultRep.setFormattedOutput(true);
        return resultRep;
    }

    /**
     * Creates {@code Registration} object if thrown the {@code ValidationException}
     *
     * @param ex the {@code ValidationException
     * @return the object {@code Registration}
     */
    private Registration ifValidationException(ValidationException ex){
        Registration result = new Registration();
        result.setValidErrorMessage(ex.getMessages());
        return result;
    }

    /**
     * Creates {@code Registration} object if thrown the {@code Exception}
     *
     * @param ex the {@code Exception}
     * @return the object {@code Registration}
     */
    private Registration ifOtherException(Exception ex){
        Registration result = new Registration();
        result.setErrorMessage(ex.getMessage());
        return result;
    }
}
