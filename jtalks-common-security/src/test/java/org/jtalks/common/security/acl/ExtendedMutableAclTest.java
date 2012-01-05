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

import com.google.common.collect.Lists;
import org.jtalks.poulpe.model.permissions.BranchPermission;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

/**
 * @author stanislav bashkirtsev
 */
public class ExtendedMutableAclTest {
    @Test(dataProvider = "extendedMutableAclAndWrappedMock")
    public void testDelete(ExtendedMutableAcl extendedMutableAcl, MutableAcl wrappedMock) throws Exception {
        List<AccessControlEntry> entries = createEntries(wrappedMock);
        when(wrappedMock.getEntries()).thenReturn(entries);

        assertEquals(2, extendedMutableAcl.delete(entries.get(2)));
        verify(wrappedMock).deleteAce(2);
    }

    @Test(dataProvider = "extendedMutableAclAndWrappedMock")
    public void testDeleteList(ExtendedMutableAcl extendedMutableAcl, MutableAcl wrappedMock) throws Exception {
        List<AccessControlEntry> entries = createEntries(wrappedMock);
        when(wrappedMock.getEntries()).thenReturn(entries);

        extendedMutableAcl.delete(entries.subList(1, 3));
        verify(wrappedMock).deleteAce(1);
        verify(wrappedMock).deleteAce(2);
    }

    @Test(dataProvider = "extendedMutableAclAndWrappedMock",
            description = "Checks what happens when there are no such elements in the Acl#getEntries()")
    public void testDeleteList_withNoSuchEntries(ExtendedMutableAcl extendedMutableAcl, MutableAcl wrappedMock) {
        List<AccessControlEntry> entries = createEntries(wrappedMock);
        when(wrappedMock.getEntries()).thenReturn(entries.subList(0, 2));

        extendedMutableAcl.delete(entries.subList(1, 3));
        verify(wrappedMock).deleteAce(1);
    }

    @Test(dataProvider = "extendedMutableAclAndWrappedMock")
    public void testDeleteList_withEmptyList(ExtendedMutableAcl extendedMutableAcl, MutableAcl wrappedMock) {
        List<AccessControlEntry> entries = new ArrayList<AccessControlEntry>();
        when(wrappedMock.getEntries()).thenReturn(entries);

        extendedMutableAcl.delete(entries);
        verify(wrappedMock, times(0)).deleteAce(anyInt());
    }

    @Test(dataProvider = "mutableAclMock")
    public void testCreate(MutableAcl acl) throws Exception {
        ExtendedMutableAcl extendedMutableAcl = ExtendedMutableAcl.create(acl);
        assertSame(acl, extendedMutableAcl.getAcl());
    }

    @Test(dataProvider = "mutableAclMock")
    public void testCastAndCreate(Acl acl) throws Exception {
        ExtendedMutableAcl extendedMutableAcl = ExtendedMutableAcl.castAndCreate(acl);
        assertSame(acl, extendedMutableAcl.getAcl());
    }

    @Test(dataProvider = "mutableAclMock", expectedExceptions = ClassCastException.class)
    public void testCastAndCreate_withWrongImplementation(Acl acl) throws Exception {
        ExtendedMutableAcl.castAndCreate(mock(Acl.class));
    }

    @Test(dataProvider = "extendedMutableAclAndWrappedMock")
    public void testAddPermissions(ExtendedMutableAcl extendedMutableAcl, MutableAcl wrappedMock) throws Exception {
        Sid sid = getRandomSids().get(0);
        List<Permission> permissions = createPermissions();
        extendedMutableAcl.addPermissions(sid, permissions, true);
        for (int i = 0; i < permissions.size(); i++) {
            verify(wrappedMock).insertAce(i, permissions.get(i), sid, true);
        }
    }

    @Test(dataProvider = "extendedMutableAclAndWrappedMock")
    public void testAddPermissionsForSids(ExtendedMutableAcl extendedMutableAcl, MutableAcl wrappedMock) {
        List<Sid> sids = getRandomSids();
        List<Permission> permissions = createPermissions();
        extendedMutableAcl.addPermissions(sids, permissions, true);
        for (int i = 0; i < permissions.size(); i++) {
            for (Sid sid : sids) {
                verify(wrappedMock).insertAce(i, permissions.get(i), sid, true);
            }
        }
    }

    @DataProvider(name = "mutableAclMock")
    public Object[][] provideMutableAclMock() {
        return new Object[][]{{mock(MutableAcl.class)}};
    }

    @DataProvider(name = "extendedMutableAclAndWrappedMock")
    public Object[][] provideExtendedMutableAclAndWrappedMock() {
        MutableAcl wrappedMock = mock(MutableAcl.class);
        return new Object[][]{{ExtendedMutableAcl.create(wrappedMock), wrappedMock}};
    }

    @SuppressWarnings("unchecked")
    private List<Sid> getRandomSids() {
        return (List<Sid>) AclDataProvider.provideRandomSids()[0][0];
    }

    private List<Permission> createPermissions() {
        return Lists.<Permission>newArrayList(BranchPermission.DELETE_POSTS, BranchPermission.VIEW_TOPICS);
    }

    private List<AccessControlEntry> createEntries(MutableAcl acl) {
        List<AccessControlEntry> toReturn = new ArrayList<AccessControlEntry>();
        for (int i = 0; i < 5; i++) {
            toReturn.add(createEntry(i, acl, new PrincipalSid("1"), BranchPermission.CREATE_TOPICS));
        }
        return toReturn;
    }

    private AccessControlEntry createEntry(long id, MutableAcl acl, Sid sid, Permission permission) {
        return new AccessControlEntryImpl(id, acl, sid, permission, true, true, true);
    }
}
