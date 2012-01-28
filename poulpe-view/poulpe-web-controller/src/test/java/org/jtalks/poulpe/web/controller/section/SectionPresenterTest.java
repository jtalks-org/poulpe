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

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.fakeBranch;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.fakeSection;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.fakeSections;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.sectionWithBranches;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.validation.EntityValidator;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.section.SectionPresenter.CreatePerformable;
import org.jtalks.poulpe.web.controller.section.SectionPresenter.DeleteBranchPerformable;
import org.jtalks.poulpe.web.controller.section.SectionPresenter.DeleteSectionPerformable;
import org.jtalks.poulpe.web.controller.section.SectionPresenter.UpdatePerformable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Konstantin Akimov
 * @author Vahluev Vyacheslav
 * @author Alexey Grigorev
 */
public class SectionPresenterTest {

    private SectionPresenter presenter;

    @Mock SectionService service;
    @Mock SectionViewImpl view;
    @Mock DialogManager dialogManager;
    @Mock EntityValidator entityValidator;
    @Mock SectionTreeComponentImpl currentSectionTreeComponent;

    private List<Section> sections = fakeSections();
    private Section section = sections.get(0);
    private Branch branch = fakeBranch();
    
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        presenter = new SectionPresenter();
        presenter.setSectionService(service);
        presenter.setDialogManager(dialogManager);
        presenter.setEntityValidator(entityValidator);
        presenter.setCurrentSectionTreeComponentImpl(currentSectionTreeComponent);

        when(service.getAll()).thenReturn(sections);
        presenter.initView(view);
    }
    
    @Test
    public void testInitView() {
        
        verify(view).showSections(sections);
        verify(view).closeDialogs();
    }

    @Test
    public void testOpenDeleteDialogOK() {
        presenter.openDeleteDialog(section);
        verify(view).openDeleteSectionDialog(section);
    }

    @Test
    public void testOpenDeleteDialogNullObject() {
        presenter.openDeleteDialog(null);
        verify(view, never()).openDeleteSectionDialog(any(Section.class));
    }

    @Test
    public void testOpenDeleteDialogUnproperObject() {
        // verify that only Section or Branch can be deleted
        presenter.openDeleteDialog(new TopicType());
        verify(view, never()).openDeleteSectionDialog(any(Section.class));
    }

    @Test
    public void testOpenDeleteDialogWithWrongArguments() {
        presenter.openDeleteDialog(branch);
        verify(dialogManager).confirmDeletion(eq(branch.getName()), any(DialogManager.Performable.class));
    }

    @Test
    public void testOpenEditDialogNullParams() {
        presenter.openEditDialog(currentSectionTreeComponent);

        verify(view, never()).openEditBranchDialog(any(Branch.class));
        verify(view, never()).openEditSectionDialog(anyString(), anyString());
    }

    @Test
    public void testOpenEditDialogTopicType() {
        when(currentSectionTreeComponent.getSelectedObject()).thenReturn(new TopicType());

        presenter.openEditDialog(currentSectionTreeComponent);

        verify(view, never()).openEditBranchDialog(any(Branch.class));
        verify(view, never()).openEditSectionDialog(anyString(), anyString());
    }

    @Test
    public void testOpenEditSectionDialog() {
        givenSectionSelected();
        presenter.openEditDialog(currentSectionTreeComponent);
        verify(view).openEditSectionDialog(section.getName(), section.getDescription());
    }

    @Test
    public void testOpenEditBranchDialog() {
        givenBranchSelected();
        presenter.openEditDialog(currentSectionTreeComponent);
        verify(view).openEditBranchDialog(branch);
    }

    private void givenBranchSelected() {
        when(currentSectionTreeComponent.getSelectedObject()).thenReturn(branch);
    }

    @Test
    public void testOpenNewSectionDialog() {
        presenter.openNewSectionDialog();
        verify(view).openNewSectionDialog();
    }

    @Test
    public void testOpenNewBranchDialog() {
        presenter.openNewBranchDialog(currentSectionTreeComponent);
        verify(view).openNewBranchDialog();
    }

    @Test
    public void testRemoveSectionFromView() {
        presenter.removeSectionFromView(section);
        verify(view).removeSection(section);
    }

    @Test
    public void testDeleteSectionNull() {
        givenNoSectionSelected();
        presenter.deleteSection(null);
        verify(dialogManager, never()).confirmDeletion(anyString(), any(DialogManager.Performable.class));
    }

    private void givenNoSectionSelected() { 
        // currentSectionTreeComponent.getSelectedObject() returns null
    }

    @Test
    public void testDeleteSectionNullWithName() {
        givenNoSectionSelected();
        presenter.deleteSection(section);
        verify(dialogManager, never()).confirmDeletion(anyString(), any(DialogManager.Performable.class));
    }

    @Test
    public void testDeleteSectionOK() {
        givenSectionSelected();
        presenter.deleteSection(null);
        verify(dialogManager).confirmDeletion(anyString(), any(DialogManager.Performable.class));
    }

    private void givenSectionSelected() {
        when(currentSectionTreeComponent.getSelectedObject()).thenReturn(section);
    }

    @Test
    public void testDeleteSectionOKWithName() {
        givenSectionSelected();
        presenter.deleteSection(fakeSection());
        verify(dialogManager).confirmDeletion(anyString(), any(DialogManager.Performable.class));
    }

    @Test
    public void testCreatePerformableCreateSection() {
        CreatePerformable perf = presenter.new CreatePerformable(section);

        perf.execute();

        verify(view).showSection(section);
        verify(view).closeNewSectionDialog();
        verify(service).saveSection(section);
    }

    @Test
    public void testUpdatePerformableEditSection() {
        UpdatePerformable perf = presenter.new UpdatePerformable(section);

        perf.execute();

        verify(currentSectionTreeComponent).updateSectionInView(section);
        verify(view).closeEditSectionDialog();
        verify(service).saveSection(section);
    }

    @Test
    public void testDeleteBranchPerformable() {
        Section section = sectionWithBranches();
        Branch branch = section.getBranches().get(0);
        
        DeleteBranchPerformable perf = presenter.new DeleteBranchPerformable(branch);
        perf.execute();
        
        verify(service).saveSection(section);
    }

    
