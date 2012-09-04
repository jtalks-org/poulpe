package org.jtalks.poulpe.service;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author stanislav bashkirtsev
 */
public class JcommuneHttpNotifierTest {
    private final String URL ="url";
    private JcommuneHttpNotifier sut;

    @BeforeMethod
    public void beforeMethod(){
        sut = spy(new JcommuneHttpNotifier());
    }

    @Test(expectedExceptions = JcommuneRespondedWithErrorException.class)
    public void notifyAboutReindexComponentIfResponseIsNotOfValidCode() throws Exception {
        doReturn(illegalResponse()).when(sut).sendHttpRequest(anyString(), eq(HttpPost.METHOD_NAME));
        sut.notifyAboutReindexComponent(URL);
    }

    @Test(expectedExceptions = NoConnectionToJcommuneException.class)
    public void notifyAboutReindexComponentIfNoConnection() throws Exception {
        doThrow(new IOException()).when(sut).sendHttpRequest(anyString(), eq(HttpPost.METHOD_NAME));
        sut.notifyAboutReindexComponent(URL);
    }

    @Test(expectedExceptions = JcommuneRespondedWithErrorException.class)
    public void testNotifyAboutDeleteElementIfResponseIsNotOfValidCode() throws Exception {
        doReturn(illegalResponse()).when(sut).sendHttpRequest(anyString(), eq(HttpDelete.METHOD_NAME));
        sut.notifyAboutDeleteElement(URL);
    }

    @Test(expectedExceptions = NoConnectionToJcommuneException.class)
    public void testNotifyAboutDeleteElementIfNoConnection() throws Exception {
        doThrow(new IOException()).when(sut).sendHttpRequest(anyString(),  eq(HttpDelete.METHOD_NAME));
        sut.notifyAboutDeleteElement(URL);
    }

    @Test
    public void testNotifyAboutComponentDelete() throws Exception {
        doNothing().when(sut).notifyAboutDeleteElement(anyString());
        sut.notifyAboutComponentDelete(anyString());
    }

    @Test
    public void testNotifyAboutBranchDelete() throws Exception {
        doNothing().when(sut).notifyAboutDeleteElement(anyString());
        sut.notifyAboutBranchDelete(URL, new PoulpeBranch());
    }

    @Test
    public void testNotifyAboutSectionDelete() throws Exception {
        doNothing().when(sut).notifyAboutDeleteElement(anyString());
        sut.notifyAboutSectionDelete(URL, new PoulpeSection());
    }

    @Test(expectedExceptions = JcommuneUrlNotConfiguredException.class)
    public void testCheckUrlIsConfiguredException() throws JcommuneUrlNotConfiguredException{
        sut.checkUrlIsConfigured("");
    }



    private HttpResponse illegalResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        doReturn(statusLine).when(httpResponse).getStatusLine();
        doReturn(100).when(statusLine).getStatusCode();
        return httpResponse;
    }
}
