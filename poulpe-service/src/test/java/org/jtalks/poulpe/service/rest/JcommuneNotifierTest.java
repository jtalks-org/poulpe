/**
 * Copyright (C) 2012  JTalks.org Team
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
package org.jtalks.poulpe.service.rest;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.delete;
import static com.xebialabs.restito.semantics.Condition.post;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.net.ServerSocket;

import org.glassfish.grizzly.http.util.HttpStatus;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.jtalks.pouple.service.rest.JcommuneNotifier;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.xebialabs.restito.server.StubServer;

/**
 * Tests that JcommuneNotifier response on commands correctly. It's actually a bit more than unit
 * test because it uses real REST server and real HTTP connections.
 * 
 * @author Evgeny Kapinos
 * @see <a href="https://github.com/mkotsur/restito/blob/master/README.md">Restito</a> 
 */
public class JcommuneNotifierTest {
    
    private final String JCOMMUNE_URL = "http://localhost";
    
    private final String WHOLEFORUM_URL_PART = "/component";
    private final String REINDEX_URL_PART = "/search/index/rebuild";
    private final String SECTIONS_URL_PART = "/sections/";
    private final String BRANCH_URL_PART = "/branches/";
    
    private final long BRANCH_ID = 1L;
    private final long SECTION_ID = 1L;
    
    private StubServer server;
    private PoulpeBranch branchMock;
    private PoulpeSection sectionMock;
    private JcommuneNotifier notifier;
   
    /**
     * Start JCommune server mock (Restito framework).
     */
    @BeforeClass
    private void BeforeTest() {      
        server = new StubServer().run();                   

        branchMock = mock(PoulpeBranch.class);         
        doReturn(BRANCH_ID).when(branchMock).getId();
         
        sectionMock = mock(PoulpeSection.class);
        doReturn(SECTION_ID).when(sectionMock).getId();
        
        notifier = new JcommuneNotifier();
    }
        
    /**
     * Shutdown server mock.
     */
    @AfterClass
    public void AfterTest() {
        server.stop(); // It works without exceptions even if server already stopped
    }
    
    /**
     * Delete exist branch.
     */
    @Test
    public void testSuccessBranchDelete() throws Exception {                      
        whenHttp(server).match(delete(BRANCH_URL_PART+BRANCH_ID)).then(status(HttpStatus.OK_200));               
        notifier.notifyAboutBranchDelete(JCOMMUNE_URL+":"+String.valueOf(server.getPort()), branchMock);
    }    

    /**
     * Delete nonexistent branch.
     */
    @Test(expectedExceptions = JcommuneRespondedWithErrorException.class)
    public void testUnsuccessfulBranchDelete() throws Exception {                     
        whenHttp(server).match(delete(BRANCH_URL_PART+BRANCH_ID)).then(status(HttpStatus.NOT_FOUND_404));               
        notifier.notifyAboutBranchDelete(JCOMMUNE_URL+":"+String.valueOf(server.getPort()), branchMock);    
    }    

    /**
     * Delete exist section.
     */
    @Test
    public void testSuccessSectionDelete() throws Exception {                      
        whenHttp(server).match(delete(SECTIONS_URL_PART+SECTION_ID)).then(status(HttpStatus.OK_200));               
        notifier.notifyAboutSectionDelete(JCOMMUNE_URL+":"+String.valueOf(server.getPort()), sectionMock);
    }    

    /**
     * Delete nonexistent section.
     */
    @Test(expectedExceptions = JcommuneRespondedWithErrorException.class)
    public void testUnsuccessfulSectionDelete() throws Exception {                     
        whenHttp(server).match(delete(SECTIONS_URL_PART+SECTION_ID)).then(status(HttpStatus.NOT_FOUND_404));               
        notifier.notifyAboutSectionDelete(JCOMMUNE_URL+":"+String.valueOf(server.getPort()), sectionMock);    
    }
    
    /**
     * Delete component.
     */
    @Test
    public void testComponentDelete() throws Exception {                     
        whenHttp(server).match(delete(WHOLEFORUM_URL_PART)).then(status(HttpStatus.OK_200));               
        notifier.notifyAboutComponentDelete(JCOMMUNE_URL+":"+String.valueOf(server.getPort()));    
    }
    
    /**
     * Reindex component.
     */
    @Test
    public void testComponentReindex() throws Exception {                     
        whenHttp(server).match(post(REINDEX_URL_PART)).then(status(HttpStatus.OK_200));               
        notifier.notifyAboutReindexComponent(JCOMMUNE_URL+":"+String.valueOf(server.getPort()));    
    }    

    /**
     * JCommune doesn't response.
     */
    @Test(expectedExceptions = NoConnectionToJcommuneException.class)
    public void testConnectionError() throws Exception {        
        // Find free port
        ServerSocket socket = new ServerSocket(0);
        socket.close();          
        // Try connect to nonexistent server
        notifier.notifyAboutBranchDelete(JCOMMUNE_URL+":"+String.valueOf(socket.getLocalPort()), branchMock);
   }
}
