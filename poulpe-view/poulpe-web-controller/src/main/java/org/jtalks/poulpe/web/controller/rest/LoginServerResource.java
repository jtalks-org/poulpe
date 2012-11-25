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

import javax.annotation.Nonnull;

import org.jtalks.poulpe.service.UserService;
import org.restlet.resource.ServerResource;

/**
 * RESTful server resource for the {@code LoginResource} interface.
 * 
 * @author Guram Savinov
 */
public class LoginServerResource extends ServerResource implements
        LoginResource {

    private UserService userService;

    /**
     * Creates the server login resource with specified {@code UserService}.
     * 
     * @param userService
     */
    public LoginServerResource(@Nonnull UserService userService) {
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String authenticate(String credintals) {
        String[] splitted = credintals.split(":");
        return "username: " + splitted[0] + ", passwordHash: " + splitted[1] + ", userService: " + userService;
    }

}
