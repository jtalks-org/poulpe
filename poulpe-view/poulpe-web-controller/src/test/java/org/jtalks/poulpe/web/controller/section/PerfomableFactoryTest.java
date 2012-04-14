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

import static org.jtalks.poulpe.web.controller.utils.ObjectsFactory.fakeSection;
import static org.jtalks.poulpe.web.controller.utils.ObjectsFactory.sectionWithBranches;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
public class PerfomableFactoryTest {

    private PerfomableFactory perfomableFactory;
    
    @Mock SectionPresenter presenter;
    @Mock SectionService sectionService;
    @Mock ComponentService componentService;
    @Mock BranchService branchService;
    @Mock ZkSectionView view;
    @Mock ZkSectionTreeComponent currentSectionTreeComponent;
    @Mock Jcommune forum;

    private PoulpeSection section = sectionWithBranches();
    private PoulpeBranch branch = (PoulpeBranch) section.getBranches().get(0);
    
    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        perfomableFactory = new PerfomableFactory(presenter);
        perfomableFactory.setCurrentSectionTreeComponent(currentSectionTreeComponent);
        perfomableFactory.setSectionService(sectionService);
        perfomableFactory.setComponentService(componentService);
        perfomableFactory.setBranchService(branchService);
        perfomableFactory.setSectionView(view);
    }
    
    @Test
    public void testCreatePerformableCreateSection() {
        Performable perf = perfomableFactory.saveSection(section, forum);
        
        perf.execute();

        verify(forum).addSection(section);
        verify(componentService).saveComponent(forum);
        verify(view).addSection(section);
        verify(view).closeEditSectionDialog();
    }

    @Test
    public void testUpdatePerformableEditSection() {
        Performable perf = perfomableFactory.updateSection(section);
        
        perf.execute();

        verify(currentSectionTreeComponent).updateSectionInView(section);
        verify(view).closeEditSectionDialog();
        verify(sectionService).saveSection(section);
    }

    @Test
    public void testDeleteBranchPerformable() {
        Performable perf = perfomableFactory.deleteBranch(branch);
        perf.execute();
        verify(sectionService).saveSection(section);
        verify(branchService, times(3)).changeGrants(any(PoulpeBranch.class), any(PermissionChanges.class));
        verify(presenter).updateView();
    }

    @Test
    public void testDeleteSectionSaveBranchesPerformable() {
        PoulpeSection section1 = fakeSection(), section2 = fakeSection();
        Performable perf = perfomableFactory.deleteSection(section1, section2);

        perf.execute();

        verify(sectionService).deleteAndMoveBranchesTo(section1, section2);
    }

    @Test
    public void testDeleteSectionWithoutSaveBranchesPerformable() {
        Performable perf = perfomableFactory.deleteSection(section, null);

        perf.execute();
        verify(sectionService).deleteRecursively(section);
    }

}
