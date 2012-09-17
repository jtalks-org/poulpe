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
package org.jtalks.poulpe.web;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Changes the standard Spring Security way for processing forbidden urls by sending to a browser redirect response
 * (301) instead of Spring Security's standard Access denied response (403).
 * <p/>
 * Class supports 2 types of redirecting: <ol> <li>A standard one which redirects user to defaultErrorUrl in case there
 * is an common access denied problem.</li> <li>A map of alternative routes - which user will be redirected to in case
 * there is access denied problem an the user comes from a special url.</li> </ol>
 *
 * @author dionis 6/28/12 10:01 PM
 * @author Evgeny Surovtsev
 */
public class AuthenticationCleaningAccessDeniedExceptionHandler extends AccessDeniedHandlerImpl {
    private final Map<String, String> alternativeRoutes = new HashMap<String, String>();
    private String defaultErrorPage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String errorPage;
        if (alternativeRoutes.containsKey(request.getServletPath())) {
            errorPage = alternativeRoutes.get(request.getServletPath());
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            errorPage = defaultErrorPage;
        }
        response.sendRedirect(request.getContextPath() + errorPage);
    }

    /**
     * Sets up a default error page. User will be redirected to the defaultPageError in case there is an access denied
     * problem and current user's URL is not in alternative routes (see. {@link #setAlternativeRoutes(Map)})
     *
     * @param defaultErrorPage a relative to web-application URL, like "/login.zul?access_denied=1".
     */
    public void setDefaultErrorPage(String defaultErrorPage) {
        this.defaultErrorPage = defaultErrorPage;
    }

    /**
     * Defines alternative routes for a user's redirecting in case of access denied problem. Each map entry's key
     * defines current users' URL, each map entry's value defines an alternative URL  which a user will be redirected
     * to.<br> <strong>ex.</strong> "/login.zul" -> "/" - if current user's application-related URL is "/login.zul" she
     * will be redirected to "/".<br> If current user's URL is not found in alternativeRoutes she will be redirected to
     * defaultErrorPage (see. {@link #setDefaultErrorPage(String)})
     *
     * @param alternativeRoutes a map of alternative routes.
     */
    public void setAlternativeRoutes(Map<String, String> alternativeRoutes) {
        this.alternativeRoutes.putAll(alternativeRoutes);
    }
}
