/**
 * Copyright (C) 2011  JTalks.org Team
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
package org.jtalks.common.security.acl;

import org.jtalks.common.model.entity.Entity;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author stanislav bashkirtsev
 */
public class AclManagerTest {
    @Mock
    private AclUtil mockAclUtil;
    @Mock
    private MutableAclService aclService;
    private AclManager manager;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        manager = new AclManager(aclService);
        manager.setAclUtil(mockAclUtil);
    }

    @Test
    public void testGetBranchPermissions() throws Exception {

    }

    @Test(dataProvider = "randomSidsAndPermissionsAndEntity", dataProviderClass = AclDataProvider.class)
    public void testGrant(List<Sid> sids, List<Permission> permissions, Entity target) throws Exception {
        when(mockAclUtil.grant(sids, permissions, target)).thenReturn(ExtendedMutableAcl.NULL_ACL);
        manager.grant(sids, permissions, target);
        verify(aclService).updateAcl(ExtendedMutableAcl.NULL_ACL);
    }

    @Test(dataProvider = "randomSidsAndPermissionsAndEntity", dataProviderClass = AclDataProvider.class)
    public void testDelete(List<Sid> sids, List<Permission> permissions, Entity target) throws Exception {
        when(mockAclUtil.delete(sids, permissions, target)).thenReturn(ExtendedMutableAcl.NULL_ACL);
        manager.delete(sids, permissions, target);
        verify(aclService).updateAcl(ExtendedMutableAcl.NULL_ACL);
    }

    @Test(dataProvider = "randomSidsAndPermissionsAndEntity", dataProviderClass = AclDataProvider.class)
    public void testRestrict(List<Sid> sids, List<Permission> permissions, Entity target) throws Exception {
        when(mockAclUtil.restrict(sids, permissions, target)).thenReturn(ExtendedMutableAcl.NULL_ACL);
        manager.restrict(sids, permissions, target);
        verify(aclService).updateAcl(ExtendedMutableAcl.NULL_ACL);
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
