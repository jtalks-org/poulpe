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

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import java.io.IOException;

/**
 * Login RESTful service resource interface
 *
 * @author Guram Savinov
 */
public interface LoginResource {

    /**
     * Authenticates user by username and password hash specified in request arguments
     *
     * @return operation status and {@code Profile} wrapped to {@code Authentication}
     *         representation If user not found, returns meets both HTTP status
     * @throws IOException when user password not match
     */
    @Get
    Representation authenticate() throws IOException;

}
