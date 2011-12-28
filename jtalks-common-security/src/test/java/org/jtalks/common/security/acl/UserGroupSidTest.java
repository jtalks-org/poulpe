package org.jtalks.common.security.acl;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author stanislav bashkirtsev
 */
public class UserGroupSidTest {
    @Test
    public void testConstructor_withSidName() throws Exception {
        assertEquals(new UserGroupSid("groupname:1").getGroupId(), "1");
    }

    @Test(expectedExceptions = IdentifiableSid.WrongFormatException.class)
    public void testConstructor_withSidNameWithoutGroupId() throws Exception {
        new UserGroupSid("groupname:").getGroupId();
    }

    @Test(expectedExceptions = IdentifiableSid.WrongFormatException.class)
    public void testConstructor_withSidNameWithTwoSeparators() throws Exception {
        new UserGroupSid("groupname:1:2").getGroupId();
    }
}
