package org.jtalks.poulpe.web.controller.section;

import java.util.List;

import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Section;
import org.jtalks.poulpe.validation.ValidationResult;

public interface SectionView {

    void showSection(Section section);

    void removeSection(Section section);

    void openNewSectionDialog();

    void closeEditSectionDialog();

    void openEditSectionDialog(String name, String description);

    void closeDialogs();

    @Deprecated
    void openNewBranchDialog();

    @Deprecated
    void openEditBranchDialog(Branch branch);

    void showSections(List<Section> sections);

    void closeNewSectionDialog();

    void openModerationDialog(Branch branch);

    void validationFailure(ValidationResult result, boolean isNewSection);

}