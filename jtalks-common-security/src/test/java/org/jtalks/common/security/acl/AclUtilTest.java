package org.jtalks.common.security.acl;

import org.jtalks.common.model.entity.Entity;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;

/**
 * @author stanislav bashkirtsev
 */
public class AclUtilTest {
    @Mock
    MutableAclService aclService;
    AclUtil util;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        util = new AclUtil(aclService);
    }


}
