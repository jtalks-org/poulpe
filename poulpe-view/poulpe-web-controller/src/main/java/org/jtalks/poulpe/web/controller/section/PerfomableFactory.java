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

import java.util.Collections;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.BranchService;
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
    private BranchService branchService;
    private SectionView sectionView;
    private ZkSectionTreeComponent currentSectionTreeComponent;
    private ComponentService componentService;

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
    public Performable deleteSection(PoulpeSection victim, PoulpeSection recipient) {
        if (recipient == null) {
            return new DeleteSectionWithoutRecipientPerformable(victim);
        } else {
            return new DeleteSectionWithRecipientPerformable(victim, recipient);
        }
    }

    /**
     * Deletes a branch
     * 
     * @param branch to be deleted
     * @return instance to be performed by dialog manager
     */
    public Performable deleteBranch(PoulpeBranch branch) {
        return new DeleteBranchPerformable(branch);
    }

    /**
     * @param section to be updated
     * @return instance to be performed by dialog manager
     */
    public Performable updateSection(PoulpeSection section) {
        return new UpdatePerformable(section);
    }

    /**
     * @param section to be saved
     * @return instance to be performed by dialog manager
     */
    public Performable saveSection(PoulpeSection section, Jcommune forum) {
        return new CreatePerformable(section, forum);
    }

    /**
     * 
     * @param branchService a branch service instance
     */
    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * Implementation of {@link DialogManager.Performable} for deleting sections and for moving its branches to the
     * given recipient, performed when user confirms deletion
     * 
     * @author unascribed
     */
    private class DeleteSectionWithRecipientPerformable implements DialogManager.Performable {
        private final PoulpeSection victim;
        private final PoulpeSection recipient;

        /**
         * @param victim to be deleted
         * @param recipient is section which will adopt victim's branches
         */
        public DeleteSectionWithRecipientPerformable(PoulpeSection victim, PoulpeSection recipient) {
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
        private final PoulpeSection victim;

        public DeleteSectionWithoutRecipientPerformable(PoulpeSection victim) {
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
        private final PoulpeSection section;
        private final Jcommune forum;

        public CreatePerformable(PoulpeSection section, Jcommune forum) {
            this.section = section;
            this.forum = forum;
        }

        @Override
        public void execute() {
            forum.addSection(section);
            componentService.saveComponent(forum);
            sectionView.addSection(section);
            sectionView.closeEditSectionDialog();
        }
    }

    /**
     * Implementation of {@link DialogManager.Performable} for saving
     * sections, performed when user confirms deletion
     * 
     * @author unascribed
     */
    private class UpdatePerformable implements DialogManager.Performable {
        private final PoulpeSection section;

        public UpdatePerformable(PoulpeSection section) {
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
     * branches, performed when user confirms deletion.
     * When deleting branch, its group is deleted as well (and permissions granted with this group).
     * 
     * @author unascribed
     */
    private class DeleteBranchPerformable implements DialogManager.Performable {
        private final PoulpeBranch branch;

        public DeleteBranchPerformable(PoulpeBranch branch) {
            this.branch = branch;
        }

        @Override
        public void execute() {
            // TODO: move away to service
            PoulpeSection section = branch.getPoulpeSection();
            deleteBranchPermissions();
            section.getBranches().remove(branch);

            sectionService.saveSection(section);

            sectionPresenter.updateView();
        }

        private void deleteBranchPermissions() {
            for (BranchPermission permission : BranchPermission.values()) {
                PermissionChanges permissionChanges = new PermissionChanges(permission);
                permissionChanges.addRemovedGroups(Collections.singleton(branch.getModeratorsGroup()));
                branchService.changeGrants(branch, permissionChanges);
            }
        }
    }

    /**
     * @param service set section service instance
     */
    public void setSectionService(SectionService service) {
        this.sectionService = service;
    }

    /**
     * @param componentService set component service instance
     */
    public void setComponentService(ComponentService componentService) {
        this.componentService = componentService;
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
