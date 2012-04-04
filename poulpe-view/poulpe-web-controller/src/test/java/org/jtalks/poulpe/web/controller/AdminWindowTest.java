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