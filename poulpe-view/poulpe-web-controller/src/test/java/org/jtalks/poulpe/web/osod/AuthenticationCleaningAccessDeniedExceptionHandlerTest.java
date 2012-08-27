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
package org.jtalks.poulpe.web.osod;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Evgeny Surovtsev
 */
public class AuthenticationCleaningAccessDeniedExceptionHandlerTest {
    AuthenticationCleaningAccessDeniedExceptionHandler accessDeniedHandler;
    HttpServletRequest request;
    HttpServletResponse response;
    static final String contextPath = "http://localhost";


    @BeforeMethod
    public void setUp() throws Exception {
        accessDeniedHandler = new AuthenticationCleaningAccessDeniedExceptionHandler();
        accessDeniedHandler.setDefaultErrorPage("/login.zul?access_denied=1");
        accessDeniedHandler.setAlternativeRoutes(Collections.singletonMap("/login.zul", "/"));

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        doReturn(contextPath).when(request).getContextPath();
    }

    @Test
    public void commonUrlRedirectsToLoginForm() throws IOException, ServletException {
    	String currentUrl = "/fake_url";
    	String redirectedUrl = "/login.zul?access_denied=1";
    	
    	doReturn(currentUrl).when(request).getServletPath();
        accessDeniedHandler.handle(request, response, new AccessDeniedException("403"));
        verify(response).sendRedirect(contextPath + redirectedUrl);
    }
    
    @Test
    public void specialUrlRedirectsToAlternativeRoute() throws IOException, ServletException {
    	String currentUrl = "/login.zul";
    	String redirectedUrl = "/";
    	
    	doReturn(currentUrl).when(request).getServletPath();
        accessDeniedHandler.handle(request, response, new AccessDeniedException("403"));
        verify(response).sendRedirect(contextPath + redirectedUrl);
    }
}
