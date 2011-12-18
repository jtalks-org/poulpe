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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.branch.BranchPresenter;

/**
 * This class is used as Presenter layer in Model-View-Presenter pattern for
 * managing Section entities
 * 
 * @author Konstantin Akimov
 * @author Vahluev Vyacheslav
 * 
 */
public class SectionPresenter {

    /**
     * initialized via Spring DI
     */
    private SectionService sectionService;
    private SectionView sectionView;
    private SectionTreeComponent currentSectionTreeComponent;
    private DialogManager dialogManager;

    public static final String ERROR_LABEL_SECTION_NAME_ALREADY_EXISTS = "sections.error.section_name_already_exists";
    public static final String ERROR_LABEL_SECTION_NAME_CANT_BE_VOID = "sections.error.section_name_cant_be_void";
    public static final String ERROR_LABEL_SECTION_NAME_VERY_LONG = "sections.editsection.name.err";

    /**
     * @param service
     *            set section service instance
     * */
    public void setSectionService(SectionService service) {
        this.sectionService = service;
    }

    // View management methods

    /**
     * initialize main view SectionView instance
     * 
     * @param view
     *            instance
     */
    public void initView(SectionView view) {
        this.sectionView = view;
        updateView();
    }

    /**
     * Use when need update view
     * */
    public void updateView() {
        List<Section> sections = sectionService.getAll();
        sectionView.showSections(sections);
        sectionView.closeDialogs();
    }

    /**
     * Remove section from sectionView
     * 
     * @param section
     *            which will be removed from view
     */
    public void removeSectionFromView(Section section) {
        sectionView.removeSection(section);
    }

    /**
     * This method is used to show dialog for editing section or branch
     * 
     * @param currentSectionTreeComponent
     *            from this instance we get selected object
     */
    public void openEditDialog(SectionTreeComponent currentSectionTreeComponent) {
        Object object = currentSectionTreeComponent.getSelectedObject();
        if (!(object instanceof Section) && !(object instanceof Branch)) {
            return;
        }
        setCurrentSectionTreeComponent(currentSectionTreeComponent);
        if (object instanceof Section) {
            Section section = (Section) object;
            sectionView.openEditSectionDialog(section.getName(),
                    section.getDescription());
        } else if (object instanceof Branch) {

            sectionView.openEditBranchDialog((Branch) object);
        }

    }

    /**
     * Method used for delete section or branch.
     * 
     * @param object
     *            can be Section or Branch instance
     * */
    public void openDeleteDialog(Object object) {
        if (!(object instanceof Section) && !(object instanceof Branch)) {
            return;
        }
        if (object instanceof Section) {
            Section section = (Section) object;
            sectionView.openDeleteSectionDialog(section);
        } else if (object instanceof Branch) {
            final Branch branch = (Branch) object;
            dialogManager.confirmDeletion(branch.getName(),
                    new DeleteBranchPerformable(branch));
        }

    }

    /**
     * This method is used to show new branch dialog
     * 
     * @param sectionTreeComponent
     *            method save reference on this instance
     */
    public void openNewBranchDialog(SectionTreeComponent sectionTreeComponent) {
        this.currentSectionTreeComponent = sectionTreeComponent;
        sectionView.openNewBranchDialog();
    }

    /**
     * This method is used to show new section dialog
     * 
     */
    public void openNewSectionDialog() {
        sectionView.openNewSectionDialog();

    }

    // End of view management methods

    // Section management methods

    /**
     * This method is invoked when the user saves editions and push edit button
     * 
     * @param name
     *            is edited section name
     * @param description
     *            is edited section description
     */
    public void editSection(final String name, final String description) {

        final Section section = (Section) this.currentSectionTreeComponent
                .getSelectedObject();

        String errorLabel = validateSection(name, description);
        if (errorLabel != null) {
            sectionView.openErrorPopupInEditSectionDialog(errorLabel);
            return;
        }

        dialogManager.confirmEdition(name, new CreateUpdatePerformable(section,
                name, description));
        sectionView.closeEditSectionDialog();

    }

    /**
     * Method check section's name and description.
     * 
     * @param name
     *            for check
     * @param description
     *            for check
     * @return label i18n error
     * */
    protected String validateSection(String name, String description) {
        if (name == null || name.equals("")) {
            return ERROR_LABEL_SECTION_NAME_CANT_BE_VOID;
        }
        return null;
    }
    private String validateNameLength(String name){
    	if(name.length() > 254){
    		return ERROR_LABEL_SECTION_NAME_VERY_LONG;
    	}
    	return null;
    }
    


    /**
     * Method check uniqueness of the section's name and the description.
     * 
     * @param name
     *            for check
     * @param description
     *            for check
     * @return label i18n error
     * */
    protected String checkSectionUniqueness(String name, String description) {
        String result = validateSection(name, description);
        Section section = new Section();
        section.setName(name);
        section.setDescription(description);
        if (result != null) {
            return result;
        } else if (sectionService.isSectionExists(section)) {
            return ERROR_LABEL_SECTION_NAME_ALREADY_EXISTS;
        }
        return null;
    }

