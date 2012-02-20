package org.jtalks.poulpe.web.controller.section;

import java.util.List;

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.validator.ValidationFailure;

/**
 * Interface for displaying {@link PoulpeSection} and {@link PoulpeBranch} objects
 * 
 * @author Konstantin Akimov
 * @author Alexey Grigorev
 */
public interface SectionView extends ValidationFailure {

    /**
     * Adds a new section to view
     * @param section to be added
     */
    void addSection(PoulpeSection section);

    /**
     * Adds a list of sections to view
     * @param sections to be added
     */
    void addSections(List<PoulpeSection> sections);
    
    /**
     * @param section to be removed
     * @deprecated while it's not used (there's empty implementation in ZkSectionView)
     */
    @Deprecated
    void removeSection(PoulpeSection section);

    /**
     * Opens edit section dialog for creating a new section
     */
    void openNewSectionDialog();

    /**
     * Opens edit section dialog for saving a  section
     * @param section to be edited
     */
    void openEditSectionDialog(PoulpeSection section);

    /**
     * Closes edit dialog
     */
    void closeEditSectionDialog();

    /**
     * Opens edit branch dialog for creating a new branch
     */
    void openNewBranchDialog();

    /**
     * Opens edit branch dialog for editing a branch
     * @param branch
     */
    void openEditBranchDialog(PoulpeBranch branch);

    /**
     * Opens moderation dialog for assigning to the branch users who will moderate it
     * @param branch to be moderated
     */
    void openModerationDialog(PoulpeBranch branch);
}