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
import org.jtalks.poulpe.web.controller.rest.pojo.Authentication;
import org.jtalks.poulpe.web.controller.rest.pojo.Credentials;
import org.jtalks.poulpe.web.controller.rest.pojo.Profile;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import javax.annotation.Nonnull;

/**
 * RESTful server resource for the {@code LoginResource} interface
 *
 * @author Guram Savinov
 */
public class LoginServerResource extends ServerResource implements LoginResource {
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAIL = "fail";
    private static final String STATUS_FAIL_INFO = "Incorrect username or password";
    private static final String USERNAME_PARAM = "username";
    private static final String HASH_PASS_PARAM = "passwordHash";

    private UserService userService;

    /**
     * Creates the server login resource with specified {@code UserService}.
     *
     * @param userService the user service
     */
    public LoginServerResource(@Nonnull UserService userService) {
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Representation authenticate() {
        Credentials cred = getCredentialsFromQuery();
        Authentication result = getAuthentication(cred);

        JaxbRepresentation<Authentication> resultRep = new JaxbRepresentation<Authentication>(result);
        resultRep.setFormattedOutput(true);
        return resultRep;
    }

    /**
     * Returns {@code Authentication} by {@code Credentials}
     *
     * @param credentials {@code Credentials}
     * @return {@code Authentication}, if user not found - set HTTP status 404 (NOT FOUND)
     */
    private Authentication getAuthentication(Credentials credentials) {
        try {
            PoulpeUser user = userService.authenticate(credentials.getUsername(), credentials.getPasswordHash());
            Authentication result = new Authentication(user.getUsername());
            result.setStatus(STATUS_SUCCESS);
            result.setProfile(new Profile(user));
            return result;
        } catch (NotFoundException e) {
            getResponse().setStatus(new Status(HttpStatus.SC_NOT_FOUND));
            Authentication result = new Authentication(credentials.getUsername());
            result.setStatus(STATUS_FAIL);
            result.setStatusInfo(STATUS_FAIL_INFO);
            return result;
        }
    }

    /**
     * @return {@code Credentials} from query parameters: username & passwordHash
     */
    private Credentials getCredentialsFromQuery() {
        Credentials result = new Credentials();
        result.setUsername(getQuery().getValues(USERNAME_PARAM));
        result.setPasswordHash(getQuery().getValues(HASH_PASS_PARAM));
        return result;
    }
}
