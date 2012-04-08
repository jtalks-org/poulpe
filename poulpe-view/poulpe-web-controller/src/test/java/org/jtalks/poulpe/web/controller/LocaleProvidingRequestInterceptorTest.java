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
package org.jtalks.poulpe.web.controller;

import static org.jtalks.poulpe.web.controller.AdminWindow.EN_LOCALE_LANG;
import static org.jtalks.poulpe.web.controller.AdminWindow.RU_LOCALE_LANG;
import static org.jtalks.poulpe.web.controller.LocaleProvidingRequestInterceptor.USER_LOCALE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;

public class LocaleProvidingRequestInterceptorTest {

    LocaleProvidingRequestInterceptor interceptor;
    @Mock
    Session sess;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interceptor = new LocaleProvidingRequestInterceptor();
    }

    @Test
    public void testRequest() {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("notLocale", "2c");
        when(request.getCookies()).thenReturn(cookies);

        interceptor.request(sess, request, response);

        verify(sess, never()).setAttribute(anyString(), any());
        verifyZeroInteractions(response);
    }

    @Test
    public void testRequestRu() {
        Cookie[] cookies = new Cookie[2];
        cookies[0] = new Cookie(USER_LOCALE, RU_LOCALE_LANG);
        cookies[1] = new Cookie("notLocale", "2c");
        when(request.getCookies()).thenReturn(cookies);

        interceptor.request(sess, request, response);

        verify(sess).setAttribute(Attributes.PREFERRED_LOCALE, Locales.getLocale(RU_LOCALE_LANG));
        verifyZeroInteractions(response);
    }

    @Test
    public void testRequestEn() {
        Cookie[] cookies = new Cookie[2];
        cookies[0] = new Cookie(USER_LOCALE, EN_LOCALE_LANG);
        cookies[1] = new Cookie("notLocale", "2c");
        when(request.getCookies()).thenReturn(cookies);

        interceptor.request(sess, request, response);

        verify(sess).setAttribute(Attributes.PREFERRED_LOCALE, Locales.getLocale(EN_LOCALE_LANG));
        verifyZeroInteractions(response);
    }

}
