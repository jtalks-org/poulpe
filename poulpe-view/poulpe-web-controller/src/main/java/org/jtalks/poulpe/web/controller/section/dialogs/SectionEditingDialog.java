package org.jtalks.poulpe.web.controller.section.dialogs;

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

    @GlobalCommand
    @NotifyChange({EDITED_SECTION, SHOW_DIALOG})
    public void showSectionDialog() {
        editedSection = forumStructureVm.getSelectedItemInTree().getSectionItem();
    }

    @Command
    @NotifyChange(SHOW_DIALOG)
    public void save() {
    }
}
