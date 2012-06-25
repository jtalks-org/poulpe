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
package org.jtalks.poulpe.model.entity;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class PoulpeBranchTest {
    @Test(dataProvider = "provideBranchWithSection")
    public void testMoveTo_branchChangesItsSection(PoulpeBranch branch) throws Exception {
        PoulpeSection moveFrom = branch.getPoulpeSection();
        PoulpeSection addTo = new PoulpeSection("section2");

        assertSame(branch.moveTo(addTo), moveFrom);
        assertSame(branch.getPoulpeSection(), addTo);
        assertNotSame(moveFrom, addTo);
    }

    @Test(dataProvider = "provideBranchWithSection")
    public void testMoveTo_sectionUpdatesItsBranches(PoulpeBranch branch) throws Exception {
        PoulpeSection addTo = new PoulpeSection("section2");

        branch.moveTo(addTo);
        assertTrue(addTo.containsBranch(branch));
    }

    @Test(dataProvider = "provideBranchWithSection")
    public void testMoveTo_sameSection(PoulpeBranch branch) throws Exception {
        PoulpeSection addTo = branch.getPoulpeSection();

        branch.moveTo(addTo);
        assertSame(branch.getPoulpeSection(), addTo);
        assertTrue(addTo.containsBranch(branch));
    }

    @Test(dataProvider = "provideBranchWithSection")
    public void testRemoveFromSection(PoulpeBranch branch) throws Exception {
        PoulpeSection section = branch.removeFromSection();
        assertNull(branch.getSection());
        assertFalse(section.containsBranch(branch));
    }

    @Test
    public void removeFromSection_withNoSection_shouldDoNothing() throws Exception {
        PoulpeBranch branch = new PoulpeBranch();
        assertNull(branch.removeFromSection());
    }

    @DataProvider
    public Object[][] provideBranchWithSection() {
        PoulpeBranch branch = new PoulpeBranch("test-branch", "test-description");
        PoulpeSection section = new PoulpeSection("test-section", "test-description");
        section.addBranchIfAbsent(branch);
        return new Object[][]{{branch}};
    }
}
