package org.jtalks.poulpe.web.controller.section;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureDataTest {
    private ForumStructureData sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new ForumStructureData();
    }

    @Test
    public void testGetSelectedItemFirstTime() throws Exception {
        ForumStructureItem item = new ForumStructureItem();
        ForumStructureItem previous = sut.setSelectedItem(item);
        assertNull(previous.getItem());
    }

    @Test
    public void testGetSelectedItemReturnsPreviousValue() throws Exception {
        ForumStructureItem item = new ForumStructureItem();
        sut.setSelectedItem(item);
        assertSame(item, sut.setSelectedItem(new ForumStructureItem()));
    }

    @Test
    public void testRemoveSelectedItem() throws Exception {

    }
}
