package org.jtalks.poulpe.web.controller.section;

import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;

public interface SectionView {

	/**
	 * Remove specified section from view TODO the implied way to remove section
	 * from view is to find proper SectionTreeComponent and remove it from
	 * SectionView children
	 * 
	 * @param section
	 */
	public void removeSection(Section section);

	/**
	 * Show the section specified in argument in view
	 * 
	 * @param section
	 */
	public void showSection(Section section);

	/**
	 * Show sections specified in argument in view
	 * 
	 * @param sections
	 */
	public void showSections(List<Section> sections);

	/**
	 * Ask view to show EditSectionDialog
	 */
	public void openEditSectionDialog(String name, String description);

	/**
	 * Ask view to hide EditSectionDialog
	 */
	public void closeEditSectionDialog();

	/**
	 * Ask view to show NewSectionDialog
	 */
	public void openNewSectionDialog();

	/**
	 * Invoking this method causes to close NewSectionDialog
	 */
	public void closeNewSectionDialog();

	/**
	 * Ask the view to close all dialogs
	 */
	public void closeDialogs();

	/**
	 * Show error message in the NewSectionDialog
	 * 
	 * @return
	 */
	public void openErrorPopupInNewSectionDialog(String error);

	/**
	 * Show error message in the EditSectionDialog
	 * 
	 * @return
	 */
	public void openErrorPopupInEditSectionDialog(String error);

	/**
	 * Close delete dialog for section
	 */
	void closeDeleteSectionDialog();

	/**
	 * Show delete section dialog
	 * 
	 * @param sections
	 *            list of sections that might take branches of the section being
	 *            deleted
	 */
	void openDeleteSectionDialog(List<Section> sections);

	// TODO realize to create new branch
	void openNewBranchDialog();

	// TODO realize to create new branch
	void closeNewBranchDialog();
	
	//TODO realize to create edit a branch
	void openEditBranchDialog();
	//TODO realize to create edit a branch
	void closeEditBranchDialog();
	
	
	boolean isEditDialogOpen();
	boolean isNewDialogOpen();
	boolean isDeleteDialogOpen();
	

}
