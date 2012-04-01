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
import static org.jtalks.poulpe.web.controller.LocaleProvider.USER_LOCALE;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Center;

/**
 * Server-side representation of view for main 'Admin Window'.
 * 
 * @author Vladimir Bukhoyarov
 * @author Vyacheslav Zhivaev
 * 
 */
public class AdminWindow extends GenericForwardComposer<Component> {

    private static final long serialVersionUID = 7658211471084280646L;
    
    private Center workArea;
    private WindowManager windowManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        windowManager.setWorkArea(workArea);
        // TODO get user language from DB
        /*
        String localeLanguage = ...; //decide the locale (from, say, database) 
        setCookie(localeLanguage);
        */
    }

    /**
     * Sets Russian language.
     * @throws IOException
     */
    public void onChangeLocaleRu() throws IOException {
        setCookieAndRedirect("ru");
    }

    /**
     * Sets English language.
     * @throws IOException
     */
    public void onChangeLocaleEn() throws IOException {
        setCookieAndRedirect("en");
    }

    private void setCookieAndRedirect(String localeLanguage) {
        setCookie(localeLanguage);
        Executions.sendRedirect(null); //reload the page
    }

    private void setCookie(String localeLanguage) {
        HttpServletResponse response = (HttpServletResponse) Executions.getCurrent().getNativeResponse();
        response.addCookie(new Cookie(USER_LOCALE, localeLanguage));
    }

    /**
     * Show the component list view
     */
    public void onShowComponents() {
        windowManager.open("components.zul");
    }

    /**
     * Show the branches list view
     */
    public void onShowBranches() {
        windowManager.open("brancheditor.zul");
    }

    /**
     * Show the topic type list view
     */
    public void onShowTopicTypes() {
        windowManager.open("topictype.zul");
    }

    /**
     * Show the sections list view
     */
    public void onShowSections() {
        windowManager.open("sections.zul");
    }

    /**
     * Shows user banning window
     */
    public void onShowUserBanning() {
        windowManager.open("userbanning.zul");
    }

    /**
     * Shows User Groups window that allows admins to CRUD groups.
     */
    public void onShowUserGroups() {
        windowManager.open("groups.zul");
    }

    /**
     * Show the users list view
     */
    public void onShowUsers() {
        windowManager.open("users.zul");
    }

    /**
     * Show the ranks page.
     */
    public void onShowRanks() {
        windowManager.open("ranks.zul");
    }

    /**
     * @param windowManager the new window manager
     */
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

}