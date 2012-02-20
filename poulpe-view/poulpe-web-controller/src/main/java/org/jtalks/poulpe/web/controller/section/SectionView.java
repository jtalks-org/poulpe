package org.jtalks.poulpe.web.controller.section;

import java.util.List;

import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;

public interface SectionView {

    void showSection(PoulpeSection section);

    void removeSection(PoulpeSection section);

    void openNewSectionDialog();

    void closeEditSectionDialog();

    void openEditSectionDialog(PoulpeSection section);

    @Deprecated
    void openNewBranchDialog();

    @Deprecated
    void openEditBranchDialog(PoulpeBranch branch);

    void showSections(List<PoulpeSection> sections);

    void openModerationDialog(PoulpeBranch branch);

    void validationFailure(ValidationResult result);

}