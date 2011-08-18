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

import org.jtalks.poulpe.model.entity.Branch;
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
    Branch getBranch();

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
    void notUniqueBranchName() throws WrongValueException;
}
