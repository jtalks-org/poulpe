package org.jtalks.poulpe.web.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.mockejb.jndi.MockContextFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/** 
 * @author Evgeny Kapinos
 */
public class LoggerInitializationListenerTest {
   
    // We should use expected external resources for testing, so constants was copied 
    private static final String LOG_FILE_USER_PROPERTY = "LOG_FILE";
    private static final String LOG_FILE_SYSTEM_PROPERTY = "LOG_FILE_POULPE";
    private static final String LOG_FILE_VALUE_DEFAULT = "../logs/poulpe.log";

    private Context tomcatContext;
    private InputStream emptyDatasourcePropertesFile;
    private LoggerInitializationListener sut;
    
    @BeforeClass
    public void setUpClass() throws Exception {
        MockContextFactory.setAsInitial();
        tomcatContext = new MockContextFactory().getInitialContext(null);
        new InitialContext().bind("java:/comp/env", tomcatContext);
        emptyDatasourcePropertesFile = new ByteArrayInputStream(new byte[0]);  
    }
    
    @BeforeMethod
    public void setUp() throws Exception {
        sut = spy(new LoggerInitializationListener());           
        emptyDatasourcePropertesFile.reset();
        doReturn(emptyDatasourcePropertesFile).when(sut).getPropertiesFileStream();        
        tomcatContext.unbind(LOG_FILE_USER_PROPERTY);        
        System.clearProperty(LOG_FILE_SYSTEM_PROPERTY); // filled after all tests 
    }

    @Test
    public void shouldLookForPropertyInJndi() throws Exception {
        tomcatContext.bind(LOG_FILE_USER_PROPERTY, "someFileName1.log");
        sut.contextInitialized(null);
        assertEquals(System.getProperty(LOG_FILE_SYSTEM_PROPERTY), "someFileName1.log");        
    }

    @Test
    public void shouldLookForPropertyInDataSourceClass() throws Exception {
        byte[] is = (LOG_FILE_USER_PROPERTY+"=someFileName2.log").getBytes("UTF-8");
        InputStream datasourcePropertesFile = new ByteArrayInputStream(is);
        doReturn(datasourcePropertesFile).when(sut).getPropertiesFileStream();
        sut.contextInitialized(null);
        assertEquals(System.getProperty(LOG_FILE_SYSTEM_PROPERTY), "someFileName2.log");        
    }

    @Test
    public void shouldLookForPropertyInSystemProperties() throws Exception {
        System.setProperty(LOG_FILE_USER_PROPERTY, "someFileName3.log");
        sut.contextInitialized(null);
        assertEquals(System.getProperty(LOG_FILE_SYSTEM_PROPERTY), "someFileName3.log");
        System.clearProperty(LOG_FILE_USER_PROPERTY);
    }
    
    @Test
    public void shouldSetDefaultPropertyValue() throws Exception {
        sut.contextInitialized(null);
        assertEquals(System.getProperty(LOG_FILE_SYSTEM_PROPERTY), LOG_FILE_VALUE_DEFAULT);        
    }
    
    @Test
    public void shouldClearSystemPropertyAfterApplicationUndeploy() throws Exception {
        System.setProperty(LOG_FILE_SYSTEM_PROPERTY, "someFileName.log");
        sut.contextDestroyed(null);
        assertEquals(System.getProperty(LOG_FILE_SYSTEM_PROPERTY), null);
    }
}