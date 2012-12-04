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

import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.service.UserService;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * RESTful server resource for the {@code LoginResource} interface.
 * 
 * @author Guram Savinov
 */
public class LoginServerResource extends ServerResource implements LoginResource {
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAIL = "fail";
    private static final String STATUS_FAIL_INFO = "Incorrect username or password";

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
     * @throws IOException 
     */
    @Override
    public Representation authenticate(Representation rep) throws IOException {
        JaxbRepresentation<Authentication> authRep = new JaxbRepresentation<Authentication>(rep, Authentication.class);
        Authentication auth = authRep.getObject();
        Credentials cred = auth.getCredintals();

        Authentication result = new Authentication(cred.getUsername());
        try {
            PoulpeUser user = userService.authenticate(cred.getUsername(), cred.getPasswordHash());
            result.setStatus(STATUS_SUCCESS);
            result.setProfile(new Profile(user.getFirstName(), user.getLastName()));
        } catch (NotFoundException e) {
            result.setStatus(STATUS_FAIL);
            result.setStatusInfo(STATUS_FAIL_INFO);
        }
        
        JaxbRepresentation<Authentication> resultRep = new JaxbRepresentation<Authentication>(result);
        resultRep.setFormattedOutput(true);
        return resultRep;
    }

}
