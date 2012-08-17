package org.jtalks.poulpe.service;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author stanislav bashkirtsev
 */
public class JcommuneHttpNotifierTest {
    private final JcommuneHttpNotifier sut = spy(new JcommuneHttpNotifier());

    @Test(expectedExceptions = JcommuneRespondedWithErrorException.class)
    public void testNotifyAboutReindexComponent() throws Exception {
        doReturn(illegalResponse()).when(sut).sendHttpRequest(anyString());
        sut.setjCommuneUrl("url");

        sut.notifyAboutReindexComponent();
    }

    private HttpResponse illegalResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        doReturn(statusLine).when(httpResponse).getStatusLine();
        doReturn(100).when(statusLine).getStatusCode();
        return httpResponse;
    }
}
