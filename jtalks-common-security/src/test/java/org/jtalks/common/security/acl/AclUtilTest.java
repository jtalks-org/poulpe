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
import org.springframework.security.acls.model.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

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

    @Test(dataProvider = "randomSidsAndPermissions", dataProviderClass = AclDataProvider.class)
    public void testDeletePermissionFromAcl(List<Sid> sids, List<Permission> permissions) throws Exception {
        ExtendedMutableAcl acl = mock(ExtendedMutableAcl.class);
        List<AccessControlEntry> entries = AclDataProvider.createEntries(acl, sids, permissions);
        when(acl.getEntries()).thenReturn(entries);
        util.deletePermissionsFromAcl(acl, sids, permissions);
        verify(acl).delete(entries);
    }

    @Test
    public void testGetAclForObjectIdentity() throws Exception {
        ObjectIdentity identity = new ObjectIdentityImpl(getClass().getName(), 1);
        MutableAcl mockAcl = mock(MutableAcl.class);
        when(aclService.readAclById(identity)).thenReturn(mockAcl);

        ExtendedMutableAcl extendedMutableAcl = util.getAclFor(identity);
        assertSame(extendedMutableAcl.getAcl(), mockAcl);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAclForObjectIdentity_whichIsNotSavedSoFar() throws Exception {
        ObjectIdentity identity = new ObjectIdentityImpl(getClass().getName(), 1);
        MutableAcl mockAcl = mock(MutableAcl.class);
        when(aclService.readAclById(identity)).thenThrow(NotFoundException.class);
        when(aclService.createAcl(identity)).thenReturn(mockAcl);

        ExtendedMutableAcl extendedMutableAcl = util.getAclFor(identity);
        assertSame(extendedMutableAcl.getAcl(), mockAcl);
    }

    @Test(dataProvider = "randomEntity", dataProviderClass = AclDataProvider.class)
    public void testCreateIdentityFor(Entity entity) throws Exception {
        ObjectIdentity identity = util.createIdentityFor(entity);
        assertEquals(identity.getType(), entity.getClass().getName());
        assertEquals(identity.getIdentifier(), entity.getId());
    }

    @Test(dataProvider = "notSavedEntity", dataProviderClass = AclDataProvider.class,
            expectedExceptions = IllegalArgumentException.class)
    public void testCreateIdentityFor_withWrongId(Entity entity) throws Exception {
        util.createIdentityFor(entity);
    }

}
