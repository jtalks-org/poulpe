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

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.service.BranchService;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestBranchEditorPresenter {

    BranchEditorPresenter presenter = new BranchEditorPresenter();
    @Mock
    BranchService service;
    @Mock
    BranchEditorView view;

    @Captor
    ArgumentCaptor<Branch> branchCaptor;

    @BeforeTest
    public void tearUp() {
        MockitoAnnotations.initMocks(this);
        presenter.setBranchService(service);
        presenter.setView(view);
    }

    @Test
    public void testUpdateView() {

        when(service.getAll()).thenReturn(getDefaultBranches());

        presenter.updateView();

        verify(view).showBranches(argThat(new isListOfTooBranches()));
        verify(service).getAll();
    }

    @Test
    public void testDeleteBranch() {
        Branch branch = new Branch();
        branch.setName("delete branch");

        when(view.getSelectedBranch()).thenReturn(branch);

        presenter.deleteBranch();

        verify(service).deleteBranch(branchCaptor.capture());
        assertEquals(branchCaptor.getValue().getName(), "delete branch");
    }

    public List<Branch> getDefaultBranches() {
        List<Branch> branches = new ArrayList<Branch>();

        Branch branch = new Branch();
        branch.setName("branch 1");
        branch.setDescription("desc 1");

        branch.setName("branch 2");
        branch.setDescription("desc 2");

        branches.add(branch);
        branches.add(branch);
        return branches;
    }
}

class isListOfTooBranches extends ArgumentMatcher<List<Branch>> {

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(Object argument) {
        return ((List<Branch>) argument).size() == 2;
    }

}
