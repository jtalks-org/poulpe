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
package org.jtalks.poulpe.web.controller.section.mvvm;

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.section.ForumStructureVm;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.DefaultTreeNode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
@Test
public class ForumStructureVmTest {
    private ComponentService componentService;
    private ForumStructureVm vm;

    @BeforeMethod
    public void setUp() throws Exception {
        componentService = mock(ComponentService.class);
        vm = new ForumStructureVm(componentService);
    }

    @Test(dataProvider = "provideRandomJcommuneWithSections", enabled = false)
    public void testSave(Jcommune jcommune) throws Exception {
        PoulpeSection selectedSection = new PoulpeSection("section", "description");
        when(componentService.getByType(ComponentType.FORUM)).thenReturn(jcommune);
        vm.saveSection();

        verify(componentService).saveComponent(jcommune);
        assertNull(vm.getSelectedItem().getItem());
        jcommune.getSections().contains(selectedSection);
    }

    @DataProvider
    public Object[][] provideRandomJcommuneWithSections() {
        Jcommune jcommune = new Jcommune();
        PoulpeSection sectionA = new PoulpeSection("SectionA");
        sectionA.addOrUpdateBranch(new PoulpeBranch("BranchA"));
        sectionA.addOrUpdateBranch(new PoulpeBranch("BranchB"));
        jcommune.addSection(sectionA);
        PoulpeSection sectionB = new PoulpeSection("SectionB");
        sectionB.addOrUpdateBranch(new PoulpeBranch("BranchD"));
        sectionB.addOrUpdateBranch(new PoulpeBranch("BranchE"));
        jcommune.addSection(sectionB);
        return new Object[][]{{jcommune}};
    }
}
