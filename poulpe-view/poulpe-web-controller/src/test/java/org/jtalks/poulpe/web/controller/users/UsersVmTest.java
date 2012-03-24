package org.jtalks.poulpe.web.controller.users;

import static org.jtalks.poulpe.web.controller.users.UsersVm.EDIT_USER_DIALOG;
import static org.jtalks.poulpe.web.controller.users.UsersVm.EDIT_USER_ZUL;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationResult;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.User;
import org.jtalks.poulpe.service.UserService;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

public class UsersVmTest {
    private static final String SEARCH_STRING = "searchString";
    UsersVm vm;
    @Mock
    UserService service;
    @Mock
    ZkHelper zkHelper;
    @Mock 
    EntityValidator entityValidator;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vm = new UsersVm(service, entityValidator);
        vm.setZkHelper(zkHelper);
    }

    @Test
    public void testSearchUser() {
        vm.setSearchString(SEARCH_STRING);
        vm.searchUser();
        verify(service).getUsersByUsernameWord(SEARCH_STRING);
    }

    @Test
    public void testEditUser() throws Exception {
        vm.editUser(new User());
        verify(zkHelper, times(1)).wireToZul(EDIT_USER_ZUL);
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(zkHelper.findComponent(anyString())).thenReturn(new Window());
        when(entityValidator.validate(any(PoulpeBranch.class))).thenReturn(ValidationResult.EMPTY);
        User user = new User();

        vm.saveUser(user);

        verify(service).updateUser(user);
        verify(zkHelper).findComponent(EDIT_USER_DIALOG);
    }

    @Command
    public void testCancelEdit() {
        vm.cancelEdit();
        verify(zkHelper).findComponent(EDIT_USER_DIALOG);
    }
}
