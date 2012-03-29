package org.jtalks.poulpe.web.controller;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.RequestInterceptor;

public class JtalksLocaleProvider implements RequestInterceptor {

    @Override
    public void request(Session sess, Object request, Object response) {
        final Cookie[] cookies = ((HttpServletRequest)request).getCookies();
        if (cookies != null) {
            for (int j = cookies.length; --j >= 0;) {
               if (cookies[j].getName().equals("my.locale")) {
                    String val = cookies[j].getValue();
                    val = "ru";
                    System.out.println("val!:"+val);
                    Locale locale = org.zkoss.util.Locales.getLocale(val);
                    sess.setAttribute(Attributes.PREFERRED_LOCALE, locale);
                    return;
                }
            }
        }
        
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.addCookie(new Cookie("my.locale", "value"));
    }

}
