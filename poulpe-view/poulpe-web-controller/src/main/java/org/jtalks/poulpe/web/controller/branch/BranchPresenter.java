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
package org.jtalks.poulpe.web.controller.branch;


import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;


/**
 * This class is implementation the branch presenter in pattern
 * Model-View-Presenter
 * 
 * @author Bekrenev Dmitry
 * */
public class BranchPresenter {

    private SectionService sectionService;
    private BranchDialogView view;

    
    /**
     * Sets the Section instance
     * @param service The instance section service
     * */
    public void setSectionService(SectionService service){
        sectionService = service;
    }

    /**
     * Sets the view instance which represent User interface
     * 
     * @param view
     *            The instance BranchEditorView
     * */
    public void setView(BranchDialogView view) {
        this.view = view;
    }
    
    /**
     * Init view initial data
     * */
    public void initView() {        
        view.initSectionList( sectionService.getAll() );
    }

    /**
     * Save new or edited branch in db In case when branch with equal name
     * exists, cause open error popup in view.
     * */
    public void saveBranch() {
        Section section = view.getSection();       
        try {
            sectionService.saveSection(section);
        } catch (NotUniqueException e) {
            view.notUniqueBranchName();
        }
        view.hide();
    }

}
