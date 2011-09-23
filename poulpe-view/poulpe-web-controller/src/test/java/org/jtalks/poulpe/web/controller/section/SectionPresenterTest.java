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

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SectionPresenterTest extends SectionPresenter {

    private SectionPresenter presenter;

    @Mock
    private SectionService service;

    @Mock
    SectionView view;

    @BeforeTest
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new SectionPresenter();
        presenter.setSectionService(service);
//        view = mock(SectionViewImpl.class);
    }

    @Test
    public void validateSectionTest() {

        when(service.isSectionExists(anyString())).thenReturn(true).thenReturn(
                false);

        String testName = "Test name";
        String testDescription = "Test name";
        assertNotNull(presenter.validateSection("", testDescription));
        assertNotNull(presenter.validateSection(testName, testDescription));
        assertNull(presenter.validateSection(testName, testDescription));
    }

    @Test
    public void initViewTest() {

        List<Section> fakeSections = getFakeSections(3);
        when(service.getAll()).thenReturn(fakeSections);

        presenter.initView(view);

//        verify(view).showSections(
//                argThat(new SectionsListMatcher(fakeSections)));
//        verify(view).closeDialogs();
    }

    @Test
    public void openDeleteDialogTest() {

        List<Section> fakeSections = getFakeSections(9);
        List<Section> cloneOfFakeSections = new ArrayList<Section>();
        for (int i = 0; i < fakeSections.size(); i++) {
            if (i != 3) {
                cloneOfFakeSections.add(fakeSections.get(i));
            }
        }

        when(service.getAll()).thenReturn(fakeSections);
        presenter.initView(view);
        presenter.openDeleteDialog(fakeSections.get(3));

        // deleteSectionDialog should be invoked with a list of sections
        // the being deleted section shouldn't presence in this list
//        verify(view).openDeleteSectionDialog(
//                argThat(new SectionMatcher(fakeSections.get(3).getName(),fakeSections.get(3).getDescription())));
    }

    @Test
    public void openEditDialogTest() {
        List<Section> fakeSections = getFakeSections(9);
        when(service.getAll()).thenReturn(fakeSections);
        presenter.initView(view);

        SectionTreeComponent sectionTreeComponent = mock(SectionTreeComponent.class);
        when(sectionTreeComponent.getSelectedObject())
                .thenReturn(fakeSections.get(3)).thenReturn(new Branch())
                .thenReturn(null);

        // section
        presenter.openEditDialog(sectionTreeComponent);
        verify(view).openEditSectionDialog(fakeSections.get(3).getName(),
                fakeSections.get(3).getDescription());

        // branch
//        presenter.openEditDialog(sectionTreeComponent);
//        verify(view, never()).openEditBranchDialog();

        // null
//        presenter.openEditDialog(sectionTreeComponent);
//        verify(view, never()).openEditBranchDialog();

    }

    @Test
    public void openNewDialogTest() {
        List<Section> fakeSections = getFakeSections(9);
        when(service.getAll()).thenReturn(fakeSections);
        presenter.initView(view);

        presenter.openNewSectionDialog();
        verify(view).openNewSectionDialog();

    }

//    @Test
//    public void editSectionFailsTest() {
//        List<Section> fakeSections = getFakeSections(9);
//        when(service.getAll()).thenReturn(fakeSections);
//        when(service.isSectionExists("1")).thenReturn(true);
//
//        presenter.initView(view);
//
//        SectionTreeComponent sectionTreeComponent = mock(SectionTreeComponent.class);
//        when(sectionTreeComponent.getSelectedObject()).thenReturn(
//                fakeSections.get(3));
//
//        presenter.openEditDialog(sectionTreeComponent);
//        presenter.editSection("1", "2");
//        verify(view).openErrorPopupInEditSectionDialog(
//                "sections.error.section_name_already_exists");
//        presenter.editSection("", "2");
//        verify(view).openErrorPopupInEditSectionDialog(
//                "sections.error.section_name_cant_be_void");
//        presenter.editSection(null, "2");
//        verify(view).openErrorPopupInEditSectionDialog(
//                "sections.error.section_name_cant_be_void");
//
//        try {
//            doThrow(new NotUniqueException()).when(service).saveSection(
//                    any(Section.class));
//            presenter.editSection("2", "2");
//            verify(view).openErrorPopupInEditSectionDialog(
//                    "sections.error.exeption_during_saving_process");
//
//        } catch (NotUniqueException e) {
//
//        }
//    }
//
//    @Test
//    public void editSectionTest() {
//        List<Section> fakeSections = getFakeSections(9);
//        when(service.getAll()).thenReturn(fakeSections);
//        when(service.isSectionExists("1")).thenReturn(false);
//
//        presenter.initView(view);
//
//        SectionTreeComponent sectionTreeComponent = mock(SectionTreeComponent.class);
//        when(sectionTreeComponent.getSelectedObject()).thenReturn(
//                fakeSections.get(3));
//
//        presenter.openEditDialog(sectionTreeComponent);
//        presenter.editSection("1", "2");        
//        try {
//            verify(service).saveSection(argThat(new SectionMatcher("1", "2")));
//        } catch (NotUniqueException e) {
//            assertFalse(true);
//        }
//
//    }

    private List<Section> getFakeSections(int sizeOfCollection) {
        List<Section> sections = new ArrayList<Section>();
        for (int i = 0; i < sizeOfCollection; i++) {
            sections.add(getFakeSection("fake " + i, "description " + i));
        }
        return sections;
    }

    private Section getFakeSection(String name, String description) {
        Section section = new Section();
        section.setName(name);
        section.setDescription(description);
        return section;
    }

}
