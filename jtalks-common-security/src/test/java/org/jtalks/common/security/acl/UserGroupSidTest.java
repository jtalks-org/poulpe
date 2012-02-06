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

import org.jtalks.common.model.entity.Group;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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

    @Test
    public void testCreateFromGroups() throws Exception {
        Group[] groups = new Group[]{new Group("1"), new Group("2")};
        groups[0].setId(1L);
        groups[1].setId(2L);
        List<UserGroupSid> resultSids = UserGroupSid.create(groups);
        for (int i = 0, resultSidsSize = resultSids.size(); i < resultSidsSize; i++) {
            assertEquals(resultSids.get(i).getGroupId(), groups[i].getId() + "");
        }
    }

    @Test
    public void testCreateFromGroups_withEmpty() throws Exception {
        List<UserGroupSid> userGroupSids = UserGroupSid.create();
        assertTrue(userGroupSids.isEmpty());
    }
}
