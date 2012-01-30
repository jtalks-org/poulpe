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

import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.fakeSections;
import static org.jtalks.poulpe.web.controller.utils.ObjectCreator.sectionWithBranches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Bekrenev Dmitry
 */
public class DeleteSectionDialogPresenterTest {

    DeleteSectionDialogPresenter presenter;

    @Mock SectionService sectionService;
    @Mock DeleteSectionDialogView view;

    private List<Section> sections = fakeSections();
    private Section section = sectionWithBranches();
    private Section recipient = sectionWithBranches();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new DeleteSectionDialogPresenter();

        presenter.setView(view);
        presenter.setSectionService(sectionService);
    }

    @Test
    public void testInitView() {
        when(sectionService.getAll()).thenReturn(sections);
        presenter.initView();
        verify(view).initSectionList(sections);
    }

    @Test
    public void testDeleteIfmodeDeleteAll() {
        givenDeleteAllMode();

        presenter.delete();

        verify(sectionService).deleteRecursively(section);
        verify(view).closeDialog();
    }

    private void givenDeleteAllMode() {
        when(view.getDeleteMode()).thenReturn(SectionDeleteMode.DELETE_ALL);
        when(view.getDeleteSection()).thenReturn(section);
    }

    @Test
    public void testDeleteIfmodeMoveBranches() {
        givenDeleteAndMoveMode();

        presenter.delete();
        
        verify(sectionService).deleteAndMoveBranchesTo(section, recipient);
        verify(view).closeDialog();
    }

    private void givenDeleteAndMoveMode() {
        when(view.getDeleteMode()).thenReturn(SectionDeleteMode.DELETE_AND_MOVE);
        when(view.getDeleteSection()).thenReturn(section);
        when(view.getRecipientSection()).thenReturn(recipient);
    }
}
