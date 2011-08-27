package org.jtalks.poulpe.web.controller.section;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.DialogManagerImpl;
import org.jtalks.poulpe.web.controller.branch.BranchPresenter;

/**
 * This class is used as Presenter layer in Model-View-Presenter pattern for
 * managing Section entities
 * 
 * @author Konstantin Akimov
 * 
 */
public class SectionPresenter {

	/**
	 * initialized via Spring DI
	 */
	private SectionService sectionService;
	private SectionView sectionView;

	private SectionTreeComponent currentSectionTreeComponent;

	public void setSectionService(SectionService service) {
		this.sectionService = service;
	}

	// View management methods

	/**
	 * initialize main view
	 * 
	 * @param view
	 */
	public void initView(SectionView view) {
		this.sectionView = view;
		List<Section> sections = sectionService.getAll();
		view.showSections(sections);
		sectionView.closeDialogs();
	}

	/**
	 * Remove section from sectionView
	 * 
	 * @param section
	 */
	public void removeSectionFromView(Section section) {
		sectionView.removeSection(section);
	}

	/**
	 * This method is used to show EditSectionDialog for editing for specified
	 * in argument section
	 * 
	 * @param section
	 *            to edit
	 */
	public void openEditDialog(SectionTreeComponent currentSectionTreeComponent) {
		Object object = currentSectionTreeComponent.getSelectedObject();
		if (!(object instanceof Section) && !(object instanceof Branch)) {
			return;
		}
		this.currentSectionTreeComponent = currentSectionTreeComponent;
		if (object instanceof Section) {
			Section section = (Section) object;
			
			sectionView.openEditSectionDialog(section.getName(), section.getDescription());
		} else if (object instanceof Branch) {
			// TODO implement openEditBranchDialog
		}

	}

	public void openDeleteDialog(Object object) {
		if (!(object instanceof Section)) {
			return;
		}
		Section section = (Section) object;
		List<Section> sections = sectionService.getAll();
		sections.remove(object);
		sectionView.openDeleteSectionDialog(sections);
	}

	/**
	 * This method is used to show NewSectionDialog for creating new Section
	 * 
	 * @param section
	 *            to edit
	 */
	public void openNewBranchDialog(SectionTreeComponent sectionTreeComponent) {
		this.currentSectionTreeComponent = sectionTreeComponent;
		sectionView.openNewBranchDialog();
	}

	public void addNewBranch(String name, String description) {
		// TODO process branch data
		Branch tmpBranch = new Branch();
		tmpBranch.setName("TEST BRANCH");
		this.currentSectionTreeComponent.addBranchToView(tmpBranch);
		sectionView.closeNewBranchDialog();
	}

	/**
	 * This method is used to show NewSectionDialog for creating new Section
	 * 
	 * @param section
	 *            to edit
	 */
	public void openNewSectionDialog() {
		sectionView.openNewSectionDialog();

	}

	// End of view management methods

	// Section management methods

	/**
	 * This method is invoked when the user saves editions and push edit button
	 */
	public void editSection(final String name, final String description) {
		

		final Section section = (Section) this.currentSectionTreeComponent
				.getSelectedObject();

		String errorLabel = null;
		if ((errorLabel = validateSection(name, description)) != null) {
			sectionView.openErrorPopupInEditSectionDialog(errorLabel);
			return;
		}

		DialogManager dm = new DialogManagerImpl();
		dm.confirmEdition(name, new DialogManager.Performable() {

			@Override
			public void execute() {
				section.setName(name);
				section.setDescription(description);
				try {
					sectionService.saveSection(section);
				} catch (NotUniqueException e) {
					Logger.getLogger(BranchPresenter.class.getName()).log(
							Level.SEVERE, null, e);
					sectionView
							.openErrorPopupInEditSectionDialog("sections.error.exeption_during_saving_process");
				}

				// its not necessary here because of section was transferred as a reference
				SectionPresenter.this.currentSectionTreeComponent
						.updateSectionInView(section);
				sectionView.closeEditSectionDialog();
			}
		});
		sectionView.closeEditSectionDialog();

	}

	protected String validateSection(String name, String description) {
		if (name == null || name.equals("")) {
			return "sections.error.section_name_cant_be_void";
		} else if (sectionService.isSectionExists(name)) {
			return "sections.error.section_name_already_exists";
		}
		return null;
	}

	/**
	 * Create new Section object and save it if there no any Sections with the
	 * same name in other cases (the name is already) should display error
	 * dialog
	 */
	public void addNewSection(final String name, final String description) {
		// final String name = sectionView.getNewSectionName();
		// final String description = sectionView.getNewSectionDescription();

		String errorLabel = null;
		if ((errorLabel = validateSection(name, description)) != null) {
			sectionView.openErrorPopupInNewSectionDialog(errorLabel);
			return;
		}

		DialogManager dm = new DialogManagerImpl();
		dm.confirmCreation(name, new DialogManager.Performable() {

			@Override
			public void execute() {
				Section section = new Section();
				section.setName(name);
				section.setDescription(description);
				try {
					sectionService.saveSection(section);
				} catch (NotUniqueException e) {
					Logger.getLogger(BranchPresenter.class.getName()).log(
							Level.SEVERE, null, e);
					sectionView
							.openErrorPopupInNewSectionDialog("sections.error.exeption_during_saving_process");
				}

				sectionView.showSection(section);
				sectionView.closeNewSectionDialog();
			}
		});

	}

	/**
	 * Delete section via sectionService
	 * 
	 * @param section
	 *            if specified than all branches should be add as children to
	 *            this section. If null then all children should be also deleted
	 */
	public void deleteSection(final Section recipient) {
		DialogManager dm = new DialogManagerImpl();
		Object selectedObject = this.currentSectionTreeComponent.getSelectedObject();
		if (!(selectedObject instanceof Section)) {
			return;
		}
		
		final Section victim = (Section) selectedObject;
		
		dm.confirmDeletion(victim.getName(), new DialogManager.Performable() {

			@Override
			public void execute() {
				if (recipient == null) {

					sectionService.deleteRecursively(victim);
					return;
				} else {
					sectionService.deleteAndMoveBranchesTo(victim, recipient);
					removeSectionFromView(victim);

					// should save also all children
					try {
						sectionService.saveSection(recipient);
					} catch (NotUniqueException e) {
						// TODO add appropriate handling of exception
					}
				}
			}
		});

	}
	// The end of section management methods

}
