package org.jtalks.poulpe.model.utils;

import org.mockejb.jndi.MockContextFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

import static org.testng.Assert.assertEquals;


/** @author stanislav bashkirtsev */
public class JndiAwarePropertyPlaceholderConfigurerTest {
    private JndiAwarePropertyPlaceholderConfigurer sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new JndiAwarePropertyPlaceholderConfigurer();
    }

    @Test
    public void shouldLookForPropertyInJndi() throws Exception {
        givenTomcatContextWithProps("placeholder", "jndi");
        assertEquals(sut.resolvePlaceholder("placeholder", null, 0), "jndi");
    }

    /**
     * Puts the property into System vars and Properties (that are read from file) and checks that if property found in
     * JNDI, that it's not looked up in other places thus making JNDI of highest priority.
     *
     * @throws Exception we don't care in tests
     */
    @Test
    public void shouldNotLookInOtherPlacesIfFoundInJndi() throws Exception {
        //because we need to change System var which a baaad thing, we'll need to make sure that this variable is not
        //used by other tests and thus we name it this way.
        String propName = "JndiAwarePropertyPlaceholderConfigurerTest-TestPlaceholder1";
        Properties properties = new Properties();
        properties.put(propName, "properties");
        System.setProperty(propName, "system");
        givenTomcatContextWithProps(propName, "jndi");

        assertEquals(sut.resolvePlaceholder(propName, properties, 0), "jndi");
    }

    /**
     * Makes sure that changes didn't break usual placeholder configurer and it works as previously.
     *
     * @throws Exception we don't care in tests
     */
    @Test
    public void looksInUsualPropertiesIfNotFoundInJndi() throws Exception {
        String propName = "JndiAwarePropertyPlaceholderConfigurerTest-TestPlaceholder2";
        Properties properties = new Properties();
        properties.put(propName, "properties");

        assertEquals(sut.resolvePlaceholder(propName, properties, 0), "properties");
    }

    private void givenTomcatContextWithProps(String placeholder, String value) throws NamingException {
        MockContextFactory.setAsInitial();
        Context tomcatContext = new MockContextFactory().getInitialContext(null);
        tomcatContext.bind(placeholder, value);
        new InitialContext().bind("java:/comp/env", tomcatContext);
    }

}
