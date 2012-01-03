package org.jtalks.common.security.acl;

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
        when(wrappedMock.getEntries()).thenReturn(entries.subList(0,2));

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

    @DataProvider(name = "mutableAclMock")
    public Object[][] provideMutableAclMock() {
        return new Object[][]{{mock(MutableAcl.class)}};
    }

    @DataProvider(name = "extendedMutableAclAndWrappedMock")
    public Object[][] provideExtendedMutableAclAndWrappedMock() {
        MutableAcl wrappedMock = mock(MutableAcl.class);
        return new Object[][]{{ExtendedMutableAcl.create(wrappedMock), wrappedMock}};
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
