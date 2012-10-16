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

import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.jtalks.poulpe.web.controller.AdminWindow.EN_LOCALE_LANG;
import static org.jtalks.poulpe.web.controller.AdminWindow.RU_LOCALE_LANG;
import static org.jtalks.poulpe.web.controller.LocaleProvidingFilter.USER_LOCALE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminWindowTest {

    @InjectMocks
    AdminWindow adminWindow = new AdminWindow(null);

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
    public void testOnChangeLocaleToRu() {
        when(zkHelper.getResponse()).thenReturn(response);
        when(zkHelper.createCookie(USER_LOCALE, RU_LOCALE_LANG)).thenReturn(cookie);

        adminWindow.onChangeLocaleToRu();

        verify(response).addCookie(cookie);
        verify(zkHelper).reloadPage();
    }

    @Test
    public void testOnChangeLocaleToEn() {
        when(zkHelper.getResponse()).thenReturn(response);
        when(zkHelper.createCookie(USER_LOCALE, EN_LOCALE_LANG)).thenReturn(cookie);

        adminWindow.onChangeLocaleToEn();

        verify(response).addCookie(cookie);
        verify(zkHelper).reloadPage();
    }

    public void testOnShow(String target) {
        verify(windowManager).open(target);
    }

    @Test
    public void testOnShowComponents() {
        adminWindow.onShowComponents();
        testOnShow("/WEB-INF/pages/component/components.zul");
    }

    @Test
    public void testOnShowBranches() {
        adminWindow.onShowBranches();
        testOnShow("brancheditor.zul");
    }

    @Test
    public void testOnShowTopicTypes() {
        adminWindow.onShowTopicTypes();
        testOnShow("topictype.zul");
    }

    @Test
    public void testOnShowForumStructure() {
        adminWindow.onShowForumStructure();
        testOnShow("WEB-INF/pages/forum/structure/ForumStructure.zul");
    }

    @Test
    public void testOnPersonalPermissions() {
        adminWindow.onShowPersonalPermissions();
        testOnShow("WEB-INF/pages/users/PersonalPermissions.zul");
    }

    @Test
    public void testOnShowUserBanning() {
        adminWindow.onShowUserBanning();
        testOnShow("WEB-INF/pages/users/UserBanning.zul");
    }

    @Test
    public void testOnShowUserGroups() {
        adminWindow.onShowUserGroups();
        testOnShow("usergroup.zul");
    }

    @Test
    public void testOnShowUsers() {
        adminWindow.onShowUsers();
        testOnShow("users.zul");
    }

    @Test
    public void testOnShowRanks() {
        adminWindow.onShowRanks();
        testOnShow("ranks.zul");
    }

    @Test
    public void testOnShowGroupsPermissions() {
        adminWindow.onShowGroupsPermissions();
        testOnShow("groups/GroupsPermissions.zul");
    }

    @Test
    public void testOnComponentClick() {
        adminWindow.onComponentClick(TestFixtures.component(ComponentType.FORUM));
        verify(windowManager).open("WEB-INF/pages/forum/structure/ForumStructure.zul");
    }
}
