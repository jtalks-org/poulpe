package org.jtalks.poulpe.web.controller.rest;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;

/**
 * Registration REST resource interface
 *
 * @author Mikhail Zaitsev
 */
public interface RegistrationResource {

    /**
     * Registers
     *
     * @param represent request representation
     * @return response representation
     */
    @Post
    Representation register(Representation represent);
}
