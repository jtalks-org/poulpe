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
package org.jtalks.poulpe.web.controller.section.moderation;

import static org.jtalks.poulpe.web.controller.section.moderation.ModerationDialogPresenter.USER_ALREADY_MODERATOR;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.BranchService;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.jtalks.poulpe.web.controller.utils.ObjectCreator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Optional;

public class ModerationDialogPresenterTest {

    private ModerationDialogPresenter presenter;

    @Mock ModerationDialogView view;
    @Mock UserService userService;
    @Mock BranchService branchService;
    @Mock DialogManager dialogManager;
    
    @Mock EntityValidator entityValidator;

    private PoulpeBranch branch;

    private List<User> allUsers;
    private User user1, user2;
    
    private ValidationResult resultWithErrors = resultWithErrors();

    private ValidationResult resultWithErrors() {
        ValidationError error = new ValidationError("name", PoulpeBranch.BRANCH_ALREADY_EXISTS);
        Set<ValidationError> errors = Collections.singleton(error);
        return new ValidationResult(errors);
    }

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);

        branch = new PoulpeBranch("name", "description");

        presenter = new ModerationDialogPresenter();
        
        presenter.setBranchService(branchService);
        presenter.setUserService(userService);
        presenter.setDialogManager(dialogManager);
        presenter.setBranch(branch);
        presenter.initView(view);
        presenter.setEntityValidator(entityValidator);
        
        allUsers = ObjectCreator.getFakeUsers(5);
        when(userService.getAll()).thenReturn(allUsers);
        
        user1 = allUsers.get(1);
        user2 = allUsers.get(2);
    }

    @Test
    public void initView() {
        presenter.initView(view);
        verify(view).updateView(branch.getModeratorsList(), allUsers);
    }

    @Test
    public void onAddWithoutErrors() {
        presenter.onAdd(user1);
        assertTrue(branch.isModeratedBy(user1));
    }

    @Test
    public void onAddUserIsAlreadyModerator() {
        branch.addModerator(user1);

        presenter.onAdd(user1);

        verify(view, never()).updateView(branch.getModeratorsList(), allUsers);
        verify(view).showComboboxErrorMessage(USER_ALREADY_MODERATOR);
    }

    @Test
    public void onConfirm() {
        givenNoConstraintsViolated();
        presenter.onConfirm();
        verify(branchService).saveBranch(branch);
    }
    
    private void givenNoConstraintsViolated() {
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(ValidationResult.EMPTY);
    }

    @Test
    public void onConfirmWithError() throws Exception {
        givenConstraintViolated();
        presenter.onConfirm();
        
        verify(branchService, never()).saveBranch(any(PoulpeBranch.class));
        verify(dialogManager).notify("item.already.exist");
    }
    
   private void givenConstraintViolated() {
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(resultWithErrors);
    } 

    @Test
    public void onDeleteModeratorRemovedFromBranch() {
        branch.addModerators(user1, user2);

        presenter.onDelete(user2);

        List<User> moderators = branch.getModeratorsList();
        
        assertEquals(moderators.size(), 1);
        assertEquals(moderators.get(0), user1);
    }
    
    @Test
    public void onDeleteViewGetsUpdated() {
        branch.addModerator(user1);
        presenter.onDelete(user1);
        verify(view).updateView(branch.getModeratorsList(), allUsers);
    }

    @Test
    public void onReject() {
        presenter.onReject();
        verify(view).hideDialog();
    }

    @Test
    public void refreshView() {
        branch.addModerators(allUsers);
        presenter.refreshView();
        verify(view).updateView(branch.getModeratorsList(), allUsers);
    }

    @Test
    public void setBranch() {
        branch.addModerators(allUsers);
        PoulpeBranch newBranch = new PoulpeBranch("tt", "ttt");

        presenter.setBranch(newBranch);
        presenter.refreshView();

        verify(view).updateView(newBranch.getModeratorsList(), allUsers);
    }

    @Test
    public void updateView() {
        presenter.updateView(allUsers, allUsers);
        verify(view).updateView(allUsers, allUsers);
    }

    @Test
    public void validateUserWhenUserIsAlreadyModerator() {
        branch.addModerator(user1);
        Optional<String> error = presenter.validateUser(user1);
        assertEquals(error.get(), USER_ALREADY_MODERATOR);
    }
       
    @Test
    public void validateUserWhenUserIsNotAModerator() {
        branch.addModerator(user1);
        Optional<String> error = presenter.validateUser(user2);
        assertFalse(error.isPresent());
    }
    
}
