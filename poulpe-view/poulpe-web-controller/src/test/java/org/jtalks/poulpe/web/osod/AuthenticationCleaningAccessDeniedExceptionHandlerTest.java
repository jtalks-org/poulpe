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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Evgeny Surovtsev
 */
public class AuthenticationCleaningAccessDeniedExceptionHandlerTest {
    AuthenticationCleaningAccessDeniedExceptionHandler accessDeniedHandler;
    AccessDeniedException accessDeniedException;
    HttpServletRequest request;
    HttpServletResponse response;
    static final String contextPath = "http://localhost";
    static final String defaultErorPage = "/login.zul?access_denied=1";
    
	static final Map<String, String> alternativeRoutesMap = new HashMap<String, String>();
    static {
    	alternativeRoutesMap.put("/login.zul", "/");
    }
    
    
    @BeforeMethod
    public void setUp() throws Exception {
    	accessDeniedHandler = new AuthenticationCleaningAccessDeniedExceptionHandler();
    	accessDeniedHandler.setDefaultErrorPage(defaultErorPage);
    	accessDeniedHandler.setAlternativeRoutes(alternativeRoutesMap);
    	
    	accessDeniedException = new AccessDeniedException("403");
    	request = mock(HttpServletRequest.class);
    	response = mock(HttpServletResponse.class);;
    	
    	doReturn(contextPath).when(request).getContextPath();
    }

    @Test(dataProvider = "provideRedirectData")
    public void redirectTest(String errorPage, String expectedRedirect) throws IOException, ServletException {
    	doReturn(errorPage).when(request).getServletPath();
    	accessDeniedHandler.handle(request, response, accessDeniedException);
    	verify(response).sendRedirect(contextPath + expectedRedirect);
    }

    @DataProvider
    public Object[][] provideRedirectData() {
        return new Object[][] {
        		{"/fake_url", "/login.zul?access_denied=1"},
        		{"/login.zul", "/"}
        };
    }
}
