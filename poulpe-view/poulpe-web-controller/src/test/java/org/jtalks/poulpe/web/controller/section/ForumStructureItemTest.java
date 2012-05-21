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
package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
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
    public void testIsSection() throws Exception {
        item.setItem(new PoulpeSection());
        assertTrue(item.isSection());
    }

    @Test
    public void testIsSection_whenNull() throws Exception {
        assertFalse(item.isSection());
    }

    @Test
    public void testGetItemAsBranch() throws Exception {
        PoulpeBranch branch = new PoulpeBranch();
        item.setItem(branch);
        assertSame(item.getBranchItem(), branch);
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void testGetItemAsBranch_isNotBranch() throws Exception {
        item.setItem(new PoulpeSection());
        item.getBranchItem();
    }

    @Test
    public void testGetItemAsBranch_isNull() throws Exception {
        assertNull(item.getBranchItem());
    }

    @Test
    public void testGetItemAsSection() throws Exception {
        PoulpeSection branch = new PoulpeSection();
        item.setItem(branch);
        assertSame(item.getSectionItem(), branch);
    }

    @Test
    public void testPrepareBranchItemForEditing_whenCreatingNew() throws Exception {
        ForumStructureItem preparedItem = item.prepareBranchItemForEditing(true);
        assertNotSame(preparedItem, item);
        assertThat(preparedItem.getItem(), instanceOf(PoulpeBranch.class));
    }

    @Test
    public void testPrepareBranchItemForEditing_whenEditingExisting() throws Exception {
        ForumStructureItem preparedItem = item.prepareBranchItemForEditing(false);
        assertSame(preparedItem, item);
    }

    @Test
    public void testPrepareSectionItemForEditing_whenCreatingNew() throws Exception {
        ForumStructureItem preparedItem = item.prepareBranchItemForEditing(true);
        assertNotSame(preparedItem, item);
        assertThat(preparedItem.getItem(), instanceOf(PoulpeBranch.class));
    }

    @Test
    public void testPrepareSectionItemForEditing_whenEditingExisting() throws Exception {
        ForumStructureItem preparedItem = item.prepareBranchItemForEditing(false);
        assertSame(preparedItem, item);
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void testGetItemAsSection_isNotSection() throws Exception {
        item.setItem(new PoulpeBranch());
        item.getSectionItem();
    }

    @Test
    public void testGetItemAsSection_isNull() throws Exception {
        assertNull(item.getSectionItem());
    }

    @Test
    public void testIsSection_whenBranch() throws Exception {
        item.setItem(new PoulpeBranch());
        assertFalse(item.isSection());
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
