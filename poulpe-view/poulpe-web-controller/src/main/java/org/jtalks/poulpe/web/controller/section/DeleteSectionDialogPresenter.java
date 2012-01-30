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

import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;

/**
 * This class is implementation the dialog section presenter in pattern
 * Model-View-Presenter
 * 
 * @author Bekrenev Dmitry
 * @author Alexey Grigorev
 */
public class DeleteSectionDialogPresenter {

    private SectionService sectionService;
    private DeleteSectionDialogView view;

    /**
     * Use for initialize combobox which contains sections
     */
    public void initView() {
        view.initSectionList(sectionService.getAll());
    }

    /**
     * Depending on user choice, deleting all branches or moving 
     */
    public void delete() {
        if (deleteAllBranches()) {
            deleteBranches();
        } else {
            moveBranches();
        }

        view.closeDialog();
    }

    private boolean deleteAllBranches() {
        return view.getDeleteMode() == SectionDeleteMode.DELETE_ALL;
    }

    private void deleteBranches() {
        sectionService.deleteRecursively(view.getSectionToDelete());
    }
    
    private void moveBranches() {
        Section deleteSection = view.getSectionToDelete();
        Section selectedSection = view.getRecipientSection();
        sectionService.deleteAndMoveBranchesTo(deleteSection, selectedSection);
    }

    public void show(Section section) {
        view.showDialog(section);
    }

    /**
     * Sets the view instance which represent User interface
     * 
     * @param view The instance DeleteSectionDialogView
     */
    public void setView(DeleteSectionDialogView view) {
        this.view = view;
    }

    /**
     * Sets section service for manipulating sections
     * 
     * @param sectionService The instance SectionService
     */
    public void setSectionService(SectionService sectionService) {
        this.sectionService = sectionService;
    }

}
