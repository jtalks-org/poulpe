package org.jtalks.poulpe.web.listener;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.mockejb.jndi.MockContextFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
/** 
 * @author Evgeny Kapinos
 */
public class LoggerInitializationListenerTest {
    private String LOGGING_CONFIG_FILE = "POULPE_LOGGING_CONFIG_FILE";
    private String LOG4J_INIT_OVERRIDE_PROPERTY = "log4j.defaultInitOverride";
    private LoggerInitializationListener sut;
    private ServletContextEvent servletContextEvent;
    private String testFile;
    private URL testFileURI;
    
    @BeforeClass
    public void setUpCommonTestData() throws Exception {
        
        // servletContextEvent is common test stub. It used in under test class as pre-logger
        ServletContext servletContext = mock(ServletContext.class);
        doNothing().when(servletContext).log(anyString());
        doNothing().when(servletContext).log(anyString(), any(Throwable.class));      
        servletContextEvent = mock(ServletContextEvent.class);
        doReturn(servletContext).when(servletContextEvent).getServletContext();
        
        testFile = "/somepath/log4j.xml";
        testFileURI = new File(testFile).toURI().toURL();
    }
    
    @BeforeMethod
    public void setUpCurrentTest() throws Exception {
        sut = spy(new LoggerInitializationListener());        
        //we can't to directly call configureLog4j() method. It will damage logging during test  
        doReturn(true).when(sut).configureLog4j(any(URL.class));      
    }

    @Test
    public void shouldLookForConfigurationInJndi() throws Exception {
        MockContextFactory.setAsInitial();
        Context tomcatContext = new MockContextFactory().getInitialContext(null);
        tomcatContext.bind(LOGGING_CONFIG_FILE, testFile);    
        InitialContext initialContext = new InitialContext();
        initialContext.bind("java:/comp/env", tomcatContext);
        
        sut.contextInitialized(servletContextEvent);

        // expected configuration file applied and it was only one call  
        verify(sut).configureLog4j(eq(testFileURI));
        verify(sut, atMost(1)).configureLog4j(any(URL.class));
        
        MockContextFactory.revertSetAsInitial();
    }

    @Test
    public void shouldLookForConfigurationInDataSourceClass() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(LOGGING_CONFIG_FILE, testFile);
        doReturn(properties).when(sut).loadDatasourcePropertiesFile();
        
        sut.contextInitialized(servletContextEvent);        
        
        // expected configuration file applied and it was only one call  
        verify(sut).configureLog4j(eq(testFileURI));
        verify(sut, atMost(1)).configureLog4j(any(URL.class));     
    }

    @Test
    public void shouldLookForConfigurationInSystemProperties() throws Exception {
        System.setProperty(LOGGING_CONFIG_FILE, testFile);               
        
        sut.contextInitialized(servletContextEvent);        
        
        // expected configuration file applied and it was only one call  
        verify(sut).configureLog4j(eq(testFileURI));           
        verify(sut, atMost(1)).configureLog4j(any(URL.class));       
        
        System.clearProperty(LOGGING_CONFIG_FILE); 
    }
    
    @Test
    public void shouldLoadDefaultConfiguration() throws Exception {              
        sut.contextInitialized(servletContextEvent);
        // embedded configuration file applied and it was only one call  
        verify(sut, atMost(1)).configureLog4j(any(URL.class));
    }
    
    @Test
    public void shouldLeftUnchangedLog4jOverrideProperty() throws Exception {              
        System.setProperty(LOG4J_INIT_OVERRIDE_PROPERTY, "salt");
        
        sut.contextInitialized(servletContextEvent);
        
        assertEquals("salt", System.getProperty(LOG4J_INIT_OVERRIDE_PROPERTY));       
        System.clearProperty(LOG4J_INIT_OVERRIDE_PROPERTY);
    }
}