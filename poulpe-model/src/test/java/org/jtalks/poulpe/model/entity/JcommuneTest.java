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

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Guram Savinov
 */
public class JcommuneTest {
    @Test(dataProvider = "provideFilledJcommune")
    public void testMoveSection(Jcommune jcommune) {
        List<PoulpeSection> sections = jcommune.getSections();
        PoulpeSection section = sections.get(1);
        PoulpeSection target = sections.get(3);
        jcommune.moveSection(section, target);
        assertEquals(sections.get(2), section);
        assertEquals(sections.get(3), target);
    }

    @Test(dataProvider = "provideFilledJcommune")
    public void testAddOrUpdateSection(Jcommune jcommune) {
        List<PoulpeSection> sections = jcommune.getSections();
        PoulpeSection exist = sections.get(1);
        exist.setName("exist");
        jcommune.addOrUpdateSection(exist);
        assertEquals(sections.get(1), exist);
        PoulpeSection notExist = new PoulpeSection("not exist");
        jcommune.addOrUpdateSection(notExist);
        assertEquals(sections.get(sections.size() - 1), notExist);
    }

    @DataProvider
    public Object[][] provideFilledJcommune() {
        Jcommune jcommune = new Jcommune();
        for (int i = 0; i < 5; i++) {
            PoulpeSection section = new PoulpeSection("section-name" + i, "section-description" + i);
            section.getBranches().add(new PoulpeBranch("branch-name" + i * 3));
            section.getBranches().add(new PoulpeBranch("branch-name" + i * 3 + 1));
            section.getBranches().add(new PoulpeBranch("branch-name" + i * 3 + 2));
            jcommune.getSections().add(section);
        }
        return new Object[][]{{jcommune}};
    }
}
