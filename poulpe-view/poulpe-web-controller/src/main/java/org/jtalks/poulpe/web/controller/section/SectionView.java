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

import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;

/**
 * @author Konstantin Akimov
 * */
public interface SectionView {

    /**
     * Remove specified section from view TODO the implied way to remove section
     * from view is to find proper SectionTreeComponent and remove it from
     * SectionView children
     * 
     * @param section for remove
     */
    void removeSection(Section section);

    /**
     * Show the section specified in argument in view
     * 
     * @param section instance for show
     */
    void showSection(Section section);

    /**
     * Show sections specified in argument in view
     * 
     * @param sections list for show
     */
    void showSections(List<Section> sections);

    /**
     * Ask view to show EditSectionDialog
     * @param name section
     * @param description section
     */
    void openEditSectionDialog(String name, String description);

    /**
     * Ask view to hide EditSectionDialog
     */
   void closeEditSectionDialog();

    /**
     * Ask view to show NewSectionDialog
     */
    void openNewSectionDialog();

    /**
     * Invoking this method causes to close NewSectionDialog
     */
    void closeNewSectionDialog();

    /**
     * Ask the view to close all dialogs
     */
    void closeDialogs();

    /**
     * Show error message in the NewSectionDialog
     * @param error description
     */
   void openErrorPopupInNewSectionDialog(String error);

    /**
     * Show error message in the EditSectionDialog  
     * @param error description 
     */
    public void openErrorPopupInEditSectionDialog(String error);

    /**
     * Close delete dialog for section
     */
    void closeDeleteSectionDialog();

    /**
     * Show delete section dialog
     * 
     * @param victim
     *            list of sections that might take branches of the section being
     *            deleted
     */
    void openDeleteSectionDialog(Section victim);

    /**
     * open dialog for create new branch
     * */
    void openNewBranchDialog();

    /**
     * close dialog for create new branch
     * */
    void closeNewBranchDialog();

    /**
     * open dialog for edit selected branch
     * @param branch for edit
     * */
    void openEditBranchDialog(Branch branch);

    /**
     * close dialog for edit selected branch
     * */
    void closeEditBranchDialog();

    /**
     * open dialog for delete branch
     * @param branch for delete
     * */
    void openDeleteBranchDialog(Branch branch);

    /**
     * Check edit section dialog opened
     * @return true if dialog open and false otherwise 
     */
    boolean isEditDialogOpen();
    /**
     * Check new section dialog opened
     * @return true if dialog open and false otherwise 
     */
    boolean isNewDialogOpen();
    /**
     * Check edit dialog opened
     * @return always return false
     */
    boolean isDeleteDialogOpen();
    
    /**
     * Open the moderator dialog
     * @param branch
     */
    void openModeratorDialog(Branch branch);
    /**
     * Close the moderator dialog
     */
    void closeModeratorDialog();    

}
