/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;

/**
 * @author Bekrenev Dmitry
 * 
 *         This class is implementation the dialog section presenter in pattern
 *         Model-View-Presenter
 * 
 * */
public class DeleteSectionDialogPresenter {

    private SectionService sectionService;
    private DeleteSectionDialogView view;

    /**
     * Sets the view instance which represent User interface
     * 
     * @param view
     *            The instance DeleteSectionDialogView
     * */
    public void setView(DeleteSectionDialogView view) {
        this.view = view;
    }

    /**
     * Sets section service for manipulating sections
     * 
     * @param sectionService
     *            The instance SectionService
     * */
    public void setSectionService(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    /**
     * Use for initialize combobox which contains sections
     * */
    public void initView() {
        view.initSectionList(sectionService.getAll());
    }

    /**
     * In depend user choice delete section recursively or with moving victim's
     * branches
     * */
    public void delete() {

        if (view.getDeleteMode().equals("deleteAll")) {
            sectionService.deleteRecursively(view.getDeleteSection());
        } else {

            Section deleteSection = view.getDeleteSection();
            Section selectedSection = view.getSelectedSection();
            sectionService.deleteAndMoveBranchesTo(deleteSection,
                    selectedSection);

        }

        view.closeDialog();
    }

}
