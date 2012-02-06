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

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.BranchService;

/**
 * This class is implementation the branch editor presenter in pattern Model-View-Presenter
 * 
 * @author Bekrenev Dmitry
 * */

public class BranchEditorPresenter {

    private BranchEditorView view;
    private BranchService branchService;


    /**
     * Sets the service instance which is used for manipulating with stored
     * branches
     * 
     * @param service
     *            The instance branch service
     * */
    public void setBranchService(BranchService service) {
        branchService = service;
    }

    /**
     * Sets the view instance which represent User interface
     * 
     * @param view
     *            The instance BranchEditorView
     * */
    public void setView(BranchEditorView view) {
        this.view = view;
    }


    /**
     * Invocation this method makes view update branch list
     * */
    public void updateView() {
        view.showBranches(branchService.getAll());

    }

    /**
     * Invocation this method makes get selected branch in branch list and delete it 
     * */
    public void deleteBranch() {
        PoulpeBranch branch = view.getSelectedBranch();
        branchService.deleteBranchRecursively(branch);
    }

}
