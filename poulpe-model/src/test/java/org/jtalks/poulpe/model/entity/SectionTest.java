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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.commons.lang.RandomStringUtils;
import org.jtalks.poulpe.test.fixtures.Fixtures;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SectionTest {
    private PoulpeSection section;
    private PoulpeBranch branch = new PoulpeBranch();

    @BeforeMethod
    public void setUp() {
        section = Fixtures.createSection();
    }

    @Test
    public void testAddBranch() {
        section.addOrUpdateBranch(branch);
        assertEquals(section.getBranches().size(), 1);
    }
    
    @Test
    public void testForCoverageSimleAccessors(){
        section = new PoulpeSection();
        section = new PoulpeSection("Name", "Description");
        section.setName(section.getName());
        section.setDescription(section.getDescription());
        section.setPosition(section.getPosition());
        
    }

    @Test
    public void testAddOrUpdateBranch() {
        PoulpeBranch branch = new PoulpeBranch("some branch");
        branch.setId(15L);
        for (int i = 0; i < 10; i++) {
            section.addOrUpdateBranch(new PoulpeBranch(RandomStringUtils.random(10)));
        }
        section.addOrUpdateBranch(branch);
        branch.setName("new name");

        section.addOrUpdateBranch(branch);

        assertEquals(11, section.getBranches().size());
    }

    @Test
    public void testDeleteBranch() {
        section.addOrUpdateBranch(branch);
        section.deleteBranch(branch);

        assertTrue(section.getBranches().isEmpty());
    }
}
