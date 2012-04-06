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

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.RequestInterceptor;

/**
 * Request Interceptor for setting locale for each user based on user's preferences.
 * 
 * @author dim42
 */
public class LocaleProvidingRequestInterceptor implements RequestInterceptor {

    public static final String USER_LOCALE = "userLocale";

    /**
     * Restore locale from the previous user request and sets it for the current session, i.e. overrides default
     * language for all users.
     */
    @Override
    public void request(Session sess, Object request, Object response) {
        final Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (cookies != null) {
            for (int j = cookies.length; --j >= 0;) {
                if (cookies[j].getName().equals(USER_LOCALE)) {
                    String localeString = cookies[j].getValue();
                    Locale locale = Locales.getLocale(localeString);
                    sess.setAttribute(Attributes.PREFERRED_LOCALE, locale);
                    return;
                }
            }
        }

    }

}
