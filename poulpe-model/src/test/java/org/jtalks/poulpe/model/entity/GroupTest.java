package org.jtalks.poulpe.model.entity;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Comparator;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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

    @Test
    public void testByNameComparator() throws Exception {
        Comparator<Group> comparator = new Group.ByNameComparator();
        assertTrue(comparator.compare(new Group("ba"), new Group("bb")) < 0);
        assertTrue(comparator.compare(new Group("bb"), new Group("ba")) > 0);
    }

    @Test(expectedExceptions = NullPointerException.class, dataProvider = "nullGroupNames")
    public void testByNameComparator_WithNulls(Group first, Group second) throws Exception {
        Comparator<Group> comparator = new Group.ByNameComparator();
        comparator.compare(first, second);
    }

    @DataProvider(name = "nullGroupNames")
    public Group[][] provideGroupsWithNullNames() {
        return new Group[][]{
                {new Group(), new Group()},
                {new Group(""), new Group()},
                {new Group(), new Group("")}};
    }
}
