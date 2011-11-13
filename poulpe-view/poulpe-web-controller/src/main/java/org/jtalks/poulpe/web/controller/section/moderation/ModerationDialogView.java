package org.jtalks.poulpe.web.controller.section.moderation;

import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.model.entity.Branch;

public interface ModerationDialogView {
    
    /**
     * Update dialog form with a list of users which already moderators of the current branch
     * @param users
     */
    public void updateView(List<User> users,List<User> usersInCombo);
    
    /**
     * Show or hide dialog
     * @param show
     */
    public void showDialog(boolean show);   
    
    public void showComboboxErrorMessage(String message);


}
