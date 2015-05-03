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


import org.jtalks.poulpe.service.exceptions.ValidationException;
import org.jtalks.poulpe.web.controller.rest.pojo.Error;
import org.jtalks.poulpe.web.controller.rest.pojo.Errors;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonServerResource extends ServerResource {

    /**
     * Creates {@code Errors} object if thrown the {@code ValidationException}
     *
     * @param ex the {@code ValidationException}
     * @return the object {@code Errors}
     */
    protected Errors ifValidationException(ValidationException ex) {
        Errors result = new Errors();
        result.setErrorList(createErrorList(ex.getTemplateMessages()));
        return result;
    }

    /**
     * Creates a list errors from code messages.
     * Removes '{' and '}' from code messages
     *
     * @param strings code messages
     * @return a list errors
     */
    private List<Error> createErrorList(List<String> strings) {
        List<Error> result = new ArrayList<Error>();
        for (String s : strings) {
            Error err = new Error();
            err.setCode(s.replaceAll("[{}]", ""));
            result.add(err);
        }
        return result;
    }

    /**
     * Creates {@code Errors} object if thrown the {@code Exception}
     *
     * @param ex the {@code Exception}
     * @return the object {@code Errors}
     */
    protected Errors ifOtherException(Exception ex) {
        Errors result = new Errors();
        List<Error> errList = new ArrayList<Error>();
        Error err = new Error();
        err.setMessage(ex.getMessage());
        errList.add(err);
        result.setErrorList(errList);
        return result;
    }

    /**
     * Creates restlet response reprehension based on provided Errors
     * @param errors errors used to create representation
     * @return created representation
     */
    protected Representation getErrorRepresentation(Errors errors) {
        JaxbRepresentation<Errors> response = new JaxbRepresentation<Errors>(errors);
        response.setFormattedOutput(true);
        return response;
    }
}
