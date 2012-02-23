package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.SectionService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.DialogManager.Performable;

/**
 * Factory class for producing {@link DialogManager.Performable} instances for
 * passing them to {@link DialogManager}. Used in {@link SectionPresenter} and
 * thus all operations which are returned by its factories are related to
 * sections.
 * 
 * @author Alexey Grigorev
 * @author Guram Savinov
 */
public class PerfomableFactory {

    private final SectionPresenter sectionPresenter;

    private SectionService sectionService;
    private ComponentService componentService;
    private SectionView sectionView;
    private ZkSectionTreeComponent currentSectionTreeComponent;

    /**
     * @param sectionPresenter
     */
    public PerfomableFactory(SectionPresenter sectionPresenter) {
        this.sectionPresenter = sectionPresenter;
    }

    /**
     * Deletes the section and moves all its branches to recipient, if any specified
     * 
     * @param victim section to be deleted
     * @param recipient section which will take all victim's branches, may be 
     * null if no recipient is needed
     * @return instance to be performed by dialog manager
     */
    public Performable deleteSection(Section victim, Section recipient) {
        if (recipient == null) {
            return new DeleteSectionWithoutRecipientPerformable(victim);
        } else {
            return new DeleteSectionWithRecipientPerformable(victim, recipient);
        }
    }

    /**
     * Deletes a branch
     * @param branch to be deleted
     * @return instance to be performed by dialog manager
     */
    public Performable deleteBranch(Branch branch) {
        return new DeleteBranchPerformable(branch);
    }

    /**
     * @param section to be updated
     * @return instance to be performed by dialog manager
     */
    public Performable updateSection(Section section) {
        return new UpdatePerformable(section);
    }

    /**
     * @param section to be saved
     * @return instance to be performed by dialog manager
     */
    public Performable saveSection(Section section) {
        return new CreatePerformable(section);
    }

    /**
     * Implementation of {@link DialogManager.Performable} for deleting
     * sections and for moving its branches to the given recipient, performed when user confirms deletion
     * 
     * @author unascribed
     */
    private class DeleteSectionWithRecipientPerformable implements DialogManager.Performable {
        private final Section victim;
        private final Section recipient;

        /**
         * @param victim to be deleted
         * @param recipient is section which will adopt victim's branches
         */
        public DeleteSectionWithRecipientPerformable(Section victim, Section recipient) {
            this.victim = victim;
            this.recipient = recipient;
        }

        @Override
        public void execute() {
            sectionService.deleteAndMoveBranchesTo(victim, recipient);
            sectionService.saveSection(recipient);
        }
    }

    /**
     * Implementation of {@link DialogManager.Performable} for deleting
     * sections, performed when user confirms deletion
     * 
     * @author unascribed
     */
    private class DeleteSectionWithoutRecipientPerformable implements DialogManager.Performable {
        private final Section victim;

        public DeleteSectionWithoutRecipientPerformable(Section victim) {
            this.victim = victim;
        }

        @Override
        public void execute() {
            sectionService.deleteRecursively(victim);
        }
    }

    /**
     * Implementation of {@link DialogManager.Performable} for creating (i.e. saving)
     * sections, performed when user confirms deletion
     * 
     * @author unascribed
     */
    private class CreatePerformable implements DialogManager.Performable {
        private final Section section;

        public CreatePerformable(Section section) {
            this.section = section;
        }

        @Override
        public void execute() {
        	Jcommune forum = (Jcommune) componentService.getByType(ComponentType.FORUM);
        	forum.addSection(section);
            componentService.saveComponent(forum);
            sectionView.showSection(section);
            sectionView.closeNewSectionDialog();
        }
    }

    /**
     * Implementation of {@link DialogManager.Performable} for saving
     * sections, performed when user confirms deletion
     * 
     * @author unascribed
     */
    private class UpdatePerformable implements DialogManager.Performable {
        private final Section section;

        public UpdatePerformable(Section section) {
            this.section = section;
        }

        @Override
        public void execute() {
            sectionService.saveSection(section);
            currentSectionTreeComponent.updateSectionInView(section);
            sectionView.closeEditSectionDialog();
        }

    }

    /**
     * Implementation of {@link DialogManager.Performable} for deleting
     * branches, performed when user confirms deletion
     * 
     * @author unascribed
     */
    private class DeleteBranchPerformable implements DialogManager.Performable {
        private final Branch branch;

        public DeleteBranchPerformable(Branch branch) {
            this.branch = branch;
        }

        @Override
        public void execute() {
            // TODO: move away to service
            Section section = branch.getSection();
            section.getBranches().remove(branch);

            sectionService.saveSection(section);

            sectionPresenter.updateView();
        }
    }

    /**
     * @param service set section service instance
     */
    public void setSectionService(SectionService service) {
        this.sectionService = service;
    }

    /**
     * @param service set component service instance
     */
    public void setComponentService(ComponentService service) {
        this.componentService = service;
    }

    /**
     * @param currentSectionTreeComponent that will process actions from
     * presenter
     */
    public void setCurrentSectionTreeComponent(ZkSectionTreeComponent currentSectionTreeComponent) {
        this.currentSectionTreeComponent = currentSectionTreeComponent;
    }

    /**
     * @param sectionView
     */
    public void setSectionView(SectionView sectionView) {
        this.sectionView = sectionView;
    }
}
