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

import org.springframework.security.access.AccessDeniedException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * @author Evgeny Surovtsev
 */
public class AuthenticationCleaningAccessDeniedExceptionHandlerTest {
    private static final String CONTEXT_PATH = "http://localhost";
    private AuthenticationCleaningAccessDeniedExceptionHandler accessDeniedHandler;

    @BeforeMethod
    public void setUp() throws Exception {
        accessDeniedHandler = new AuthenticationCleaningAccessDeniedExceptionHandler();
    }

    @Test(dataProvider = "requestAndResponse")
    public void commonUrlRedirectsToLoginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String currentUrl = "/fake_url";
        String redirectedUrl = "/login.zul?access_denied=1";

        doReturn(currentUrl).when(request).getServletPath();
        accessDeniedHandler.setDefaultErrorPage("/login.zul?access_denied=1");
        accessDeniedHandler.handle(request, response, new AccessDeniedException("403"));
        verify(response).sendRedirect(CONTEXT_PATH + redirectedUrl);
    }

    @Test(dataProvider = "requestAndResponse")
    public void specialUrlRedirectsToAlternativeRoute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String currentUrl = "/login.zul";
        String redirectedUrl = "/";

        doReturn(currentUrl).when(request).getServletPath();
        accessDeniedHandler.setAlternativeRoutes(Collections.singletonMap("/login.zul", "/"));
        accessDeniedHandler.handle(request, response, new AccessDeniedException("403"));
        verify(response).sendRedirect(CONTEXT_PATH + redirectedUrl);
    }

    @DataProvider
    public Object[][] requestAndResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doReturn(CONTEXT_PATH).when(request).getContextPath();
        return new Object[][]{{request, mock(HttpServletResponse.class)}};
    }
}
