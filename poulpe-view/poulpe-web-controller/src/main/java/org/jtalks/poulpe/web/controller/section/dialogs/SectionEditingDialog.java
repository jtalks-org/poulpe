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

    /** Constructor for initialization variables */
    public SectionEditingDialog(ForumStructureVm forumStructureVm, ForumStructureService forumStructureService) {
        this.forumStructureVm = forumStructureVm;
        this.forumStructureService = forumStructureService;
    }

    /** Creates and opens new section dialog */
    @GlobalCommand
    @NotifyChange({EDITED_SECTION, SHOW_DIALOG})
    public void showNewSectionDialog() {
        editedSection = new PoulpeSection();
        showDialog = true;
    }

    /** Opens current section dialog */
    @GlobalCommand
    @NotifyChange({EDITED_SECTION, SHOW_DIALOG})
    public void showEditSectionDialog() {
        editedSection = forumStructureVm.getSelectedItemInTree().getSectionItem();
        showDialog = true;
    }

    /** Updates current section in model and store it */
    @Command
    @NotifyChange(SHOW_DIALOG)
    public void save() {
        forumStructureVm.updateSectionInTree(editedSection);
        storeNewSection(editedSection);
    }

    /** Saves section through {@link jcommune} 
     * @param section new {@link PoulpeSection} object 
     */
    void storeNewSection(PoulpeSection section) {
        Jcommune jcommune = (Jcommune) (Object) forumStructureVm.getTreeModel().getRoot().getData();
        jcommune.addOrUpdateSection(section);
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

    /** @return {@link PoulpeSection} current edited section instance */
    public PoulpeSection getEditedSection() {
        return editedSection;
    }
}
