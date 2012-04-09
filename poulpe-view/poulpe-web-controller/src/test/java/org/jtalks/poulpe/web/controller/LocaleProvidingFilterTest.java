package org.jtalks.poulpe.web.controller;

import static org.jtalks.poulpe.web.controller.AdminWindow.EN_LOCALE_LANG;
import static org.jtalks.poulpe.web.controller.AdminWindow.RU_LOCALE_LANG;
import static org.jtalks.poulpe.web.controller.LocaleProvidingFilter.USER_LOCALE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;

public class LocaleProvidingFilterTest {

    LocaleProvidingFilter interceptor;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain chain;
    @Mock
    HttpSession session;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interceptor = new LocaleProvidingFilter();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testRequestWithNonexistentLocale() throws IOException, ServletException {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("notLocale", "2c");
        when(request.getCookies()).thenReturn(cookies);

        interceptor.doFilter(request, response, chain);

        verify(session, never()).setAttribute(anyString(), any());
        verifyZeroInteractions(response);
    }

    @Test
    public void testRequestRu() throws IOException, ServletException {
        Cookie[] cookies = new Cookie[2];
        cookies[0] = new Cookie(USER_LOCALE, RU_LOCALE_LANG);
        cookies[1] = new Cookie("notLocale", "2c");
        when(request.getCookies()).thenReturn(cookies);

        interceptor.doFilter(request, response, chain);

        verify(session).setAttribute(Attributes.PREFERRED_LOCALE, Locales.getLocale(RU_LOCALE_LANG));
        verifyZeroInteractions(response);
    }

    @Test
    public void testRequestEn() throws IOException, ServletException {
        Cookie[] cookies = new Cookie[2];
        cookies[0] = new Cookie(USER_LOCALE, EN_LOCALE_LANG);
        cookies[1] = new Cookie("notLocale", "2c");
        when(request.getCookies()).thenReturn(cookies);

        interceptor.doFilter(request, response, chain);

        verify(session).setAttribute(Attributes.PREFERRED_LOCALE, Locales.getLocale(EN_LOCALE_LANG));
        verifyZeroInteractions(response);
    }

}