//    @Test
//    public void testDeleteBranchPerformable() {
//        Section fakeSection = fakeSection();
//        Branch branch1 = fakeBranch(), branch2 = fakeBranch();
//        branch1.setSection(fakeSection);
//        branch2.setSection(fakeSection);
//        
//        fakeSection.addOrUpdateBranch(branch1);
//        fakeSection.addOrUpdateBranch(branch2);
//
//        DeleteBranchPerformable perf = presenter.new DeleteBranchPerformable(branch1);
//        perf.execute();
//
//        assertEquals(fakeSection.getBranches().size(), 1);
//        assertEquals(fakeSection.getBranches().get(0), branch2);
//        
//        verify(service).saveSection(fakeSection);
//    }

    @Test
    public void testDeleteSectionSaveBranchesPerformable() {
        Section section1 = fakeSection(), section2 = fakeSection();
        
        DeleteSectionPerformable perf = presenter.new DeleteSectionPerformable(section1, section2);
        perf.execute();

        verify(service).deleteAndMoveBranchesTo(section1, section2);
    }

    
//    @Test
//    public void testDeleteSectionSaveBranchesPerformable() {
//        List<Section> fakeSections = fakeSections();
//        when(service.getAll()).thenReturn(fakeSections);
//        presenter.initView(view);
//
//        Section fakeSection = fakeSections.get(3);
//        Branch fakeBranch = fakeBranch();
//        Branch fakeBranch_1 = fakeBranch();
//        fakeBranch.setSection(fakeSection);
//        fakeBranch_1.setSection(fakeSection);
//        fakeSection.addOrUpdateBranch(fakeBranch);
//        fakeSection.addOrUpdateBranch(fakeBranch_1);
//
//        Section fakeSection_1 = fakeSections.get(3);
//        Branch fakeBranch_2 = fakeBranch();
//        Branch fakeBranch_3 = fakeBranch();
//        fakeBranch_2.setSection(fakeSection_1);
//        fakeBranch_3.setSection(fakeSection_1);
//        fakeSection_1.addOrUpdateBranch(fakeBranch_2);
//        fakeSection_1.addOrUpdateBranch(fakeBranch_3);
//
//        DeleteSectionPerformable perf = presenter.new DeleteSectionPerformable(fakeSection, fakeSection_1);
//
//        perf.execute();
//
//        verify(service).deleteAndMoveBranchesTo(argThat(new SectionMatcher(fakeSection)),
//                argThat(new SectionMatcher(fakeSection_1)));
//        assertEquals(fakeSection_1.getBranches().size(), 4);
//        assertTrue(fakeSection_1.getBranches().contains(fakeBranch));
//        assertTrue(fakeSection_1.getBranches().contains(fakeBranch_1));
//        assertTrue(fakeSection_1.getBranches().contains(fakeBranch_2));
//        assertTrue(fakeSection_1.getBranches().contains(fakeBranch_3));
//    }

    @Test
    public void testDeleteSectionWithoutSaveBranchesPerformable() {
        DeleteSectionPerformable perf = presenter.new DeleteSectionPerformable(section, null);
        perf.execute();
        verify(service).deleteRecursively(section);
    }

}
