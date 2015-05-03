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
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.service.exceptions.ValidationException;
import org.jtalks.poulpe.web.controller.rest.pojo.*;
import org.jtalks.poulpe.web.controller.rest.pojo.Error;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import java.util.Collections;

public class ActivationServerResource extends CommonServerResource implements ActivationResource {

    private UserService userService;

    public ActivationServerResource(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Representation activate() {
        String uuid = getQueryValue("uuid");
        if (uuid == null || uuid.isEmpty()) {
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
            return createErrorRepresentation("TODO", "TODO");
        }
        try {
            userService.activate(uuid);
        } catch (NotFoundException e) {
            getResponse().setStatus(new Status(HttpStatus.SC_NOT_FOUND));
            return createErrorRepresentation("TODO", "TODO");
        } catch (ValidationException e) {
            getResponse().setStatus(new Status(HttpStatus.SC_BAD_REQUEST));
            return getErrorRepresentation(ifValidationException(e));
        } catch (Exception e) {
            getResponse().setStatus(new Status(HttpStatus.SC_INTERNAL_SERVER_ERROR));
            return getErrorRepresentation(ifOtherException(e));
        }
        return new StringRepresentation(" ");
    }

    private Representation createErrorRepresentation(String code, String message) {
        Errors result = new Errors();
        org.jtalks.poulpe.web.controller.rest.pojo.Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        result.setErrorList(Collections.singletonList(error));
        return getErrorRepresentation(result);
    }
}
