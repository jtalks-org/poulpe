package org.jtalks.common.security.acl;

import org.jtalks.common.model.entity.Entity;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author stanislav bashkirtsev
 */
public class AclManagerTest {
    private List<Permission> permissions = new ArrayList<Permission>();
    private AclManager manager;
    private MutableAclService aclService;

    @BeforeMethod
    public void setUp() throws Exception {
        aclService = mock(MutableAclService.class);
        manager = new AclManager(aclService);
    }

    @Test
    public void testGetBranchPermissions() throws Exception {

    }

    @Test(dataProvider = "randomSidsAndPermissionsAndEntity", dataProviderClass = AclDataProvider.class)
    public void testGrant(List<Sid> sids, List<Permission> permissions, Entity target) throws Exception {

    }

    @Test(dataProvider = "randomEntity", dataProviderClass = AclDataProvider.class)
    public void testDeleteFromAcl(Entity target) throws Exception {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(target.getClass(), target.getId());
        manager.deleteFromAcl(target.getClass(), target.getId());
        verify(aclService).deleteAcl(objectIdentity, true);
    }

    @Test(expectedExceptions = IllegalStateException.class,
            dataProvider = "randomEntity", dataProviderClass = AclDataProvider.class)
    public void testDeleteFromAclWithZeroId(Entity target) throws Exception {
        manager.deleteFromAcl(target.getClass(), 0);
    }
}
