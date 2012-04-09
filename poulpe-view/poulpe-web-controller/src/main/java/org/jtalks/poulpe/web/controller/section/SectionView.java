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