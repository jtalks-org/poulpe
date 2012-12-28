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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.restlet.data.Method;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
/**
 * Functional tests for JCommuneNotifierImpl class
 * 
 * @author Evgeny Kapinos
 */
public class JCommuneNotifierImplTest {
        
    // We don't use interface, because this test 
    // intended for this implementation only
    private JCommuneNotifierImpl sut;  
   
    @BeforeClass
    private void beforeTestCase() throws Exception { 
        sut = spy(new JCommuneNotifierImpl(null));
        doNothing().when(sut).notifyJCommune(any(String.class), any(Method.class));
    }
           
    @Test
    public void checkCorrectURL() throws Exception { 
        sut.notifyAboutReindexComponent("http://localhost/jcommune");
    }
    
    @Test(expectedExceptions = JcommuneUrlNotConfiguredException.class)
    public void checkIncorrectURL() throws Exception {                      
        sut.notifyAboutReindexComponent("");
    }
    
}
