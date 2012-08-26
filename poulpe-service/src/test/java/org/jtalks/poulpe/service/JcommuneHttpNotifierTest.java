package org.jtalks.poulpe.service;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author stanislav bashkirtsev
 */
public class JcommuneHttpNotifierTest {
    private final JcommuneHttpNotifier sut = spy(new JcommuneHttpNotifier());

    @Test(expectedExceptions = JcommuneRespondedWithErrorException.class)
    public void shouldThrowIfResponseIsNotOfValidCode() throws Exception {
        doReturn(illegalResponse()).when(sut).sendHttpRequest(anyString());

        sut.notifyAboutReindexComponent("url");
    }

    @Test(expectedExceptions = NoConnectionToJcommuneException.class, enabled = false)
    public void testNotifyAboutComponentDelete() throws Exception {
        sut.notifyAboutComponentDelete("url");
    }

    @Test(expectedExceptions = NoConnectionToJcommuneException.class, enabled = false)
    public void testNotifyAboutBranchDelete() throws Exception {
        sut.notifyAboutBranchDelete("url", new PoulpeBranch());
    }

    @Test(expectedExceptions = NoConnectionToJcommuneException.class, enabled = false)
    public void testNotifyAboutSectionDelete() throws Exception {
        sut.notifyAboutSectionDelete("url", new PoulpeSection());
    }

    private HttpResponse illegalResponse() {
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        doReturn(statusLine).when(httpResponse).getStatusLine();
        doReturn(100).when(statusLine).getStatusCode();
        return httpResponse;
    }
}
