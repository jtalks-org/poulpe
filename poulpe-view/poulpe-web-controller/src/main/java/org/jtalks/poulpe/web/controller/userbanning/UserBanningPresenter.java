package org.jtalks.poulpe.web.controller.userbanning;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.common.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.DialogManager;

/**
 * Presenter class for 'ban user' action
 * 
 * @author costa
 * 
 */
public class UserBanningPresenter {
    /**
     * Service to operate with {@link User} objects
     */
    private UserService userService;
    /**
     * Manager to use dialogs in view
     */
    private DialogManager dialogManager;

    private UserBanningView view;

    /**
     * Method to be invoked when view is initialized
     * 
     * @param view
     */
    public void initView(UserBanningView view) {
        this.view = view;
        List<User> users = userService.getAll();
        updateView(users);
    }

    public void updateView(List<User> listOfUsers) {
        view.updateView(listOfUsers);
    }
    
    public void banBasters(final List<User> usersToBan, final boolean permanent,final Integer daysOfBan,final String reason){
        dialogManager.confirmBan(usersToBan, reason, new BanningDialog(usersToBan, permanent, daysOfBan, reason));        
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public DialogManager getDialogManager() {
        return dialogManager;
    }

    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }
    
    public class BanningDialog implements DialogManager.Performable{
        
        private boolean permanent;
        private List<User> usersToBan;
        private Integer daysOfBan;
        private String reason;
        
        public BanningDialog(List<User> users, boolean permanent, Integer daysOfBan, String reason) {
            this.usersToBan = users;
            this.permanent = permanent;
            this.daysOfBan = daysOfBan;
            this.reason = reason;
        }
        
        @Override
        public void execute() {
            if(permanent){
                userService.setPermanentBanStatus(usersToBan, true, reason);
            }else{
                userService.setTemporaryBanStatus(usersToBan, daysOfBan, reason);
            }        
            view.clearView();
        }
    }
}
