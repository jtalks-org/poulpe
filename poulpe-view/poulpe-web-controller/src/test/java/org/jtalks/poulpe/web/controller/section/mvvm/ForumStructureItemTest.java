package org.jtalks.poulpe.web.controller.section.mvvm;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureItemTest {
    private ForumStructureItem item;

    @BeforeMethod
    public void setUp() throws Exception {
        item = new ForumStructureItem();
    }

    @Test
    public void testIsBranch() throws Exception {
        item.setItem(new PoulpeBranch());
        assertTrue(item.isBranch());
    }

    @Test
    public void testIsBranch_whenNull() throws Exception {
        assertFalse(item.isBranch());
    }

    @Test
    public void testIsBranch_whenSectionIsInside() throws Exception {
        item.setItem(new PoulpeSection());
        assertFalse(item.isBranch());
    }

    @Test
    public void testIsPersisted_withNotPersisted() throws Exception {
        item.setItem(new PoulpeBranch());
        assertFalse(item.isPersisted());
    }

    @Test
    public void testIsPersisted_withNull() throws Exception {
        assertFalse(item.isPersisted());
    }

    @Test
    public void testIsPersisted_withPersisted() throws Exception {
        PoulpeBranch branch = new PoulpeBranch();
        branch.setId(1L);
        item.setItem(branch);
        assertTrue(item.isPersisted());
    }

    @Test
    public void testGetItem_withNull() throws Exception {
        assertNull(item.getItem(PoulpeBranch.class));
    }

    @Test
    public void testGetItem() throws Exception {
        PoulpeSection section = new PoulpeSection();
        item.setItem(section);
        assertSame(item.getItem(PoulpeSection.class), section);
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void testGetItem_withWrongClass() throws Exception {
        PoulpeSection section = new PoulpeSection();
        item.setItem(section);
        assertSame(item.getItem(PoulpeBranch.class), section);
    }

    @Test
    public void testClearState() throws Exception {
        item.setItem(new PoulpeSection());
        assertNull(item.clearState().getItem());
    }

    @Test
    public void testClearState_withOriginallyNull() throws Exception {
        assertNull(item.clearState().getItem());
    }
}
