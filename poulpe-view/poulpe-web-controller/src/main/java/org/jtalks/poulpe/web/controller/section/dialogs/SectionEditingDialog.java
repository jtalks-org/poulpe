package org.jtalks.poulpe.web.controller.section.dialogs;

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.web.controller.section.ForumStructureVm;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * @author stanislav bashkirtsev
 */
public class SectionEditingDialog {
    private static final String SHOW_DIALOG = "showDialog", EDITED_SECTION = "editedSection";
    private final ForumStructureVm forumStructureVm;
    private final ForumStructureService forumStructureService;
    private boolean showDialog;
    private PoulpeSection editedSection;

    public SectionEditingDialog(ForumStructureVm forumStructureVm, ForumStructureService forumStructureService) {
        this.forumStructureVm = forumStructureVm;
        this.forumStructureService = forumStructureService;
    }

    @GlobalCommand
    @NotifyChange({EDITED_SECTION, SHOW_DIALOG})
    public void showNewSectionDialog() {
        editedSection = new PoulpeSection();
        showDialog = true;
    }

    @GlobalCommand
    @NotifyChange({EDITED_SECTION, SHOW_DIALOG})
    public void showEditSectionDialog() {
        editedSection = forumStructureVm.getSelectedItemInTree().getSectionItem();
        showDialog = true;
    }

    @Command
    @NotifyChange(SHOW_DIALOG)
    public void save() {
        forumStructureVm.updateSectionInTree(editedSection);
        storeNewSection(editedSection);
    }

    void storeNewSection(PoulpeSection section) {
        Jcommune jcommune = (Jcommune) (Object) forumStructureVm.getTreeModel().getRoot().getData();
        jcommune.addSection(section);
        forumStructureService.saveJcommune(jcommune);
    }

    /**
     * By this flag the ZUL decides whether to show the branch editing dialog or not.
     *
     * @return whether to show the branch editing dialog
     */
    public boolean isShowDialog() {
        boolean result = showDialog;
        showDialog = false;
        return result;
    }

    public PoulpeSection getEditedSection() {
        return editedSection;
    }
}
