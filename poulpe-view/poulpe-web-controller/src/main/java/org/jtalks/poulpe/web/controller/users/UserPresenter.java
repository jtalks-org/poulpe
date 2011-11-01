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
package org.jtalks.poulpe.web.controller.users;

import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.EditListener;
import org.jtalks.poulpe.web.controller.WindowManager;

/**
 * Presenter of edit User page.
 * @author Vytautas Kashchuk
 */
public class UserPresenter {

	 public interface UserView {
		 
	 	void showFirstname(String firstname);

        void showLastname(String lastname);

        void showEmail(String email);

        void showPassword(String password);

        String getFirstname();
        
        String getLastname();
        
        String getEmail();
        
        String getPassword();
        
        void hideEditAction();
    }

    /**
     * Save and init view.
     * @param UserView
     */
    public void initView(UserView view) {
        this.view = view;
    }
    
    private DialogManager dialogManager;
    private WindowManager windowManager;
    private UserService userService;
    private UserView view;
    private User user;
    private EditListener<User> listener;

    /**
     * Set the UserService implementation.
     * @param userService impl of UserService
     */
    public void setUserService(UserService userService) {
		this.userService = userService;
	}
    
    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }
    
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }
    
    public void initializeForEdit(UserView view, User user, EditListener<User> listener) {
        this.view = view;
        this.listener = listener;
        try {
            this.user = userService.get(user.getId());
        } catch (NotFoundException e) {
            dialogManager.notify("item.doesnt.exist");
            closeView();
            return;
        }
        view.showFirstname(this.user.getFirstName());
        view.showLastname(this.user.getLastName());
        view.showEmail(this.user.getEmail());
        view.showPassword(this.user.getPassword());        
    }

    public void onUpdateAction() {
        update();
        closeView();
        listener.onUpdate(user);
    }
    
    public void onCancelEditAction() {
        closeView();
        listener.onCloseEditorWithoutChanges();
    }
    
    private void closeView() {
        windowManager.closeWindow(view);
    }

    private void update() {
        user.setFirstName(view.getFirstname());
        user.setLastName(view.getLastname());
        user.setEmail(view.getEmail());
        user.setPassword(view.getPassword());
        userService.updateUser(user);
    }
}