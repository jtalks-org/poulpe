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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestBranchEditorPresenter {

    BranchEditorPresenter presenter = new BranchEditorPresenter();
    @Mock
    BranchService service;
    @Mock
    BranchEditorView view;
    @Captor
    ArgumentCaptor<PoulpeBranch> branchCaptor;
    
    private static final String branchName = "branch";
    private static final String description = "decs";
    
    private final PoulpeBranch branch = new PoulpeBranch(branchName, description);
    
    @BeforeTest
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter.setBranchService(service);
        presenter.setView(view);
    }

    @Test
    public void testUpdateView() {
        List<PoulpeBranch> listOfTwoBranches = Arrays.asList(branch, branch);
        when(service.getAll()).thenReturn(listOfTwoBranches);

        presenter.updateView();

        verify(view).showBranches(listOfTwoBranches);
        verify(service).getAll();
    }
    
    @Test
    public void testDeleteBranch() {
        when(view.getSelectedBranch()).thenReturn(branch);

        presenter.deleteBranch();

        // TODO: is it used?? the method marked as deprecated
        verify(service).deleteBranchRecursively(branchCaptor.capture());
        assertEquals(branchCaptor.getValue(), branch);
    }
}
