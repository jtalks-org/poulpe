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

import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.zkoss.zk.ui.WrongValueException;

/**
 * Interface for representation view single branch
 * 
 * @author Bekrenev Dmitry
 * */
public interface BranchDialogView {

    /**
     * Get new or edited branch from view
     * 
     * @return Branch new or edited branch
     * */
    Section getSection();

    /**
     * Cause hiding current dialog
     * */
    void hide();

    /**
     * Show dialog and fill fields name and description
     * 
     * @param branch
     *            name and description current branch will fill dialog fields
     * */
    void show(Branch branch);

    /**
     * Show dialog
     * */
    void show();

    /**
     * Cause throw exception for popup error message
     * */
    void notUniqueBranchName();

    /**
     * Init Section combobox list
     * 
     * @param sections
     *            list sections
     * */
    void initSectionList(List<Section> sections);
    
    /**
     * Get branch from view
     * 
     * @return Branch branch
     * */
    Branch getBranch (Section section);
}
