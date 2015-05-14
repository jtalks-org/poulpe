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
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.ValidationException;
import org.restlet.data.Status;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;

public class ActivationServerResource extends CommonServerResource implements ActivationResource {

    private UserService userService;

    public ActivationServerResource(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Representation activate() {
        String uuid = getQueryValue("uuid");
        try {
            userService.activate(uuid);
        } catch (NotFoundException e) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new EmptyRepresentation();
        } catch (ValidationException e) {
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return getErrorRepresentation(ifValidationException(e));
        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return getErrorRepresentation(ifOtherException(e));
        }
        getResponse().setStatus(Status.SUCCESS_OK);
        return new EmptyRepresentation();
    }
}
