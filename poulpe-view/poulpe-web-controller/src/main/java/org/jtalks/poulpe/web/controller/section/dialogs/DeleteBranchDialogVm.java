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

import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;
import org.jtalks.poulpe.web.controller.section.ForumStructureVm;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;

import static org.zkoss.util.resource.Labels.getLabel;

/**
 * This VM is responsible for deleting the branch: whether to move the content of the branch to the other branch or
 * should the branch be deleted with whole its content.
 *
 * @author stanislav bashkirtsev
 */
public class DeleteBranchDialogVm extends AbstractDialogVm {
    private static final String JCOMMUNE_CONNECTION_FAILED = "branches.error.jcommune_no_connection";
    private static final String JCOMMUNE_RESPONSE_FAILED = "branches.error.jcommune_no_response";
    private static final String JCOMMUNE_URL_FAILED = "branches.error.jcommune_no_url";
    private static final String BRANCH_DELETING_FAILED_DIALOG_TITLE = "branches.deleting_problem_dialog.title";
    private final ForumStructureVm forumStructureVm;
    private final ForumStructureService forumStructureService;

    /**
     * Constructor
     *
     * @param forumStructureVm      forum structure visual model
     * @param forumStructureService forum structure service
     */
    public DeleteBranchDialogVm(ForumStructureVm forumStructureVm, ForumStructureService forumStructureService) {
        this.forumStructureVm = forumStructureVm;
        this.forumStructureService = forumStructureService;
    }

    /**
     * Confirm delete branch with content
     */
    @Command
    @NotifyChange(SHOW_DIALOG)
    public void confirmDeleteBranchWithContent() {
        PoulpeBranch selectedBranch = forumStructureVm.getSelectedItemInTree().getBranchItem();
        try {
            forumStructureService.removeBranch(selectedBranch);
            forumStructureVm.removeBranchFromTree(selectedBranch);
        } catch (NoConnectionToJcommuneException e) {
            showError(JCOMMUNE_CONNECTION_FAILED);
        } catch (JcommuneRespondedWithErrorException ex) {
            showError(JCOMMUNE_RESPONSE_FAILED);
        } catch (JcommuneUrlNotConfiguredException ex) {
            showError(JCOMMUNE_URL_FAILED);
        }
    }

    /**
     * Delete branch command
     */
    @GlobalCommand("deleteBranch")
    @NotifyChange(SHOW_DIALOG)
    public void deleteBranch() {
        showDialog();
    }

    /**
     * Show error message
     *
     * @param message message
     */
    protected void showError(String message) {
        Messagebox.show(getLabel(message),
                getLabel(BRANCH_DELETING_FAILED_DIALOG_TITLE), Messagebox.OK, Messagebox.ERROR);
    }
}
