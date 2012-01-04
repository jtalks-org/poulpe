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
package org.jtalks.common.security.web;


import org.jtalks.common.model.entity.User;
import org.jtalks.common.security.user.LastLoginTimeServiceMixin;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that activated when user successfully authenticated.
 *
 * @author Kirill Afonin
 */
public class SuccessfulAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private LastLoginTimeServiceMixin lastLoginTimeService;

    /**
     * Constructor.
     *
     * @param lastLoginTimeService service for users related actions
     */
    public SuccessfulAuthenticationHandler(LastLoginTimeServiceMixin lastLoginTimeService) {
        this.lastLoginTimeService = lastLoginTimeService;
    }

    /**
     * Handle user's successfull authentication.
     * Updates last login time for authenticated user.
     *
     * @param request        http request
     * @param response       http response
     * @param authentication user's authentication
     * @throws javax.servlet.ServletException .
     * @throws java.io.IOException            .
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        lastLoginTimeService.updateLastLoginTime(user);
        logger.info("User logged in: " + user.getUsername());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
