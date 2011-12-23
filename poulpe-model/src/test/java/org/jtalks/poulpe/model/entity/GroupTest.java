package org.jtalks.poulpe.model.entity;

import org.testng.annotations.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author stanislav bashkirtsev
 */
public class GroupTest {
    @Test
    public void testCreateGroupsWithNames() throws Exception {
        List<Group> result = Group.createGroupsWithNames("Activated", "Registered");
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getName(), "Activated");
        assertEquals(result.get(1).getName(), "Registered");
    }

    @Test
    public void testCreateGroupsWithNames_oneName() throws Exception {
        List<Group> result = Group.createGroupsWithNames("Activated");
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getName(), "Activated");
    }
}
