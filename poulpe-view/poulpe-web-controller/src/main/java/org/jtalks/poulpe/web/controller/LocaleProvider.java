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
 */
public class LocaleProvider implements RequestInterceptor {

    public static final String USER_LOCALE = "userLocale";

    /**
     * Restore locale from the previous user request and sets it for the current session, i.e. overrides default language for all users.
     */
    @Override
    public void request(Session sess, Object request, Object response) {
        final Cookie[] cookies = ((HttpServletRequest)request).getCookies();
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
