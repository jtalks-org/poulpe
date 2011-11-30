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
package org.jtalks.poulpe.web.controller.branch;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Bekrenev Dmitry
 * */
public class TestBranchPresenter {

    @Mock
    SectionService service;
    @Mock
    BranchDialogView view;
    @Mock
    BranchService branchService;
    @Captor
    ArgumentCaptor<Section> sectionCaptor;
    @Captor
    ArgumentCaptor<List<Section>> initSectionCaptor;

    BranchPresenter presenter = new BranchPresenter();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter.setSectionService(service);
        presenter.setView(view);
        presenter.setBranchService(branchService);
    }

    @Test
    public void testInitView() {
        List<Section> sections = new ArrayList<Section>();

        sections.add(new Section());
        sections.add(new Section());
        sections.add(new Section());
        sections.add(new Section());

        when(service.getAll()).thenReturn(sections);

        presenter.initView();

        verify(view).initSectionList(initSectionCaptor.capture());
        assertEquals(initSectionCaptor.getValue().size(), 4);

    }

    @Test
    public void testSaveBranch() throws NotUniqueException {
        Section section = new Section();
        Branch branch = new Branch();

        section.setName("getted section");
        when(view.getSection()).thenReturn(section);

        presenter.saveBranch(section, branch);

        verify(service).saveSection(sectionCaptor.capture());
        assertEquals(sectionCaptor.getValue().getName(), "getted section");
        assertEquals(sectionCaptor.getValue().getBranches().size(), 1);
    }

    @Test
    public void testSaveBranchWhenExceptionHappen() throws NotUniqueException {
        Section section = new Section();
        section.setName("not unique");

        when(view.getSection()).thenReturn(section);
        doThrow(new NotUniqueException()).when(service).saveSection(section);

        presenter.saveBranch();

        verify(view).notUniqueBranchName();
    }

}
