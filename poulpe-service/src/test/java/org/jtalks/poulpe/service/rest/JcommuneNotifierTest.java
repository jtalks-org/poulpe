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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.jtalks.pouple.service.rest.JcommuneNotifier;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;
import org.restlet.routing.Variable;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests that JcommuneNotifier response on commands. It's actually a bit more than unit
 * test because it uses real REST server and real HTTP connections.
 * 
 * @author Evgeny Kapinos
 * 
 */
public class JcommuneNotifierTest extends ServerResource {
    
    private Component component;    
    private static List<Integer> availableBrancheId; // Only for one thread testing
    
    /**
     * Method run simulated JCommune (HTTP server and fill start branch state) .
     * @throws Exception 
     */
    @BeforeClass
    private void setUpTest() throws Exception {
        
        // Start HTTP server
        component = SetupServer();
        
        // Setup simulated JCommume state  
        availableBrancheId = new ArrayList<Integer>();
        availableBrancheId.add(1);
        availableBrancheId.add(2);
        availableBrancheId.add(20);               

    }
    
    /**
     * Method runs JCommuneNotifier and check results.
     */
    @Test
    public void test()  throws Exception {       
        
        JcommuneNotifier sut = spy(new JcommuneNotifier());

        String jCommuneUrl = "http://localhost:"+String.valueOf(component.getServers().get(0).getActualPort());
        
        PoulpeBranch branch = mock(PoulpeBranch.class);
        
        doReturn(1L).when(branch).getId();        
        sut.notifyAboutBranchDelete(jCommuneUrl, branch);        
        assertFalse(availableBrancheId.contains(Integer.valueOf(1)));
        
        doThrow(JcommuneRespondedWithErrorException.class).when(sut).notifyAboutBranchDelete(jCommuneUrl, branch);
        
        component.stop();
        doThrow(NoConnectionToJcommuneException.class).when(sut).notifyAboutBranchDelete(jCommuneUrl, branch);
    }
        
    /**
     * Tear down test HTTP server (via Restlet framework).
     * @throws Exception 
     */
    @AfterClass
    public void tearDownTest() throws Exception {
        if (component.isStarted()){
            component.stop();
        }
    }
    
    /**
     * Setup and run test HTTP server (via Restlet framework).
     * @throws Exception 
     */
    private Component SetupServer() throws Exception{
        
        // Create a new Restlet component and add a HTTP server connector to it 
        Component component = new Component();  
        
        // Zero port - mean any available port. We'll get it after start
        component.getServers().add(Protocol.HTTP, 0);
        
        // Then attach it to the local host. 
        // We will use URL's like "/branches/asd123" available. Not "/branches/asd123/123" nor "/branches/asd123?q=1"
        // This class will be used for processing HTTP requests. So we use Restlet annotations like @Delete
        TemplateRoute route = component.getDefaultHost().attach("/branches/{branchId}", JcommuneNotifierTest.class, Template.MODE_EQUALS);  
        
        // decrease the URL freedom. Disallow alphabetical. Allow digits only, somthing like "/branches/123"
        Map<String, Variable> routeVariables = route.getTemplate().getVariables();
        routeVariables.put("branchId", new Variable(Variable.TYPE_DIGIT));
        
        // Now, let's start the component! 
        // Note that the HTTP server connector is also automatically started. 
        component.start();  
        
        return component;
    }
    
    @Delete  
    public void DeleteBranch() {
        String branchIdAsString = (String) getRequest().getAttributes().get("branchId");
        Integer branchId;
        try {
            branchId = Integer.valueOf(branchIdAsString);
        } catch (NumberFormatException e){
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST); // 400
            return;
        }
        
        if(!availableBrancheId.remove(branchId)){
            setStatus(Status.CLIENT_ERROR_NOT_FOUND); // 404
            return;
        }
        
        // return 200. Default Restlet behavior
    }
    
    /**
     * Auxiliary method for local debugging.
     * Server stopped by terminating VM process
     * 
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        JcommuneNotifierTest jcommuneNotifierTest = new JcommuneNotifierTest();
        jcommuneNotifierTest.SetupServer();
    }
}
