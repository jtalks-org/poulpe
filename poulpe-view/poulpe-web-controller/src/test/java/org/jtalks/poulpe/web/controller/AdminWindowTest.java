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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zk.ui.Execution;

public class AdminWindowTest {

    AdminWindow adminWindow;
    @Mock
    ZkHelper zkHelper;
    @Mock
    HttpServletResponse response;
    @Mock
    Execution execution;
    @Mock
    Cookie cookie;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        adminWindow = new AdminWindow();
        adminWindow.setZkHelper(zkHelper);
    }

    @Test
    public void testOnChangeLocaleToRu() throws IOException {
        when(zkHelper.getResponse()).thenReturn(response);

        adminWindow.onChangeLocaleToRu();

        verify(response).addCookie(any(Cookie.class));
        verify(zkHelper).reloadPage();
    }

}