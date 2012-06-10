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

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.section.dialogs.ConfirmBranchDeletionDialogVm;
import org.jtalks.poulpe.web.controller.section.dialogs.ConfirmSectionDeletionDialogVm;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * @author stanislav bashkirtsev
 */
public class ForumStructureVmTest {


    @DataProvider
    public Object[][] provideRandomJcommuneWithSections() {
        Jcommune jcommune = new Jcommune();
        PoulpeSection sectionA = new PoulpeSection("SectionA");
        sectionA.addOrUpdateBranch(createBranch(sectionA, "BranchA"));
        sectionA.addOrUpdateBranch(createBranch(sectionA, "BranchB"));
        jcommune.addSection(sectionA);
        PoulpeSection sectionB = new PoulpeSection("SectionB");
        sectionB.addOrUpdateBranch(createBranch(sectionB, "BranchD"));
        sectionB.addOrUpdateBranch(createBranch(sectionB, "BranchE"));
        jcommune.addSection(sectionB);
        return new Object[][]{{jcommune}};
    }

    private PoulpeBranch createBranch(PoulpeSection section, String branchName) {
        PoulpeBranch branch = new PoulpeBranch(branchName);
        branch.setId(new Random().nextLong());
        branch.setSection(section);
        return branch;
    }
}