    /**
     * Create new Section object and save it if there no any Sections with the
     * same name in other cases (the name is already) should display error
     * dialog
     * 
     * @param name
     *            section
     * @param description
     *            section
     */
    public void addNewSection(final String name, final String description) {

        String errorLabel = checkSectionUniqueness(name, description);
        if (errorLabel == null){
        	errorLabel=validateNameLength(name);
        }
        if (errorLabel != null) {
            sectionView.openErrorPopupInNewSectionDialog(errorLabel);
            return;
        }
        dialogManager.confirmCreation(name, new CreateUpdatePerformable(null,
                name, description));

    }

    /**
     * Delete section via sectionService
     * 
     * @param recipient
     *            if specified than all branches should be add as children to
     *            this section. If null then all children should be also deleted
     */
    public void deleteSection(final Section recipient) {

        Object selectedObject = this.currentSectionTreeComponent
                .getSelectedObject();
        if (!(selectedObject instanceof Section)) {
            return;
        }
        final Section victim = (Section) selectedObject;
        dialogManager.confirmDeletion(victim.getName(),
                new DeleteSectionPerformable(victim, recipient));
        removeSectionFromView(victim);

    }

    /**
     * DI for dialog dialog manager might be used within spring
     * 
     * @param dialogManager
     *            manager to manage interaction with user
     */
    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    /**
     * @return a currect selected <code>SectionTreeComponent</code>
     */
    public SectionTreeComponent getCurrentSectionTreeComponent() {
        return currentSectionTreeComponent;
    }

    /**
     * @param currentSectionTreeComponent
     *            is current <code>SectionTreeComponent</code> that will process
     *            actions from presenter
     */
    public void setCurrentSectionTreeComponent(
            SectionTreeComponent currentSectionTreeComponent) {
        this.currentSectionTreeComponent = currentSectionTreeComponent;
    }
    
    /**
     * Method used to manage moderation dialog 
     * @param branch
     */
    public void openModeratorDialog(Object selectedObject){
        if(!(selectedObject instanceof Branch)){
            return;
        }
        sectionView.openModeratorDialog((Branch)selectedObject);
                
    }

    public class CreateUpdatePerformable implements DialogManager.Performable {

        private Section section;
        private String name;
        private String description;

        /**
         * The params are already checked and validated otherwise the behavior
         * can't be predictable
         * 
         * @param section to edit. If null then this section will be created
         * @param name new name for section
         * @param description new description for section
         */
        public CreateUpdatePerformable(Section section, String name,
                String description) {
            this.section = section;
            this.name = name;
            this.description = description;
        }

        @Override
        public void execute() {
            if (section == null) {
                // then we are going to create a new section
                section = new Section();
                section.setName(name);
                section.setDescription(description);
                doSaving();
                sectionView.showSection(section);
                sectionView.closeNewSectionDialog();
            } else {
                // then we are going to edit some section
                section.setName(name);
                section.setDescription(description);
                doSaving();
                // its not necessary here because of section was transferred as
                // a reference
                SectionPresenter.this.currentSectionTreeComponent
                        .updateSectionInView(section);
                sectionView.closeEditSectionDialog();
            }
        }

        /**
         * inner method to externalize a saving action
         */
        private void doSaving() {
            try {
                sectionService.saveSection(section);
            } catch (NotUniqueException e) {
                Logger.getLogger(BranchPresenter.class.getName()).log(
                        Level.SEVERE, null, e);
                sectionView
                        .openErrorPopupInNewSectionDialog("sections.error.exeption_during_saving_process");
            }
        }
    }

    public class DeleteBranchPerformable implements DialogManager.Performable {

        private Branch branch;

        /**
         * 
         * @param branch
         *            to be deleted
         */
        public DeleteBranchPerformable(Branch branch) {
            this.branch = branch;
        }

        @Override
        public void execute() {
            Section section = branch.getSection();
            section.getBranches().remove(branch);
            try {
                sectionService.saveSection(section);
            } catch (NotUniqueException e) {

            }
            updateView();
        }
    }

    public class DeleteSectionPerformable implements DialogManager.Performable {

        private Section victim;
        private Section recipient;

        /**
         * @param victim
         *            to be deleted
         * @param recipient
         *            is section which will adopt victim's branches, might be
         *            <code>null</code>
         */
        public DeleteSectionPerformable(Section victim, Section recipient) {
            this.victim = victim;
            this.recipient = recipient;
        }

        @Override
        public void execute() {
            if (recipient == null) {

                sectionService.deleteRecursively(victim);
                return;
            } else {
                sectionService.deleteAndMoveBranchesTo(victim, recipient);

                // should save also all children
                try {
                    sectionService.saveSection(recipient);
                } catch (NotUniqueException e) {
                    // TODO add appropriate handling of exception
                }
            }

        }
    }

}
