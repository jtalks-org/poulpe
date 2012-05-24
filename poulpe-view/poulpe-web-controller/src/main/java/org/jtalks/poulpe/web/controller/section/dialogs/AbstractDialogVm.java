package org.jtalks.poulpe.web.controller.section.dialogs;

/**
 * @author stanislav bashkirtsev
 */
public class AbstractDialogVm {
    private boolean showDialog = false;

    /**
     * Sets the flag of showing the dialog to {@code true}, is used by ZUL.
     *
     * @return this
     */
    public AbstractDialogVm showDialog() {
        showDialog = true;
        return this;
    }

    /**
     * Decides whether to show the dialog or not (is used by ZUL). Changes to flag to {@code false} each time it's
     * invoked.
     *
     * @return {@code true} if the confirmation dialog should be shown, {@code false} otherwise
     */
    public boolean isShowDialog() {
        boolean tempResult = showDialog;
        showDialog = false;
        return tempResult;
    }
}
