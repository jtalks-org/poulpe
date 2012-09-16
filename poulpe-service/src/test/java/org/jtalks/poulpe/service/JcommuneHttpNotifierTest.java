package org.jtalks.poulpe.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hsqldb.lib.StringInputStream;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.jtalks.poulpe.service.JcommuneHttpNotifierTest.HttpRequestMatcher.eqRequest;
import static org.jtalks.poulpe.test.fixtures.TestFixtures.user;
import static org.mockito.Mockito.*;

/** @author stanislav bashkirtsev */
public class JcommuneHttpNotifierTest {
    private JcommuneHttpNotifier sut;

    @BeforeMethod
    public void beforeMethod() {
        UserDao userDao = mock(UserDao.class);
        //user dao always returns same admin user with same password
        doReturn(user("admin", "password", "mail@mail.ru")).when(userDao).getByUsername("admin");
        sut = spy(new JcommuneHttpNotifier(userDao));
    }

    @Test
    public void successfulNotifyAboutReindexComponent() throws Exception {
        doReturn(validResponse()).when(sut).doSendRequest(any(HttpUriRequest.class));
        sut.notifyAboutReindexComponent("url");
        verify(sut).doSendRequest(argThat(eqRequest("url/search/index/rebuild?password=password", "POST")));
    }

    @Test(expectedExceptions = JcommuneRespondedWithErrorException.class)
    public void notifyAboutReindexComponentIfResponseIsNotOfValidCode() throws Exception {
        doReturn(illegalResponse()).when(sut).doSendRequest(any(HttpUriRequest.class));
        sut.notifyAboutReindexComponent("url");
    }

    @Test(expectedExceptions = NoConnectionToJcommuneException.class)
    public void notifyAboutReindexComponentIfNoConnection() throws Exception {
        doThrow(new IOException()).when(sut).doSendRequest(any(HttpUriRequest.class));
        sut.notifyAboutReindexComponent("url");
    }

    @Test(expectedExceptions = JcommuneRespondedWithErrorException.class)
    public void testNotifyAboutDeleteElementIfResponseIsNotOfValidCode() throws Exception {
        doReturn(illegalResponse()).when(sut).doSendRequest(any(HttpUriRequest.class));
        sut.notifyAboutDeleteElement("url");
    }

    @Test(expectedExceptions = NoConnectionToJcommuneException.class)
    public void testNotifyAboutDeleteElementIfNoConnection() throws Exception {
        doThrow(new IOException()).when(sut).doSendRequest(any(HttpUriRequest.class));
        sut.notifyAboutDeleteElement("url");
    }

    @Test
    public void testNotifyAboutComponentDelete() throws Exception {
        doReturn(validResponse()).when(sut).doSendRequest(any(HttpUriRequest.class));
        sut.notifyAboutComponentDelete("url");
        verify(sut).doSendRequest(argThat(eqRequest("url/component?password=password", "DELETE")));
    }

    @Test
    public void testNotifyAboutBranchDelete() throws Exception {
        doReturn(validResponse()).when(sut).doSendRequest(any(HttpUriRequest.class));
        sut.notifyAboutBranchDelete("url", new PoulpeBranch());
        verify(sut).doSendRequest(argThat(eqRequest("url/branches/0?password=password", "DELETE")));
    }

    @Test
    public void testNotifyAboutSectionDelete() throws Exception {
        doReturn(validResponse()).when(sut).doSendRequest(any(HttpUriRequest.class));
        sut.notifyAboutSectionDelete("url", new PoulpeSection());
        verify(sut).doSendRequest(argThat(eqRequest("url/sections/0?password=password", "DELETE")));
    }

    @Test(expectedExceptions = JcommuneUrlNotConfiguredException.class)
    public void emptyUrlShouldMeanNotConfigured() throws JcommuneUrlNotConfiguredException {
        sut.checkUrlIsConfigured("");
    }

    @Test(expectedExceptions = JcommuneUrlNotConfiguredException.class)
    public void nullUrlShouldMeanNotConfigured() throws JcommuneUrlNotConfiguredException {
        sut.checkUrlIsConfigured(null);
    }

    private HttpResponse validResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        doReturn(statusLine).when(httpResponse).getStatusLine();
        doReturn(200).when(statusLine).getStatusCode();
        return httpResponse;
    }

    private HttpResponse illegalResponse() throws IOException {
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        doReturn(statusLine).when(httpResponse).getStatusLine();
        doReturn(100).when(statusLine).getStatusCode();
        HttpEntity httpEntity = mock(HttpEntity.class);
        doReturn(httpEntity).when(httpResponse).getEntity();
        doReturn(new StringInputStream("mock-content")).when(httpEntity).getContent();
        return httpResponse;
    }

    /** A special matcher for our case to check whether the HTTP request are equal or not. */
    static class HttpRequestMatcher extends BaseMatcher<HttpUriRequest> {
        private final String url;
        private final String method;

        private HttpRequestMatcher(String url, String method) {
            this.url = url;
            this.method = method;
        }

        public static HttpRequestMatcher eqRequest(String url, String method) {
            return new HttpRequestMatcher(url, method);
        }

        @Override
        public boolean matches(Object o) {
            HttpUriRequest request = (HttpUriRequest) o;
            return url.equals(request.getURI().toString()) && method.equals(request.getMethod());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(ToStringBuilder.reflectionToString(this));
        }
    }
}
