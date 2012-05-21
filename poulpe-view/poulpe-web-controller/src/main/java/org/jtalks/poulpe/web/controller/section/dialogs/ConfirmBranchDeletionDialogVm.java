package org.jtalks.poulpe.web.controller.section.dialogs;

/**
 * This VM is responsible for deleting the branch: whether to move the content of the branch to the other branch or
 * should the branch be deleted with whole its content.
 *
 * @author stanislav bashkirtsev
 */
public class ConfirmBranchDeletionDialogVm {
    private boolean showDialog;

    /**
     * Sets the flag of showing the dialog to {@code true}, is used by ZUL.
     *
     * @return this
     */
    public ConfirmBranchDeletionDialogVm showDialog() {
        showDialog = true;
        return this;
    }

    /**
     * Decides whether to show the dialog or not (is used by ZUL).
     *
     * @return {@code true} if the confirmation dialog should be shown, {@code false} otherwise
     */
    public boolean isShowDialog() {
        return showDialog;
    }
}
