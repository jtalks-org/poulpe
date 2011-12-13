package org.jtalks.poulpe.web.controller.section.moderation;

import java.util.List;

import org.jtalks.common.model.entity.User;

public interface ModerationDialogView {
    
    /**
     * Update dialog form with a list of users which already moderators of the current branch
     * @param users
     */
    public void updateView(List<User> users, List<User> usersInCombo);
    
    /**
     * Show or hide dialog
     * @param show
     * @deprecated use {@link #showDialog()} and {@link #hideDialog()} instead
     */
    @Deprecated
    public void showDialog(boolean show);  
    
    /**
     * Shows the dialog
     */
    public void showDialog();
    
    /**
     * Hides the dialog
     */
    public void hideDialog();
    
    
    public void showComboboxErrorMessage(String message);
}
