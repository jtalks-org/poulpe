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
import static org.jtalks.poulpe.web.controller.LocaleProvidingFilter.USER_LOCALE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminWindowTest {

    @InjectMocks
    AdminWindow adminWindow = new AdminWindow();
    @Mock
    ZkHelper zkHelper;
    @Mock
    HttpServletResponse response;
    @Mock
    Cookie cookie;
    @Mock
    WindowManager windowManager;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnChangeLocaleToRu() throws IOException {
        when(zkHelper.getResponse()).thenReturn(response);
        when(zkHelper.createCookie(USER_LOCALE, RU_LOCALE_LANG)).thenReturn(cookie);

        adminWindow.onChangeLocaleToRu();

        verify(response).addCookie(cookie);
        verify(zkHelper).reloadPage();
    }

    @Test
    public void testOnChangeLocaleToEn() throws IOException {
        when(zkHelper.getResponse()).thenReturn(response);
        when(zkHelper.createCookie(USER_LOCALE, EN_LOCALE_LANG)).thenReturn(cookie);

        adminWindow.onChangeLocaleToEn();

        verify(response).addCookie(cookie);
        verify(zkHelper).reloadPage();
    }

    @Test
    public void testOnShowComponents() {
        String target = "components.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowBranches() {
        String target = "brancheditor.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowTopicTypes() {
        String target = "topictype.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowForumStructure() {
        String target = "WEB-INF/pages/forum/structure/ForumStructure.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowUserBanning() {
        String target = "userbanning.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowUserGroups() {
        String target = "usergroup.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowUsers() {
        String target = "users.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowRanks() {
        String target = "ranks.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowGroupsPermissions() {
        String target = "groups/GroupsPermissions.zul";
        windowManager.open(target);
        verify(windowManager).open(target);
    }
}