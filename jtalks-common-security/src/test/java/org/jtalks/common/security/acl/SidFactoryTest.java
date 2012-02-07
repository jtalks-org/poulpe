package org.jtalks.common.security.acl;

import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author stanislav bashkirtsev
 */
public class SidFactoryTest {
    @Test
    public void testCreate_customSid() throws Exception {
        UserGroupSid sid = (UserGroupSid) SidFactory.create("usergroup:2", true);
        assertEquals(sid.getGroupId(), "2");
        assertEquals(sid.getSidId(), "usergroup:2");
    }

    @Test(expectedExceptions = SidFactory.SidWithoutRequiredConstructorException.class)
    public void testCreate_sidWithoutRequiredConstructor() throws Exception {
        SidFactory.addMapping("without_required_constructor", SidWithoutRequiredConstructor.class);
        SidFactory.create("without_required_constructor:2", false);
    }

    @Test(expectedExceptions = SidFactory.SidWithoutRequiredConstructorException.class)
    public void testCreate_sidWithPrivateConstructor() throws Exception {
        SidFactory.addMapping("private_constructor", SidWithPrivateConstructor.class);
        SidFactory.create("private_constructor:2", false);
    }

    @Test(expectedExceptions = SidFactory.SidClassIsNotConcreteException.class)
    public void testCreate_sidWithAbstractClass() throws Exception {
        SidFactory.addMapping("abstract_class", AbstractSid.class);
        SidFactory.create("abstract_class:2", false);
    }

    @Test(expectedExceptions = SidFactory.SidConstructorThrewException.class)
    public void testCreate_sidWithConstructorThatThrowsException() throws Exception {
        SidFactory.addMapping("constructor_throws", SidWithConstructorThatThrowsException.class);
        SidFactory.create("constructor_throws:2", false);
    }

    @Test
    public void testCreate_principalSid() throws Exception {
        PrincipalSid sid = (PrincipalSid) SidFactory.create("uncle toby", true);
        assertEquals(sid.getPrincipal(), "uncle toby");
    }

    @Test
    public void testCreate_grantedAuthoritySid() throws Exception {
        GrantedAuthoritySid sid = (GrantedAuthoritySid) SidFactory.create("ROLE_ADMIN", false);
        assertEquals(sid.getGrantedAuthority(), "ROLE_ADMIN");
    }

    private static class SidWithoutRequiredConstructor implements IdentifiableSid {
        @Override
        public String getSidId() {
            return null;
        }
    }

    private static class SidWithPrivateConstructor implements IdentifiableSid {
        private SidWithPrivateConstructor(String sidId) {

        }

        @Override
        public String getSidId() {
            return null;
        }
    }

    private static class SidWithConstructorThatThrowsException implements IdentifiableSid {
        public SidWithConstructorThatThrowsException(String sidId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSidId() {
            return null;
        }
    }

    private static abstract class AbstractSid implements IdentifiableSid {
        public AbstractSid(String sidId) {
        }

        @Override
        public String getSidId() {
            return null;
        }
    }

}
