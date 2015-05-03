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
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.ValidationException;
import org.jtalks.poulpe.web.controller.rest.pojo.Error;
import org.jtalks.poulpe.web.controller.rest.pojo.Errors;
import org.jtalks.poulpe.web.controller.rest.pojo.User;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.util.Series;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementation registration resource for users
 *
 * @author Mikhail Zaitsev
 */
public class UserRegistrationResource extends CommonServerResource implements RegistrationResource {

    public static final String HEADERS_KEY = "org.restlet.http.headers";
    public static final String DRY_RUN_PARAM = "dryRun";
    public static final String TRUE = "true";

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
        try {
            JaxbRepresentation<User> userRep = new JaxbRepresentation<User>(represent, User.class);
            PoulpeUser poulpeUser = createPoulpeUser(userRep.getObject());
            if (!getDryRunFromHeader()) {
                userService.registration(poulpeUser);
                return new StringRepresentation(poulpeUser.getUuid());
            } else {
                userService.dryRunRegistration(poulpeUser);
                return new StringRepresentation(" ");
            }
        } catch (ValidationException e) {
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
            return getErrorRepresentation(ifValidationException(e));
        } catch (IOException e) {
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
            return getErrorRepresentation(ifIOException());
        } catch (Exception e) {
            getResponse().setStatus(new Status(HttpStatus.SC_INTERNAL_SERVER_ERROR));
            return getErrorRepresentation(ifOtherException(e));
        }
    }

    /**
     * Creates {@code Errors} object if thrown the {@code IOException}
     *
     * @return the object {@code Errors}
     */
    private Errors ifIOException() {
        Errors result = new Errors();
        List<Error> errList = new ArrayList<Error>();
        Error err = new Error();
        err.setMessage("Impossible to unmarshal request");
        errList.add(err);
        result.setErrorList(errList);
        return result;
    }

    /**
     * Creates {@code PoulpeUser} from the POJO {@code User}
     *
     * @param user the POJO {@code User}
     * @return {@code PoulpeUser}
     */
    private PoulpeUser createPoulpeUser(User user) {
        PoulpeUser result = new PoulpeUser();
        result.setUsername(user.getUsername());
        result.setEmail(user.getEmail());
        result.setPassword(user.getPasswordHash());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        return result;
    }

    /**
     * Gets DRY_RUN_PARAM from HTTP header.
     *
     * @return value of parameter, or false if not exist.
     */
    protected boolean getDryRunFromHeader() {
        Series headers = (Series) getRequest().getAttributes().get(HEADERS_KEY);
        if (headers == null) {
            return false;
        }
        String value = headers.getValues(DRY_RUN_PARAM);
        return value != null && value.equalsIgnoreCase(TRUE);
    }
}
